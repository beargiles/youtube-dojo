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

import com.coyotesong.dojo.youtube.model.PlaylistItem;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
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

    public ClientForPlaylistItemListFactory(YouTube.Builder ytBuilder) {
        this.ytBuilder = ytBuilder;
    }

    public Builder newBuilder() {
        return new Builder(ytBuilder);
    }

    public static class Builder {
        private static final List<String> PLAYLIST_ITEM_PARTS = Arrays.asList("contentDetails", "id", "snippet", "status");

        private final YouTube.Builder ytBuilder;

        private List<String> ids = Collections.emptyList();
        private String playlistId;
        private String videoId;
        private String quotaUser;

        private Builder(YouTube.Builder ytBuilder) {
            this.ytBuilder = ytBuilder;
        }

        public Builder withId(String id) {
            this.ids = Collections.singletonList(id);
            return this;
        }

        public Builder withIds(List<String> ids) {
            this.ids = ids;
            return this;
        }

        public Builder withPlaylistId(String playlistId) {
            this.playlistId = playlistId;
            return this;
        }

        public Builder withVideoId(String videoId) {
            this.videoId = videoId;
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
        public ClientForPlaylistItemList build() throws IOException {
            final YouTube.PlaylistItems.List request = ytBuilder.build().playlistItems().list(PLAYLIST_ITEM_PARTS);

            if (!ids.isEmpty()) {
                request.setId(ids);
            } else if (isNotBlank(playlistId)) {
                request.setPlaylistId(playlistId);
            } else {
                throw new IllegalStateException("either 'id', 'playlistId' must be specified");
            }

            // the server complains if this is the only thing we specify
            if (isNotBlank(videoId)) {
                request.setVideoId(videoId);
            }

            if (isNotBlank(quotaUser)) {
                request.setQuotaUser(quotaUser);
            }

            return new ClientForPlaylistItemList(request);
        }
    }

    public static class ClientForPlaylistItemList extends ClientForYouTubeRequest<PlaylistItem, PlaylistItemListResponse, com.google.api.services.youtube.model.PlaylistItem> {
        public ClientForPlaylistItemList(YouTube.PlaylistItems.List request) {
            super(new PlaylistItemState(request));
        }
    }


    public static class PlaylistItemState extends AbstractClientState<PlaylistItem, PlaylistItemListResponse, com.google.api.services.youtube.model.PlaylistItem> {
        private final YouTube.PlaylistItems.List request;

        public PlaylistItemState(YouTube.PlaylistItems.List request) {
            this.request = request;
        }

        /**
         * Perform YouTube API call
         */
        @Override
        public void update() throws IOException {
            request.setPageToken(getNextPageToken());
            final PlaylistItemListResponse response = request.execute();

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
         * Convert YouTube object to ours.
         *
         * @param item
         * @return
         */
        public PlaylistItem convert(com.google.api.services.youtube.model.PlaylistItem item) {
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

            return value;
        }
    }
}
