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
import com.coyotesong.dojo.youtube.form.VideoSearchForm;
import com.coyotesong.dojo.youtube.form.YouTubeSearchForm;
import com.coyotesong.dojo.youtube.model.SearchResult;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.google.api.services.youtube.model.ThumbnailDetails;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Search list client factory
 */
@Component
@SuppressWarnings("unused")
public class ClientForSearchListFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ClientForSearchListFactory.class);
    private static final long MAX_RESPONSES = 100L;

    private final YouTube.Builder ytBuilder;
    private final YouTubeApiCacheService cache;

    public ClientForSearchListFactory(@NotNull YouTube.Builder ytBuilder, @NotNull YouTubeApiCacheService cache) {
        this.ytBuilder = ytBuilder;
        this.cache = cache;
    }

    @NotNull
    public Builder newBuilder() {
        return new Builder(ytBuilder, cache);
    }

    public static class Builder {
        private static final List<String> SEARCH_PARTS = Arrays.asList(Constants.Part.ID.toString(), Constants.Part.SNIPPET.toString());

        private final YouTube.Builder ytBuilder;
        private final YouTubeApiCacheService cache;

        private YouTubeSearchForm searchForm;

        private String quotaUser;

        private Builder(@NotNull YouTube.Builder ytBuilder, @NotNull YouTubeApiCacheService cache) {
            this.ytBuilder = ytBuilder;
            this.cache = cache;
        }

        public Builder withSearchForm(@NotNull YouTubeSearchForm searchForm) {
            this.searchForm = searchForm;
            return this;
        }

        public Builder withQuotaUser(@NotNull String quotaUser) {
            this.quotaUser = quotaUser;
            return this;
        }

        /**
         * Build new instance.
         *
         * @return
         * @throws IOException
         */
        @NotNull
        public YouTubeClient.ListSearchResults build() throws IOException {
            final YouTube.Search.List request = ytBuilder.build().search().list(SEARCH_PARTS);

            if (searchForm != null) {
                if (isNotBlank(searchForm.getType())) {
                    request.setType(Collections.singletonList(searchForm.getType()));
                }

                if (isNotBlank(searchForm.getQuery())) {
                    request.setQ(searchForm.getQuery());
                }

                if (isNotBlank(searchForm.getChannelId())) {
                    request.setChannelId(searchForm.getChannelId());
                }
                if (isNotBlank(searchForm.getChannelType())) {
                    request.setChannelType(searchForm.getChannelType());
                }
                if (isNotBlank(searchForm.getEventType())) {
                    request.setEventType(searchForm.getEventType());
                }
                if (isNotBlank(searchForm.getOrder())) {
                    request.setOrder(searchForm.getOrder());
                }
                if (searchForm.getPublishedAfter() != null) {
                    request.setPublishedAfter(ISO_INSTANT.format(searchForm.getPublishedAfter()));
                }
                if (searchForm.getPublishedBefore() != null) {
                    request.setPublishedBefore(ISO_INSTANT.format(searchForm.getPublishedBefore()));
                }
                if (isNotBlank(searchForm.getRegionCode())) {
                    request.setRegionCode(searchForm.getRegionCode());
                }
                if (isNotBlank(searchForm.getSafeSearch())) {
                    request.setSafeSearch(searchForm.getSafeSearch());
                }
                if (isNotBlank(searchForm.getTopicId())) {
                    request.setTopicId(searchForm.getTopicId());
                }

                if (searchForm instanceof VideoSearchForm videoSearchForm) {
                    if (isNotBlank(videoSearchForm.getVideoType())) {
                        request.setVideoType(videoSearchForm.getVideoType());
                    }
                    // nothing additional... yet
                }

                if (searchForm.getMaxResults() != null) {
                    request.setMaxResults((searchForm.getMaxResults().longValue()));
                }
            }

            if ((request.getMaxResults() != null) && (request.getMaxResults() > MAX_RESPONSES)) {
                request.setMaxResults(MAX_RESPONSES);
            }

            if (isNotBlank(quotaUser)) {
                request.setQuotaUser(quotaUser);
            }

            final YouTubeClientState<SearchResult, YouTube.Search.List, SearchListResponse, com.google.api.services.youtube.model.SearchResult> state =
                    new YouTubeClientState<>(request, ClientForSearchListFactory::convert, cache);

            return new YouTubeClient.ListSearchResults(state);
        }
    }

    /**
     * Convert YouTube API object to ours.
     *
     * @param result
     * @return
     */
    @NotNull
    public static SearchResult convert(@NotNull com.google.api.services.youtube.model.SearchResult result) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("{}", result.toString());
        }

        final SearchResult value = new SearchResult();
        // value.setId(result.getId());
        value.setEtag(result.getEtag());

        final ResourceId id = result.getId();
        if (id != null) {
            value.setChannelId(id.getChannelId());
            value.setPlaylistId(id.getPlaylistId());
            value.setVideoId(id.getVideoId());
        }

        if (result.getSnippet() != null) {
            final SearchResultSnippet snippet = result.getSnippet();

            value.setChannelId(snippet.getChannelId());
            value.setChannelTitle(snippet.getChannelTitle());
            value.setDescription(snippet.getDescription());
            value.setLiveBroadcastContent(snippet.getLiveBroadcastContent());
            value.setTitle(snippet.getTitle());

            if (snippet.getPublishedAt() != null) {
                value.setPublishedAt(Instant.ofEpochMilli(snippet.getPublishedAt().getValue()));
            }

            final ThumbnailDetails td = snippet.getThumbnails();
            if ((td != null) && !td.isEmpty() && !td.getDefault().isEmpty()) {
                value.setTnUrl(td.getDefault().getUrl());
            }
        }

        value.setLastChecked(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        return value;
    }
}
