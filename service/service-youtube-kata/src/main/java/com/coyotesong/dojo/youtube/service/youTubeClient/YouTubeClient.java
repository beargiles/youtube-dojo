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

import com.coyotesong.dojo.youtube.model.Caption;
import com.coyotesong.dojo.youtube.model.Channel;
import com.coyotesong.dojo.youtube.model.ChannelSection;
import com.coyotesong.dojo.youtube.model.I18nLanguage;
import com.coyotesong.dojo.youtube.model.I18nRegion;
import com.coyotesong.dojo.youtube.model.Playlist;
import com.coyotesong.dojo.youtube.model.PlaylistImage;
import com.coyotesong.dojo.youtube.model.PlaylistItem;
import com.coyotesong.dojo.youtube.model.SearchResult;
import com.coyotesong.dojo.youtube.model.Video;
import com.coyotesong.dojo.youtube.model.VideoCategory;
import com.coyotesong.dojo.youtube.service.*;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.GenericJson;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequest;
import com.google.api.services.youtube.model.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * YouTube REST Client.
 * <p>
 * See https://googleapis.github.io/google-http-java-client/unit-testing.html
 * </p>
 *
 * @param <R> our class
 * @param <C> YouTube REST client method (e.g., YouTube.Videos.List)
 * @param <S> YouTube REST client results (e.g., VideoListResponse)
 * @param <T> YouTube REST client element class (e.g., Video)
 */
