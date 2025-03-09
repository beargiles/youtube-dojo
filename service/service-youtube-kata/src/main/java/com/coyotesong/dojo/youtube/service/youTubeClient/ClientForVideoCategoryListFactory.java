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
import com.coyotesong.dojo.youtube.model.VideoCategory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoCategoryListResponse;
import com.google.api.services.youtube.model.VideoCategorySnippet;
import org.jetbrains.annotations.NotNull;
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
    private final YouTubeApiCacheService cache;

    public ClientForVideoCategoryListFactory(@NotNull YouTube.Builder ytBuilder, @NotNull YouTubeApiCacheService cache) {
        this.ytBuilder = ytBuilder;
        this.cache = cache;
    }

    @NotNull
    public Builder newBuilder() {
        return new Builder(ytBuilder, cache);
    }

    public static class Builder {
        private static final List<String> VIDEO_CATEGORIES_PARTS = Arrays.asList(
                Constants.Part.ID.toString(), Constants.Part.SNIPPET.toString());

        private final YouTube.Builder ytBuilder;
        private final YouTubeApiCacheService cache;

        private List<String> ids = Collections.emptyList();
        private String hl;
        private String quotaUser;

        private Builder(@NotNull YouTube.Builder ytBuilder, @NotNull YouTubeApiCacheService cache) {
            this.ytBuilder = ytBuilder;
            this.cache = cache;
        }

        public Builder withId(@NotNull String id) {
            this.ids = Collections.singletonList(id);
            return this;
        }

        public Builder withIds(@NotNull List<String> ids) {
            this.ids = ids;
            return this;
        }

        public Builder withHl(@NotNull String hl) {
            this.hl = hl;
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
        public YouTubeClient.ListVideoCategories build() throws IOException {
            final YouTube.VideoCategories.List request = ytBuilder.build().videoCategories().list(VIDEO_CATEGORIES_PARTS);

            if (isBlank(hl)) {
                throw new IllegalArgumentException("'hl' must be specified");
            }

            if (!ids.isEmpty()) {
                request.setId(ids);
            }

            request.setHl(hl);

            if (isNotBlank(quotaUser)) {
                request.setQuotaUser(quotaUser);
            }

            final YouTubeClientState<VideoCategory, YouTube.VideoCategories.List, VideoCategoryListResponse, com.google.api.services.youtube.model.VideoCategory> state =
                    new YouTubeClientState<>(request, ClientForVideoCategoryListFactory::convert, cache);

            return new YouTubeClient.ListVideoCategories(state);
        }
    }

    /**
     * Convert YouTube API object to ours.
     *
     * @param category
     * @return
     */
    @NotNull
    public static VideoCategory convert(@NotNull com.google.api.services.youtube.model.VideoCategory category) {
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

        // value.setLastChecked(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        return value;
    }
}
