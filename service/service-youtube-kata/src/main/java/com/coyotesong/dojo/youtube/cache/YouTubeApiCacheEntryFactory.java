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

package com.coyotesong.dojo.youtube.cache;

import com.coyotesong.dojo.youtube.lang3.MyToStringStyle;
import com.coyotesong.dojo.youtube.model.YouTubeApiCacheEntry;
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
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
public class YouTubeApiCacheEntryFactory {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeApiCacheEntryFactory.class);

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

    /**
     * Create a new instance populated with the contents of the userValue
     *
     * @param userValue   YouTube API response to be copied
     * @param requestJson summarized YouTube API request
     */
    public YouTubeApiCacheEntry newInstance(@NotNull GenericJson userValue, @NotNull String requestJson) {
        final YouTubeApiCacheEntry entry = newInstance(userValue);

        if (isNotBlank(requestJson)) {
            entry.setRequestJson(requestJson);
        }

        return entry;
    }

    /**
     * Create a new instance populated with the contents of the userValue
     *
     * @param userValue YouTube API response to be copied
     */
    public YouTubeApiCacheEntry newInstance(@NotNull GenericJson userValue) {
        final YouTubeApiCacheEntry entry = new YouTubeApiCacheEntry();

        Class<? extends GenericJson> responseClass = userValue.getClass();

        // There's no default JsonFactory
        // Question: is it worth the trouble to use a lock or concurrent map?
        if (!FACTORY_MAP.containsKey(responseClass)) {
            final JsonFactory factory = userValue.getFactory();
            if (factory != null) {
                FACTORY_MAP.put(responseClass, factory);
            }
        }

        entry.setEtag(apply(GET_ETAG, responseClass, userValue));
        entry.setEventId(apply(GET_EVENT_ID, responseClass, userValue));
        entry.setKind(apply(GET_KIND, responseClass, userValue));
        entry.setNextPageToken(apply(GET_NEXT_PAGE_TOKEN, responseClass, userValue));
        entry.setPrevPageToken(apply(GET_PREV_PAGE_TOKEN, responseClass, userValue));
        entry.setRegionCode(apply(GET_REGION_CODE, responseClass, userValue));
        entry.setVisitorId(apply(GET_VISITOR_ID, responseClass, userValue));

        final PageInfo pageInfo = apply(GET_PAGE_INFO, responseClass, userValue);
        if (pageInfo != null) {
            entry.setResultsPerPage(pageInfo.getResultsPerPage());
            entry.setTotalResults(pageInfo.getTotalResults());
        }

        if (FACTORY_MAP.containsKey(responseClass)) {
            try {
                final JsonFactory factory = FACTORY_MAP.get(responseClass);
                if (factory != null) {
                    entry.setContent(factory.toPrettyString(userValue));
                } else {
                    LOG.warn("factory was null ?!");
                }
            } catch (IOException e) {
                LOG.warn("{}: error serializing response {}: {}", e.getClass().getName(), responseClass.getName(), e.getMessage());
            }
        }

        entry.setLastUpdated(Instant.now(Clock.systemUTC()));

        return entry;
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
    /*
    public <T extends GenericJson> T valueOf() {
        if (FACTORY_MAP.containsKey(responseClass)) {
            try {
                @SuppressWarnings("unchecked")
                T t = (T) FACTORY_MAP.get(responseClass).fromString(content, responseClass);
                return t;
            } catch (IOException e) {
                LOG.warn("{}: error deserializing content: {}", e.getClass().getName(), e.getMessage());
            }
        }

        return null;
    }
     */

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

                case "getTokenPagination":

                case "getFactory":
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