@SuppressWarnings("unused")
public class YouTubeClient<R, C extends YouTubeRequest<S>, S extends GenericJson, T extends GenericJson> implements Iterator<List<R>> {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeClient.class);

    /**
     * Counter used to prevent exhausting our quota in a single API call.
     */
    private static final int MAX_COUNTER = 2;

    /**
     * Convenience class for YouTube Captions client
     */
    public static class ListCaptions extends YouTubeClient<Caption, YouTube.Captions.List, CaptionListResponse, com.google.api.services.youtube.model.Caption> {
        public ListCaptions(@NotNull YouTubeClientState<Caption, YouTube.Captions.List, CaptionListResponse, com.google.api.services.youtube.model.Caption> state) {
            super(state);
        }
    }

    /**
     * Convenience class for YouTube Channels client
     */
    public static class ListChannels extends YouTubeClient<Channel, YouTube.Channels.List, ChannelListResponse, com.google.api.services.youtube.model.Channel> {
        public ListChannels(@NotNull YouTubeClientState<Channel, YouTube.Channels.List, ChannelListResponse, com.google.api.services.youtube.model.Channel> state) {
            super(state);
        }
    }

    /**
     * Convenience class for YouTube ChannelSections client
     */
    public static class ListChannelSections extends YouTubeClient<ChannelSection, YouTube.ChannelSections.List, ChannelSectionListResponse, com.google.api.services.youtube.model.ChannelSection> {
        public ListChannelSections(@NotNull YouTubeClientState<ChannelSection, YouTube.ChannelSections.List, ChannelSectionListResponse, com.google.api.services.youtube.model.ChannelSection> state) {
            super(state);
        }
    }

    /**
     * Convenience class for YouTube I18nLanguages client
     */
    public static class ListI18nLanguages extends YouTubeClient<I18nLanguage, YouTube.I18nLanguages.List, I18nLanguageListResponse, com.google.api.services.youtube.model.I18nLanguage> {
        public ListI18nLanguages(@NotNull YouTubeClientState<I18nLanguage, YouTube.I18nLanguages.List, I18nLanguageListResponse, com.google.api.services.youtube.model.I18nLanguage> state) {
            super(state);
        }
    }

    /**
     * Convenience class for YouTube I18nRegions client
     */
    public static class ListI18nRegions extends YouTubeClient<I18nRegion, YouTube.I18nRegions.List, I18nRegionListResponse, com.google.api.services.youtube.model.I18nRegion> {
        public ListI18nRegions(@NotNull YouTubeClientState<I18nRegion, YouTube.I18nRegions.List, I18nRegionListResponse, com.google.api.services.youtube.model.I18nRegion> state) {
            super(state);
        }
    }

    /**
     * Convenience class for YouTube PlaylistImages client
     */
    public static class ListPlaylistImages extends YouTubeClient<PlaylistImage, YouTube.PlaylistImages.List, PlaylistImageListResponse, com.google.api.services.youtube.model.PlaylistImage> {
        public ListPlaylistImages(@NotNull YouTubeClientState<PlaylistImage, YouTube.PlaylistImages.List, PlaylistImageListResponse, com.google.api.services.youtube.model.PlaylistImage> state) {
            super(state);
        }
    }

    /**
     * Convenience class for YouTube PlaylistItems client
     */
    public static class ListPlaylistItems extends YouTubeClient<PlaylistItem, YouTube.PlaylistItems.List, PlaylistItemListResponse, com.google.api.services.youtube.model.PlaylistItem> {
        public ListPlaylistItems(@NotNull YouTubeClientState<PlaylistItem, YouTube.PlaylistItems.List, PlaylistItemListResponse, com.google.api.services.youtube.model.PlaylistItem> state) {
            super(state);
        }
    }

    /**
     * Convenience class for YouTube Playlists client
     */
    public static class ListPlaylists extends YouTubeClient<Playlist, YouTube.Playlists.List, PlaylistListResponse, com.google.api.services.youtube.model.Playlist> {
        public ListPlaylists(@NotNull YouTubeClientState<Playlist, YouTube.Playlists.List, PlaylistListResponse, com.google.api.services.youtube.model.Playlist> state) {
            super(state);
        }
    }

    /**
     * Convenience class for YouTube Search client
     */
    public static class ListSearchResults extends YouTubeClient<SearchResult, YouTube.Search.List, SearchListResponse, com.google.api.services.youtube.model.SearchResult> {
        public ListSearchResults(@NotNull YouTubeClientState<SearchResult, YouTube.Search.List, SearchListResponse, com.google.api.services.youtube.model.SearchResult> state) {
            super(state);
        }
    }

    /**
     * Convenience class for YouTube VideoCategories client
     */
    public static class ListVideoCategories extends YouTubeClient<VideoCategory, YouTube.VideoCategories.List, VideoCategoryListResponse, com.google.api.services.youtube.model.VideoCategory> {
        public ListVideoCategories(@NotNull YouTubeClientState<VideoCategory, YouTube.VideoCategories.List, VideoCategoryListResponse, com.google.api.services.youtube.model.VideoCategory> state) {
            super(state);
        }
    }

    /**
     * Convenience class for YouTube Videos client
     */
    public static class ListVideos extends YouTubeClient<Video, YouTube.Videos.List, VideoListResponse, com.google.api.services.youtube.model.Video> {
        public ListVideos(@NotNull YouTubeClientState<Video, YouTube.Videos.List, VideoListResponse, com.google.api.services.youtube.model.Video> state) {
            super(state);
        }
    }

    private final YouTubeClientState<R, C, S, T> state;

    private int counter = 0;

    public YouTubeClient(@NotNull YouTubeClientState<R, C, S, T> state) {
        this.state = state;
    }

    public String getEtag() {
        return state.getEtag();
    }

    public String getEventId() {
        return state.getEventId();
    }

    public String getVisitorId() {
        return state.getVisitorId();
    }

    public boolean hasNext() {
        return state.hasNext();
    }

    @Override
    @NotNull
    public List<R> next() {

        if (!hasNext()) {
            LOG.info("next() called without checking 'hasNext()' first");
            return Collections.emptyList();
        }

        // prevent a single call from exhausting our quota
        if (counter++ >= MAX_COUNTER) {
            state.setFinished(true);
            return Collections.emptyList();
        }

        try {
            state.update();
            state.setFinished(StringUtils.isBlank(state.getNextPageToken()));
        } catch (GoogleJsonResponseException e) {
            final GoogleJsonError error = e.getDetails();
            state.setFailed(true);
            switch (error.getCode()) {
                case 400:
                    if ((error.getDetails() != null) && !error.getDetails().isEmpty() && "API_KEY_INVALID".equals(error.getDetails().get(0).getReason())) {
                        throw new YouTubeAuthenticationFailureException(error);
                    }
                    throw new YouTubeClientException(error);

                case 403:
                    if ((error.getErrors() != null) && !error.getErrors().isEmpty() && "youtube.quota".equals(error.getErrors().get(0).getDomain())) {
                        throw new YouTubeQuotaExceededException(error);
                    }
                    if ((error.getErrors() != null) && !error.getErrors().isEmpty() && "forbidden".equals(error.getErrors().get(0).getReason())) {
                        throw new YouTubeAccessForbiddenException(error);
                    }
                    throw new YouTubeAuthenticationFailureException(error);

                case 404:
                    // this is not consistently seen with unknown channel, playlist, video, etc.
                    // so it's more consistent to just set the error state and return.
                    LOG.info("Not found: {}", error.getMessage());
                    break;

                default:
                    throw new YouTubeClientException(error);
            }
        } catch (IOException e) {
            state.setFailed(true);
            throw new YouTubeIOException(e);
        }

        return state.next();
    }
}
