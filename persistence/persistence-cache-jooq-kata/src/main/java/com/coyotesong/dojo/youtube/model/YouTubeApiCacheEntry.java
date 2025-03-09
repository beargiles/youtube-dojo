/*
 * Copyright (c) 2024 Bear Giles <bgiles@coyotesong.com>.
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coyotesong.dojo.youtube.model;

import com.coyotesong.dojo.youtube.lang3.MyToStringStyle;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.youtube.model.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Clock;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

/**
 * YouTube API cache entry
 *
 * Implementation note: for maximum flexibility we want to keep the 'items' field in JSON -
 * this protects us when the content changes.
 *
 * An underlying database can still safely extract the contents into individual fields i
 * via CREATE and UPDATE triggers (or rules) and json parsing.
 */
@SuppressWarnings("unused")
public class YouTubeApiCacheEntry implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(YouTubeApiCacheEntry.class);

    private static final Map<Class<?>, Method> GET_ETAG = new LinkedHashMap<>();
    private static final Map<Class<?>, Method> GET_EVENT_ID = new LinkedHashMap<>();
    private static final Map<Class<?>, Method> GET_ITEMS = new LinkedHashMap<>(); // unused
    private static final Map<Class<?>, Method> GET_KIND = new LinkedHashMap<>();
    private static final Map<Class<?>, Method> GET_NEXT_PAGE_TOKEN = new LinkedHashMap<>();
    private static final Map<Class<?>, Method> GET_PAGE_INFO = new LinkedHashMap<>();
    private static final Map<Class<?>, Method> GET_PREV_PAGE_TOKEN = new LinkedHashMap<>();
    private static final Map<Class<?>, Method> GET_REGION_CODE = new LinkedHashMap<>();
    private static final Map<Class<?>, Method> GET_VISITOR_ID = new LinkedHashMap<>();
    private static final Map<Class<?>, JsonFactory> FACTORY_MAP = new LinkedHashMap<>();

    static {
        // we could perform this as-needed but that would require adding every time
        // the copy constructor is called. This is better since we know there's only
        // a very limited number of supported responses.
        initialize(CaptionListResponse.class,
                ChannelListResponse.class,
                ChannelSectionListResponse.class,
                I18nLanguageListResponse.class,
                I18nRegionListResponse.class,
                PlaylistImageListResponse.class,
                PlaylistItemListResponse.class,
                PlaylistListResponse.class,
                SearchListResponse.class,
                VideoCategoryListResponse.class,
                VideoListResponse.class);
    }

    // we don't expose this
    private Class<? extends GenericJson> responseClass;

    private Integer key;
    private String requestJson;
    private String etag;
    private String kind;
    private String regionCode;
    private String eventId;
    private String visitorId;
    private Integer resultsPerPage;
    private Integer totalResults;
    private String nextPageToken;
    private String prevPageToken;
    // tokenPagination ?
    private String content;
    // private String jsonContent;  - in request
    // exception?

    // not an Instant in order to avoid potential problems.
    private Instant lastUpdated;

    /**
     * Default constructor
     */
    public YouTubeApiCacheEntry() {
    }

    /**
     * Copy constructor
     * <p>
     * Not all YouTube API responses contain all top-level fields.
     * <p>
     * This constructor does not/can not set requestJson or status code
     *
     * @param userValue   YouTube API response to be copied
     * @param requestJson summarized YouTube API request
     */
    protected YouTubeApiCacheEntry(@NotNull GenericJson userValue, @NotNull String requestJson) {
        this(userValue);
        this.requestJson = requestJson;
    }

    /**
     * Copy constructor
     * <p>
     * Not all YouTube API responses contain all top-level fields.
     * <p>
     * This constructor does not/can not set requestJson or status code
     *
     * @param userValue YouTube API response to be copied
     */
    protected YouTubeApiCacheEntry(@NotNull GenericJson userValue) {
        this.responseClass = userValue.getClass();

        // There's no default JsonFactory
        // Question: is it worth the trouble to use a lock or concurrent map?
        if (!FACTORY_MAP.containsKey(responseClass)) {
            final JsonFactory factory = userValue.getFactory();
            if (factory != null) {
                FACTORY_MAP.put(responseClass, factory);
            }
        }

        this.etag = apply(GET_ETAG, responseClass, userValue);
        this.eventId = apply(GET_EVENT_ID, responseClass, userValue);
        this.kind = apply(GET_KIND, responseClass, userValue);
        this.nextPageToken = apply(GET_NEXT_PAGE_TOKEN, responseClass, userValue);
        this.prevPageToken = apply(GET_PREV_PAGE_TOKEN, responseClass, userValue);
        this.regionCode = apply(GET_REGION_CODE, responseClass, userValue);
        this.visitorId = apply(GET_VISITOR_ID, responseClass, userValue);

        final PageInfo pageInfo = apply(GET_PAGE_INFO, responseClass, userValue);
        if (pageInfo != null) {
            this.resultsPerPage = pageInfo.getResultsPerPage();
            this.totalResults = pageInfo.getTotalResults();
        }

        if (FACTORY_MAP.containsKey(responseClass)) {
            try {
                final JsonFactory factory = FACTORY_MAP.get(responseClass);
                if (factory != null) {
                    this.content = factory.toPrettyString(userValue);
                } else {
                    LOG.warn("factory was null ?!");
                }
            } catch (IOException e) {
                LOG.warn("{}: error serializing response {}: {}", e.getClass().getName(), responseClass.getName(), e.getMessage());
            }
        }

        this.lastUpdated = Instant.now(Clock.systemUTC());
    }

    @SuppressWarnings("unchecked")
    private static <T> T apply(Map<Class<?>, Method> map, Class<? extends GenericJson> clz, GenericJson userValue) {
        if (map.containsKey(clz)) {
            try {
                return (T) map.get(clz).invoke(userValue);
            } catch (InvocationTargetException | IllegalAccessException e) {
                LOG.warn("{}: error retrieving {}#{}: {}", e.getClass().getName(), clz.getName(), map.get(clz).getName(), e.getMessage());
            }
        }
        return null;
    }

    /**
     * Convert this object to YouTube API response
     *
     * @param <T> class of YouTube API response
     * @return YouTube API response
     */
    public <T extends GenericJson> T valueOf() {
        LOG.info("valueOf({})", kind);
        if (FACTORY_MAP.containsKey(responseClass)) {
            LOG.info("converting!");
            try {
                @SuppressWarnings("unchecked")
                T t = (T) FACTORY_MAP.get(responseClass).fromString(content, responseClass);
                return t;
            } catch (IOException e) {
                LOG.warn("{}: error deserializing content: {}", e.getClass().getName(), e.getMessage());
            }
        } else {
            LOG.info("no factory map found!");
        }

        return null;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getRequestJson() {
        return requestJson;
    }

    public void setRequestJson(String requestJson) {
        this.requestJson = requestJson;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    @Nullable
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Nullable
    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    public Integer getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(Integer resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    @Nullable
    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    @Nullable
    public String getPrevPageToken() {
        return prevPageToken;
    }

    public void setPrevPageToken(String prevPageToken) {
        this.prevPageToken = prevPageToken;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof YouTubeApiCacheEntry that)) return false;

        // do not include 'key'!
        return new EqualsBuilder()
                .append(responseClass, that.responseClass)
                .append(requestJson, that.requestJson)
                .append(kind, that.kind)
                .append(content, that.content)
                .append(etag, that.etag)
                .append(regionCode, that.regionCode)
                .append(eventId, that.eventId)
                .append(visitorId, that.visitorId)
                .append(resultsPerPage, that.resultsPerPage)
                .append(totalResults, that.totalResults)
                .append(nextPageToken, that.nextPageToken)
                .append(prevPageToken, that.prevPageToken)
                .append(lastUpdated, that.lastUpdated)
                .isEquals();
    }

    @Override
    public int hashCode() {
        // do not include 'key'!
        return new HashCodeBuilder(17, 37)
                .append(requestJson)
                .append(kind)
                .append(content)
                .append(etag)
                .toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this, MyToStringStyle.DEFAULT_STYLE)
                .append("requestJson", requestJson)
                .append("etag", etag)
                .append("kind", kind)
                .append("regionCode", regionCode)
                // .append("eventId", eventId)
                // .append("visitorId", visitorId)
                .append("resultsPerPage", resultsPerPage)
                .append("totalResults", totalResults)
                .append("nextPageToken", nextPageToken)
                .append("prevPageToken", prevPageToken)
                .append("lastUpdated", (lastUpdated == null) ? null : ISO_INSTANT.format(lastUpdated))
                .append("content", (content == null) ? null : "\n  >   " + content.substring(0, Math.min(100, content.length())).replace("\n", "\n  >   "))
                .toString();
    }

    /**
     * Initialize static maps for clas -> method
     *
     * @param classes YouTube API response classes
     */
    static void initialize(@NotNull Class<?>... classes) {
        for (Class<?> clz : classes) {
            initialize(clz);
        }
    }

    /**
     * Initialize static maps for class -> method
     *
     * @param clz individual YouTube API response class
     */
    static void initialize(@NotNull Class<?> clz) {
        // using a loop lets us avoid dealing with NoSuchMethodExceptions
        for (Method m : clz.getMethods()) {
            // make sure this is a 'getter'
            if (!m.getName().startsWith("get") || m.getParameterCount() > 0) {
                continue;
            }

            // plus a little more paranoia...
            if (Void.class.equals(m.getReturnType())) {
                continue;
            }

            switch (m.getName()) {
                case "getEtag":
                    GET_ETAG.put(clz, m);
                    break;

                case "getEventId":
                    GET_EVENT_ID.put(clz, m);
                    break;

                case "getKind":
                    GET_KIND.put(clz, m);
                    break;

                case "getNextPageToken":
                    GET_NEXT_PAGE_TOKEN.put(clz, m);
                    break;

                case "getPageInfo":
                    GET_PAGE_INFO.put(clz, m);
                    break;

                case "getPrevPageToken":
                    GET_PREV_PAGE_TOKEN.put(clz, m);
                    break;

                case "getRegionCode":
                    GET_REGION_CODE.put(clz, m);
                    break;

                case "getVisitorId":
                    GET_VISITOR_ID.put(clz, m);
                    break;

                case "getItems":
                    GET_ITEMS.put(clz, m);
                    break;

                case "getFactory":
                    // this is usually null
                    // @TODO - was something lost here?
                    // try {
                    //    //
                    //}
                    // LOG.info("getFactory is not null!");
                    // FACTORY_MAP.put(clz, m);
                    break;

                case "getTokenPagination":
                case "getUnknownKeys":
                case "getClass":
                case "getClassInfo":
                    // standard...
                    break;

                default:
                    // I know there's a 'getSummary()'...
                    LOG.info("unhandled getter: {}", m.getName());
            }
        }
    }
}
