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

package com.coyotesong.dojo.youtube.service.youtubeClient;

import com.coyotesong.dojo.youtube.form.VideoSearchForm;
import com.coyotesong.dojo.youtube.form.YouTubeSearchForm;
import com.coyotesong.dojo.youtube.model.SearchResult;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.google.api.services.youtube.model.ThumbnailDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Search list client factory
 */
@Component
@SuppressWarnings("unused")
public class ClientForSearchListFactory {
    private final YouTube.Builder ytBuilder;

    public ClientForSearchListFactory(YouTube.Builder ytBuilder) {
        this.ytBuilder = ytBuilder;
    }

    public Builder newBuilder() {
        return new Builder(ytBuilder);
    }

    public static class Builder {
        private static final List<String> SEARCH_PARTS = Arrays.asList("contentDetails", "contentOwnerDetails", "id", "snippet", "topicDetails");

        private final YouTube.Builder ytBuilder;

        private YouTubeSearchForm searchForm;

        private String quotaUser;

        private Builder(YouTube.Builder ytBuilder) {
            this.ytBuilder = ytBuilder;
        }

        public Builder withSearchForm(YouTubeSearchForm searchForm) {
            this.searchForm = searchForm;
            return this;
        }

        public Builder withQuotaUser(String quotaUser) {
            this.quotaUser = quotaUser;
            return this;
        }

        /**
         * Build new instance.
         *
         * @return
         * @throws IOException
         */
        public ClientForSearchList build() throws IOException {
            final YouTube.Search.List request = ytBuilder.build().search().list(SEARCH_PARTS);

            if (searchForm != null) {
                if (!searchForm.getTypes().isEmpty()) {
                    request.setType(searchForm.getTypes());
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
                if (isNotBlank(searchForm.getPublishedAfter())) {
                    request.setPublishedAfter(searchForm.getPublishedAfter());
                }
                if (isNotBlank(searchForm.getPublishedBefore())) {
                    request.setPublishedBefore(searchForm.getPublishedBefore());
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

                if (searchForm instanceof VideoSearchForm) {
                    // nothing additional... yet
                }
            }

            if (isNotBlank(quotaUser)) {
                request.setQuotaUser(quotaUser);
            }

            return new ClientForSearchList(request);
        }
    }

    public static class ClientForSearchList extends ClientForYouTubeRequest<SearchResult, SearchListResponse, com.google.api.services.youtube.model.SearchResult> {
        ClientForSearchList(YouTube.Search.List request) {
            super(new SearchState(request));
        }
    }

    public static class SearchState extends AbstractClientState<SearchResult, SearchListResponse, com.google.api.services.youtube.model.SearchResult> {
        private static final Logger LOG = LoggerFactory.getLogger(SearchState.class);
        private final YouTube.Search.List request;

        public SearchState(YouTube.Search.List request) {
            this.request = request;
        }

        /**
         * Perform YouTube API call.
         */
        public void update() throws IOException {
            request.setMaxResults(5L);  // FIXME...
            request.setPageToken(getNextPageToken());
            final SearchListResponse response = request.execute();

            setEtag(response.getEtag());
            setEventId(response.getEventId());
            setPageInfo(response.getPageInfo());
            setVisitorId(response.getVisitorId());

            // 'setNextPageToken()' is handled here in order to ensure that it's a null value
            // if there's an empty response.
            if (response.isEmpty() || (response.getItems() == null) || response.getItems().isEmpty()) {
                setItems(Collections.emptyList());
                setNextPageToken(null);
            } else {
                setItems(response.getItems().stream().map(this::convert).toList());
                setNextPageToken(response.getNextPageToken());
            }
        }

        /**
         * Convert YouTube API object to ours.
         *
         * @param result
         * @return
         */
        public SearchResult convert(com.google.api.services.youtube.model.SearchResult result) {
            final SearchResult value = new SearchResult();
            // value.setId(result.getId());
            value.setEtag(result.getEtag());

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
}
