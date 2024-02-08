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

import com.coyotesong.dojo.youtube.model.Playlist;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.google.api.services.youtube.model.PlaylistPlayer;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.google.api.services.youtube.model.ThumbnailDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Playlist list client factory
 */
@Component
@SuppressWarnings("unused")
public class ClientForPlaylistListFactory {
    private final YouTube.Builder ytBuilder;

    public ClientForPlaylistListFactory(YouTube.Builder ytBuilder) {
        this.ytBuilder = ytBuilder;
    }

    public Builder newBuilder() {
        return new Builder(ytBuilder);
    }

    public static class Builder {
        private static final List<String> PLAYLIST_PARTS = Arrays.asList("contentDetails", "id", "player", "snippet", "status");

        private final YouTube.Builder ytBuilder;

        private List<String> ids = Collections.emptyList();
        private String channelId;
        private String hl;
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

        public Builder withChannelId(String channelId) {
            this.channelId = channelId;
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
        public ClientForPlaylistList build() throws IOException {
            final YouTube.Playlists.List request = ytBuilder.build().playlists().list(PLAYLIST_PARTS);

            if (!ids.isEmpty()) {
                request.setId(ids);
            } else if (isNotBlank(channelId)) {
                request.setChannelId(channelId);
            } else {
                throw new IllegalStateException("either 'id' or 'channelId' must be specified");
            }

            if (isNotBlank(hl)) {
                request.setHl(hl);
            }

            if (isNotBlank(quotaUser)) {
                request.setQuotaUser(quotaUser);
            }

            return new ClientForPlaylistList(request);
        }
    }

    public static class ClientForPlaylistList extends ClientForYouTubeRequest<Playlist, PlaylistListResponse, com.google.api.services.youtube.model.Playlist> {
        public ClientForPlaylistList(YouTube.Playlists.List request) {
            super(new PlaylistState(request));
        }
    }


    public static class PlaylistState extends AbstractClientState<Playlist, PlaylistListResponse, com.google.api.services.youtube.model.Playlist> {
        private final YouTube.Playlists.List request;

        public PlaylistState(YouTube.Playlists.List request) {
            this.request = request;
        }

        /**
         * Perform YouTube API call
         */
        @Override
        public void update() throws IOException {
            request.setPageToken(getNextPageToken());
            final PlaylistListResponse response = request.execute();

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
         * @param playlist
         * @return
         */
        public Playlist convert(com.google.api.services.youtube.model.Playlist playlist) {
            final Playlist value = new Playlist();
            value.setId(playlist.getId());
            value.setEtag(playlist.getEtag());

            final PlaylistSnippet snippet = playlist.getSnippet();
            if (snippet != null) {
                if (snippet.getPublishedAt() != null) {
                    value.setPublishedAt(Instant.ofEpochMilli(snippet.getPublishedAt().getValue()));
                }

                value.setChannelId(snippet.getChannelId());
                value.setTitle(snippet.getTitle());
                value.setDescription(snippet.getDescription());
                value.setChannelTitle(snippet.getChannelTitle());
                value.setLang(snippet.getDefaultLanguage());

                value.setTnVideoId(snippet.getThumbnailVideoId());

                if (snippet.getTags() != null) {
                    value.setTags("\"" + String.join("\", \"", snippet.getTags()) + "\"");
                }

                final ThumbnailDetails td = snippet.getThumbnails();
                if ((td != null) && !td.isEmpty() && !td.getDefault().isEmpty()) {
                    value.setTnUrl(td.getDefault().getUrl());
                }
            }

            final PlaylistPlayer player = playlist.getPlayer();
            if (player != null) {
                value.setEmbedHtml(player.getEmbedHtml());
            }

            return value;
        }
    }
}
