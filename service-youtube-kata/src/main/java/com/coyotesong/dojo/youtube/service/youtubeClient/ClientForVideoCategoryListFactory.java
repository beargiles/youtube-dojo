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

import com.coyotesong.dojo.youtube.model.VideoCategory;
import com.coyotesong.dojo.youtube.security.LogSanitizer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoCategoryListResponse;
import com.google.api.services.youtube.model.VideoCategorySnippet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Video list client factory
 */
@Component
@SuppressWarnings("unused")
public class ClientForVideoCategoryListFactory {
    private final YouTube.Builder ytBuilder;
    private final LogSanitizer sanitize;

    public ClientForVideoCategoryListFactory(YouTube.Builder ytBuilder, LogSanitizer sanitize) {
        this.ytBuilder = ytBuilder;
        this.sanitize = sanitize;
    }

    public Builder newBuilder() {
        return new Builder(ytBuilder, sanitize);
    }

    public static class Builder {
        private static final List<String> VIDEO_PARTS = Arrays.asList("id", "snippet");

        private final YouTube.Builder ytBuilder;
        private final LogSanitizer sanitize;

        private List<String> ids = Collections.emptyList();
        private String hl;
        private String quotaUser;

        private Builder(YouTube.Builder ytBuilder, LogSanitizer sanitize) {
            this.ytBuilder = ytBuilder;
            this.sanitize = sanitize;
        }

        public Builder withId(String id) {
            this.ids = Collections.singletonList(id);
            return this;
        }

        public Builder withIds(List<String> ids) {
            this.ids = ids;
            return this;
        }

        public Builder withHl(String hl) {
            this.hl = hl;
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
        public ClientForVideoCategoryList build() throws IOException {
            final YouTube.VideoCategories.List request = ytBuilder.build().videoCategories().list(VIDEO_PARTS);

            if (isBlank(hl)) {
                throw new IllegalStateException("'hl' must be specified");
            }

            if (!ids.isEmpty()) {
                request.setId(ids);
            }

            request.setHl(hl);

            if (isNotBlank(quotaUser)) {
                request.setQuotaUser(quotaUser);
            }

            return new ClientForVideoCategoryList(request, sanitize);
        }
    }

    public static class ClientForVideoCategoryList extends ClientForYouTubeRequest<VideoCategory, VideoCategoryListResponse, com.google.api.services.youtube.model.VideoCategory> {
        public ClientForVideoCategoryList(YouTube.VideoCategories.List request, LogSanitizer sanitize) {
            super(new VideoCategoryState(request, sanitize));
        }
    }

    public static class VideoCategoryState extends AbstractClientState<VideoCategory, VideoCategoryListResponse, com.google.api.services.youtube.model.VideoCategory> {
        private final static Logger LOG = LoggerFactory.getLogger(VideoCategoryState.class);
        private final static YTUtils utils = new YTUtils();

        private final YouTube.VideoCategories.List request;
        private final LogSanitizer sanitize;

        public VideoCategoryState(YouTube.VideoCategories.List request, LogSanitizer sanitize) {
            this.request = request;
            this.sanitize = sanitize;
        }

        /**
         * Perform YouTube API call
         */
        @Override
        public void update() throws IOException {
            // request.setPageToken(getNextPageToken());
            final VideoCategoryListResponse response = request.execute();

            setEtag(response.getEtag());
            setEventId(response.getEventId());
            setNextPageToken(response.getNextPageToken());
            setPageInfo(response.getPageInfo());
            setVisitorId(response.getVisitorId());

            if (response.isEmpty() || (response.getItems() == null) || response.getItems().isEmpty()) {
                setItems(Collections.emptyList());
            } else {
                setItems(response.getItems().stream().map(this::convert).toList());
            }
        }

        /**
         * Convert YouTube API object to ours.
         *
         * @param category
         * @return
         */
        public VideoCategory convert(com.google.api.services.youtube.model.VideoCategory category) {
            final VideoCategory value = new VideoCategory();

            value.setId(category.getId());
            value.setEtag(category.getEtag());

            if (category.getSnippet() != null) {
                final VideoCategorySnippet snippet = category.getSnippet();
                // this will always(?) be 'UCBR8-60-B28hp2BmDPdntcQ'
                value.setChannelId(snippet.getChannelId());
                value.setTitle(snippet.getTitle());
                value.setAssignable(snippet.getAssignable());
            }

            return value;
        }
    }
}
