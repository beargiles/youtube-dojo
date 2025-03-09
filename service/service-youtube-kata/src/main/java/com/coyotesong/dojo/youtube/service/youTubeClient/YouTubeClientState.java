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

package com.coyotesong.dojo.youtube.service.youTubeClient;

import com.coyotesong.dojo.youtube.service.YouTubeApiCacheService;
import com.coyotesong.dojo.youtube.lang3.MyToStringStyle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.json.GenericJson;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequest;
import com.google.api.services.youtube.model.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * YouTube REST Client state information
 *
 * @param <R> our class
 * @param <C> YouTube REST client method (e.g., YouTube.Videos.List)
 * @param <S> YouTube REST client results (e.g., VideoListResponse)
 * @param <T> YouTube REST client element class (e.g., Video)
 */
@SuppressWarnings("unused")
public class YouTubeClientState<R, C extends YouTubeRequest<S>, S extends GenericJson, T extends GenericJson> implements Iterator<List<R>> {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeClientState.class);

    /**
     * Static maps from request/response classname to method.
     * <p>
     * These maps are statically initialized since we know which API calls we support.
     * </p>
     * <p>
     * This is required since the YouTube API doesn't have an appropriate abstraction
     * layer for these methods. Nearly all classes implement them, and the exceptions
     * could support dummy implementations.
     * </p>
     * <p>
     * Another option is modifying the bytecode to insert a new abstraction layer
     * but that's overkill here since we can restrict all access to this class.
     * </p>
     */
    private static final Map<String, Method> GET_HL = new LinkedHashMap<>();
    private static final Map<String, Method> SET_HL = new LinkedHashMap<>();
    private static final Map<String, Method> SET_MAX_RESULTS = new LinkedHashMap<>();
    private static final Map<String, Method> SET_PAGE_TOKEN = new LinkedHashMap<>();
    private static final Map<String, Method> SET_PARENT_ETAG = new LinkedHashMap<>();

    private static final Map<Class<?>, String> KIND_MAP = new LinkedHashMap<>();

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @NotNull
    private final C request;

    @NotNull
    private final Function<T, R> convert;

    @NotNull
    private final String requestClassName;

    private final YouTubeApiCacheService cache;

    private String etag;
    private String eventId;
    private String nextPageToken;
    private PageInfo pageInfo;
    private String regionCode;
    private String visitorId;
    private List<R> items = Collections.emptyList();

    private boolean finished;
    private boolean failed;

    private final Map<String, Object> keyMap = new LinkedHashMap<>();

    protected YouTubeClientState(@NotNull C request, @NotNull Function<T, R> convert) {
        this(request, convert, null);
    }

    @SuppressWarnings("rawtypes")
    protected YouTubeClientState(@NotNull C request, @NotNull Function<T, R> convert, YouTubeApiCacheService cache) {
        this.request = request;
        this.convert = convert;
        this.cache = cache;
        this.requestClassName = request.getClass().getName();

        // this breaks serialization/deserialization below
        // request.setReturnRawInputStream(true);

        if (KIND_MAP.containsKey(request.getResponseClass())) {
            keyMap.put("kind", KIND_MAP.get(request.getResponseClass()));
        }

        // use a sorted list for everything else in order to be unambiguous
        final List<String> keys = new ArrayList<>(request.keySet());
        keys.remove("accessToken");
        keys.remove("key");
        keys.remove("part"); // should this be left in?
        keys.remove("oauthToken");
        Collections.sort(keys);
        for (String key : keys) {
            final Object o = request.get(key);
            if ((o instanceof List) && !((List) o).isEmpty() && (((List) o).get(0) instanceof Comparable)) {
                @SuppressWarnings("unchecked")
                final List<? extends Comparable<? super Comparable<?>>> l = new ArrayList<>((List) o);
                Collections.sort(l);
                keyMap.put(key, l);
            } else {
                keyMap.put(key, o);
            }
        }
    }

    public String getEtag() {
        return etag;
    }

    public String getEventId() {
        return eventId;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public String getVisitorId() {
        return visitorId;
    }

    public List<R> getItems() {
        return items;
    }

    public boolean hasNext() {
        return !failed && !finished;
    }

    @NotNull
    public List<R> next() {
        return items;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    /**
     * Perform indirect access to getHl() method.
     */
    @Nullable
    String getHl() {
        final Method m = GET_HL.get(requestClassName);
        if (m != null) {
            try {
                return (String) m.invoke(request);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.info("Error when invoking getHl()");
            }
        }

        return null;
    }

    /**
     * Perform indirect access to setHl(String) method.
     */
    void setHl(@NotNull R value, String hl) {
        final Method m = SET_HL.get(requestClassName);
        if (m != null) {
            try {
                m.invoke(value, hl);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.info("Error when invoking setHl()");
            }
        }
    }

    /**
     * Perform indirect access to setMaxResults(Long) method.
     *
     * @param count max results, from 1 to 50 inclusive.
     */
    void setMaxResults(int count) {
        if (!(0 < count && count <= 50)) {
            throw new IllegalArgumentException("'count' is not in the range from 1 to 50, inclusive");
        }

        final Method m = SET_MAX_RESULTS.get(requestClassName);
        if (m != null) {
            try {
                m.invoke(request, (long) count);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.info("Error when invoking setMaxResults()");
            }
        }
    }

    /**
     * Perform indirect access to setPageToken(String) method
     *
     * @param pageToken page token
     */
    void setPageToken(@Nullable String pageToken) {
        final Method m = SET_PAGE_TOKEN.get(requestClassName);
        if (m != null) {
            try {
                m.invoke(request, pageToken);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.info("Error when invoking setPageToken()");
            }
        }
    }

    /**
     * Perform indirect access to setParentEtag(String) method
     *
     * @param value target
     * @param etag  parent etag
     */
    void setParentEtag(@NotNull R value, @Nullable String etag) {
        final Method m = SET_PARENT_ETAG.get(requestClassName);
        if (m != null) {
            try {
                m.invoke(value, etag);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.info("Error when invoking setParentEtag()");
            }
        }
    }

    String updateKeyWithPageToken() throws IOException {
        keyMap.remove("pageToken");
        if (isNotBlank(nextPageToken)) {
            keyMap.put("pageToken", nextPageToken);
        }
        return MAPPER.writeValueAsString(keyMap);
    }

    /**
     * Perform YouTube API call.
     * <p>
     * This assumes request is already initialized with key (API Key), access token
     * (OAuth Token), and quota user.
     * </p>
     */
    public void update() throws IOException {
        // this.setMaxResults(50);
        this.setPageToken(nextPageToken);
        final S response = cache.get(updateKeyWithPageToken(), request::execute);

        if (response == null) {
            LOG.warn("null response!");
            return;
        }

        this.etag = (String) response.get("etag");
        this.pageInfo = (PageInfo) response.get("pageInfo");

        // this is often null
        this.regionCode = (String) response.get("regionCode");

        // this.eventId = response.getEventId();
        // this.visitorId = response.getVisitorId();

        @SuppressWarnings("unchecked") final List<T> items = (List<T>) response.get("items");
        if ((items == null) || items.isEmpty()) {
            this.items = Collections.emptyList();
            this.nextPageToken = null;
        } else {
            this.items = items.stream().map(convert).toList();
            this.nextPageToken = (String) response.get("nextPageToken");

            // copy additional value from response
            this.items.forEach(s -> this.setParentEtag(s, this.etag));

            // copy additional value from request
            final String hl = this.getHl();
            if (isNotBlank(hl)) {
                // this will not overwrite values provided by response
                this.items.forEach(s -> this.setHl(s, hl));
            }
        }
    }

    static {
        initialize(YouTube.Search.List.class, SearchListResponse.class, com.coyotesong.dojo.youtube.model.SearchResult.class);

        initialize(YouTube.Channels.List.class, ChannelListResponse.class, com.coyotesong.dojo.youtube.model.Channel.class);
        initialize(YouTube.PlaylistImages.List.class, PlaylistImageListResponse.class, com.coyotesong.dojo.youtube.model.PlaylistImage.class);
        initialize(YouTube.PlaylistItems.List.class, PlaylistItemListResponse.class, com.coyotesong.dojo.youtube.model.PlaylistItem.class);
        initialize(YouTube.Playlists.List.class, PlaylistListResponse.class, com.coyotesong.dojo.youtube.model.Playlist.class);
        initialize(YouTube.Videos.List.class, VideoListResponse.class, com.coyotesong.dojo.youtube.model.Video.class);

        // constants(?) - they always return a single page (nextPageToken = null)
        initialize(YouTube.ChannelSections.List.class, ChannelSectionListResponse.class, com.coyotesong.dojo.youtube.model.ChannelSection.class);
        initialize(YouTube.I18nLanguages.List.class, I18nLanguageListResponse.class, com.coyotesong.dojo.youtube.model.I18nLanguage.class);
        initialize(YouTube.I18nRegions.List.class, I18nRegionListResponse.class, com.coyotesong.dojo.youtube.model.I18nRegion.class);
        initialize(YouTube.VideoCategories.List.class, VideoCategoryListResponse.class, com.coyotesong.dojo.youtube.model.VideoCategory.class);

        KIND_MAP.put(CaptionListResponse.class, "youtube#captionListResponse");
        KIND_MAP.put(ChannelListResponse.class, "youtube#channelListResponse");
        KIND_MAP.put(ChannelSectionListResponse.class, "youtube#channelSectionListResponse");
        KIND_MAP.put(I18nLanguageListResponse.class, "youtube#i18nLanguageListResponse");
        KIND_MAP.put(I18nRegionListResponse.class, "youtube#i18nRegionListResponse");
        KIND_MAP.put(PlaylistImageListResponse.class, "youtube#playlistImageListResponse");
        KIND_MAP.put(PlaylistItemListResponse.class, "youtube#playlistItemListResponse");
        KIND_MAP.put(PlaylistListResponse.class, "youtube#playlistListResponse");
        KIND_MAP.put(SearchListResponse.class, "youtube#searchListResponse");
        KIND_MAP.put(VideoListResponse.class, "youtube#videoListResponse");
        KIND_MAP.put(VideoCategoryListResponse.class, "youtube#videoCategoryListResponse");

        //
        // These are the remaining 'List' requests. Many of them are restricted to
        // authenticated users, and often to the owner of the channel.
        //
        // YouTube.Activities.List
        // YouTube.Captions.List
        // YouTube.CommentThreads.List
        // YouTube.Comments.List
        // YouTube.LiveBroadcasts.List
        // YouTube.LiveChatMessages.List
        // YouTube.LiveChatModerators.List
        // YouTube.LiveStreams.List
        // YouTube.Members.List
        // YouTube.MembershipLevels.List
        // YouTube.Subscriptions.List
        // YouTube.SuperChatEvents.List
        // YouTube.ThirdPartyLinks.List
        //
        // definitely requires OAuth token
        // YouTube.VideoAbuseReportReasons.List
    }

    /**
     * Initialize maps from request/response to method
     *
     * @param request  request class
     * @param response response class
     * @param target   target class
     */
    static void initialize(@NotNull Class<? extends YouTubeRequest<?>> request,
                           @NotNull Class<? extends GenericJson> response, @NotNull Class<?> target) {
        final String requestClassName = request.getName();
        final String responseClassName = response.getName();

        // not all requests include 'getHl()'
        try {
            final Method m = request.getMethod("getHl");
            GET_HL.put(requestClassName, m);
            SET_HL.put(requestClassName, null);
            if (GET_HL.containsKey(requestClassName)) {
                // assume that the existing conversion method already has 'setHl()' if
                // the response has 'getHl()'.
                try {
                    final Method m1 = response.getMethod("getHl");
                    // nothing to do...
                } catch (NoSuchMethodException e1) {
                    try {
                        final Method m2 = target.getMethod("setHl", String.class);
                        SET_HL.put(requestClassName, m2);
                    } catch (NoSuchMethodException e2) {
                        LOG.info("Unable to find {}#setHl(String)", target.getName());
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            LOG.trace("Unable to find getHl()' on {}", requestClassName);
            GET_HL.put(requestClassName, null);
            SET_HL.put(requestClassName, null);
        }

        try {
            final Method m = request.getMethod("setMaxResults", Long.class);
            SET_MAX_RESULTS.put(requestClassName, m);
        } catch (NoSuchMethodException e) {
            LOG.trace("Unable to find 'setMaxResults(Long)' on {}", requestClassName);
            SET_MAX_RESULTS.put(requestClassName, null);
        }

        try {
            final Method m = request.getMethod("setPageToken", String.class);
            SET_PAGE_TOKEN.put(requestClassName, m);
        } catch (NoSuchMethodException e) {
            LOG.trace("Unable to find 'setPageToken(String)' on {}", requestClassName);
            SET_PAGE_TOKEN.put(requestClassName, null);
        }

        // we can use requestClassName since it's unique
        try {
            final Method m = target.getMethod("setParentEtag", String.class);
            SET_PARENT_ETAG.put(requestClassName, m);
        } catch (NoSuchMethodException e) {
            LOG.info("Unable to find 'setParentEtag(String)' on {}", target.getName());
            SET_PARENT_ETAG.put(requestClassName, null);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, MyToStringStyle.DEFAULT_STYLE)
                .append("classname", requestClassName)
                .append("etag", etag)
                .append("eventId", eventId)
                .append("nextPageToken", nextPageToken)
                .append("item cnt", items.size())
                .append("finished", finished)
                .append("failed", failed)
                .toString();
        // pageInfo
        // regionCode
    }
}
