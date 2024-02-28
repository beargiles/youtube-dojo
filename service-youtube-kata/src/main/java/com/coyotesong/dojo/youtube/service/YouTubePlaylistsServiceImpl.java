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
package com.coyotesong.dojo.youtube.service;

import com.coyotesong.dojo.youtube.model.Playlist;
import com.coyotesong.dojo.youtube.security.LogSanitizer;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForPlaylistListFactory;
import com.coyotesong.dojo.youtube.service.youTubeClient.YouTubeClient.ListPlaylists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

// see https://googleapis.dev/java/google-api-services-youtube/latest/com/google/api/services/youtube/YouTube.html

/**
 * Implementation of YouTubeService
 */
@Service("YouTubePlaylistsService")
@CacheConfig(cacheNames = {"playlists"})
public class YouTubePlaylistsServiceImpl implements YouTubePlaylistsService {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubePlaylistsServiceImpl.class);

    private final ClientForPlaylistListFactory clientForPlaylistListFactory;
    private final LogSanitizer sanitize;

    @Autowired
    public YouTubePlaylistsServiceImpl(@NotNull ClientForPlaylistListFactory clientForPlaylistListFactory,
                                       @NotNull LogSanitizer sanitize) {
        this.clientForPlaylistListFactory = clientForPlaylistListFactory;
        this.sanitize = sanitize;
    }

    /**
     * @param playlistId playlist to load
     * @return requested playlist, if available
     * @throws IOException error during REST call
     */
    @Override
    @Nullable
    public Playlist getPlaylist(@NotNull String playlistId) throws IOException {
        if (isBlank(playlistId)) {
            throw new IllegalArgumentException("'playlistId' must not be blank");
        }

        LOG.trace("getPlaylist('{}')...", sanitize.forPlaylistId(playlistId));
        final List<Playlist> playlists = getPlaylists(Collections.singletonList(playlistId));
        if (!playlists.isEmpty()) {
            final Playlist playlist = playlists.get(0);
            LOG.trace("getPlaylist('{}') -> '{}'", sanitize.forPlaylistId(playlistId), sanitize.forString(playlist.getTitle()));
            return playlist;
        }

        LOG.trace("getPlaylist('{}') -> null", sanitize.forPlaylistId(playlistId));
        return null;
    }

    /**
     * Get retrieve information about specified playlists
     *
     * @param ids - playlists to load
     * @return requested playlists (when available)
     */
    @Override
    @NotNull
    public List<Playlist> getPlaylists(@NotNull @Unmodifiable List<String> ids) throws IOException {
        if (ids == null) {
            throw new IllegalArgumentException("'ids' must be non-null");
        } else if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        // TODO: check for blank elements

        LOG.trace("getPlaylists()...");

        final List<Playlist> playlists = new ArrayList<>();

        // TODO - should handle this in underlying service...
        for (int offset = 0; offset < ids.size(); offset += 50) {
            final List<String> list = ids.subList(offset, Math.min(offset + 50, ids.size()));
            final ListPlaylists client = clientForPlaylistListFactory.newBuilder().withIds(list).build();
            while (client.hasNext()) {
                playlists.addAll(client.next());
            }
        }

        LOG.trace("getPlaylists()... {} record(s)", playlists.size());
        return playlists;
    }

    /**
     * Get retrieve information about specified playlists
     *
     * @param channelId - channel ids to load
     * @return requested playlists (when available)
     */
    @Override
    @NotNull
    public List<Playlist> getPlaylistsForChannelId(@NotNull String channelId) throws IOException {
        if (isBlank(channelId)) {
            throw new IllegalArgumentException("'channelId' must not be blank");
        }

        LOG.trace("getPlaylistsForChannelId('{}')...", sanitize.forChannelId(channelId));

        final List<Playlist> playlists = new ArrayList<>();

        final ListPlaylists client = clientForPlaylistListFactory.newBuilder().withChannelId(channelId).build();
        while (client.hasNext()) {
            playlists.addAll(client.next());
        }

        LOG.trace("getPlaylistsForChannelId('{}')... {} record(s)", sanitize.forChannelId(channelId), playlists.size());
        return playlists;
    }
}