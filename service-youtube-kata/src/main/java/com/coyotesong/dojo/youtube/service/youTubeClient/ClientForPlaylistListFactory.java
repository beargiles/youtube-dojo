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

import com.coyotesong.dojo.youtube.model.Playlist;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.google.api.services.youtube.model.PlaylistPlayer;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.google.api.services.youtube.model.ThumbnailDetails;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    public ClientForPlaylistListFactory(@NotNull YouTube.Builder ytBuilder) {
        this.ytBuilder = ytBuilder;
    }

    @NotNull
    public Builder newBuilder() {
        return new Builder(ytBuilder);
    }

    public static class Builder {
        private static final List<String> PLAYLIST_PARTS = Arrays.asList(
                Constants.Part.CONTENT_DETAILS.toString(),
                Constants.Part.ID.toString(),
                Constants.Part.PLAYER.toString(),
                Constants.Part.SNIPPET.toString(),
                Constants.Part.STATUS.toString());

        private final YouTube.Builder ytBuilder;

        private List<String> ids = Collections.emptyList();
        private String channelId;
        private String hl;
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

        public Builder withChannelId(@NotNull String channelId) {
            this.channelId = channelId;
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
        public YouTubeClient.ListPlaylists build() throws IOException {
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

            final YouTubeClientState<Playlist, YouTube.Playlists.List, PlaylistListResponse, com.google.api.services.youtube.model.Playlist> state =
                    new YouTubeClientState<>(request, ClientForPlaylistListFactory::convert);

            return new YouTubeClient.ListPlaylists(state);
        }
    }

    /**
     * Convert YouTube object to ours.
     *
     * @param playlist
     * @return
     */
    @NotNull
    public static Playlist convert(@NotNull com.google.api.services.youtube.model.Playlist playlist) {
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

        value.setLastChecked(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        return value;
    }
}
