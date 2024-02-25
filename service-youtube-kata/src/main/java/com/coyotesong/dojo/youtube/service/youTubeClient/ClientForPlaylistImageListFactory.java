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

import com.coyotesong.dojo.youtube.model.PlaylistImage;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistImageListResponse;
import com.google.api.services.youtube.model.PlaylistImageSnippet;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * PlaylistImage list client factory
 */
@Component
@SuppressWarnings("unused")
public class ClientForPlaylistImageListFactory {
    private final YouTube.Builder ytBuilder;

    public ClientForPlaylistImageListFactory(@NotNull YouTube.Builder ytBuilder) {
        this.ytBuilder = ytBuilder;
    }

    @NotNull
    public Builder newBuilder() {
        return new Builder(ytBuilder);
    }

    public static class Builder {
        private final YouTube.Builder ytBuilder;

        private String playlistId;
        private String quotaUser;

        private Builder(@NotNull YouTube.Builder ytBuilder) {
            this.ytBuilder = ytBuilder;
        }

        public Builder withPlaylistId(@NotNull String playlistId) {
            this.playlistId = playlistId;
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
        public YouTubeClient.ListPlaylistImages build() throws IOException {
            final YouTube.PlaylistImages.List request = ytBuilder.build().playlistImages().list();

            if (isNotBlank(playlistId)) {
                request.setParent(playlistId);
            } else {
                throw new IllegalStateException("'playlistId' must be specified");
            }

            if (isNotBlank(quotaUser)) {
                request.setQuotaUser(quotaUser);
            }

            final YouTubeClientState<PlaylistImage, YouTube.PlaylistImages.List, PlaylistImageListResponse, com.google.api.services.youtube.model.PlaylistImage> state =
                    new YouTubeClientState<>(request, ClientForPlaylistImageListFactory::convert);

            return new YouTubeClient.ListPlaylistImages(state);
        }
    }

    /**
     * Convert YouTube object to ours.
     *
     * @param image
     * @return
     */
    @NotNull
    public static PlaylistImage convert(@NotNull com.google.api.services.youtube.model.PlaylistImage image) {
        final PlaylistImage value = new PlaylistImage();
        value.setId(image.getId());
        // value.setEtag(image.getEtag());

        final PlaylistImageSnippet snippet = image.getSnippet();
        if (snippet != null) {
            value.setHeight(snippet.getHeight());
            value.setPlaylistId(snippet.getPlaylistId());
            value.setType(snippet.getType());
            value.setWidth(snippet.getWidth());
        }

        // value.setLastChecked(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        return value;
    }
}
