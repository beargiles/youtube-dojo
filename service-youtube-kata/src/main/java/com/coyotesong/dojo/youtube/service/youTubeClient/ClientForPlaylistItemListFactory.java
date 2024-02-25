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

import com.coyotesong.dojo.youtube.model.PlaylistItem;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * PlaylistItem list client factory
 */
@Component
@SuppressWarnings("unused")
public class ClientForPlaylistItemListFactory {
    private final YouTube.Builder ytBuilder;

    public ClientForPlaylistItemListFactory(@NotNull YouTube.Builder ytBuilder) {
        this.ytBuilder = ytBuilder;
    }

    @NotNull
    public Builder newBuilder() {
        return new Builder(ytBuilder);
    }

    public static class Builder {
        private static final List<String> PLAYLIST_ITEM_PARTS = Arrays.asList(
                Constants.Part.CONTENT_DETAILS.toString(),
                Constants.Part.ID.toString(),
                Constants.Part.SNIPPET.toString());

        private final YouTube.Builder ytBuilder;

        private List<String> ids = Collections.emptyList();
        private String playlistId;
        private String videoId;
        private String quotaUser;

        private Builder(@NotNull YouTube.Builder ytBuilder) {
            this.ytBuilder = ytBuilder;
        }

        public Builder withId(@NotNull String id) {
            this.ids = Collections.singletonList(id);
            return this;
        }

        public Builder withIds(@NotNull List<String> ids) {
            this.ids = ids;
            return this;
        }

        public Builder withPlaylistId(@NotNull String playlistId) {
            this.playlistId = playlistId;
            return this;
        }

        public Builder withVideoId(@NotNull String videoId) {
            this.videoId = videoId;
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
        public YouTubeClient.ListPlaylistItems build() throws IOException {
            final YouTube.PlaylistItems.List request = ytBuilder.build().playlistItems().list(PLAYLIST_ITEM_PARTS);

            if (!ids.isEmpty()) {
                request.setId(ids);
            } else if (isNotBlank(playlistId)) {
                request.setPlaylistId(playlistId);
            } else {
                throw new IllegalStateException("either 'id' or 'playlistId' must be specified");
            }

            // the server complains if this is the only thing we specify
            if (isNotBlank(videoId)) {
                request.setVideoId(videoId);
            }

            if (isNotBlank(quotaUser)) {
                request.setQuotaUser(quotaUser);
            }

            final YouTubeClientState<PlaylistItem, YouTube.PlaylistItems.List, PlaylistItemListResponse, com.google.api.services.youtube.model.PlaylistItem> state =
                    new YouTubeClientState<>(request, ClientForPlaylistItemListFactory::convert);

            return new YouTubeClient.ListPlaylistItems(state);
        }
    }

    /**
     * Convert YouTube object to ours.
     *
     * @param item
     * @return
     */
    @NotNull
    public static PlaylistItem convert(@NotNull com.google.api.services.youtube.model.PlaylistItem item) {
        final PlaylistItem value = new PlaylistItem();
        value.setId(item.getId());
        value.setEtag(item.getEtag());

        final PlaylistItemContentDetails details = item.getContentDetails();
        if (details != null) {
            value.setVideoId(details.getVideoId());
            value.setNote(details.getNote());
            // details.getStartAt();
            // details.getEndAt();
            // details.getVideoPublishedAt();
        }

        final PlaylistItemSnippet snippet = item.getSnippet();
        if (snippet != null) {
            value.setChannelId(snippet.getChannelId());
            value.setChannelTitle(snippet.getChannelTitle());
            value.setDescription(snippet.getDescription());
            value.setPlaylistId(snippet.getPlaylistId());
            value.setPosition(snippet.getPosition());
            value.setTitle(snippet.getTitle());
            value.setVideoOwnerChannelId(snippet.getVideoOwnerChannelId());
            // snippet.getVideoOwnerChannelTitle();

            if (snippet.getPublishedAt() != null) {
                value.setPublishedAt(Instant.ofEpochMilli(snippet.getPublishedAt().getValue()));
            }

            final ResourceId resourceId = snippet.getResourceId();
            if (resourceId != null) {
                // resourceId().getVideoId();
                // resourceId().getPlaylistId();
                // resourceId.getChannelId();
            }

            final ThumbnailDetails td = snippet.getThumbnails();
            if ((td != null) && !td.isEmpty() && !td.getDefault().isEmpty()) {
                value.setThumbnailUrl(td.getDefault().getUrl());
            }
        }

        // value.setLastChecked(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        return value;
    }
}
