/*
 * Copyright (c) 2023 Bear Giles <bgiles@coyotesong.com>.
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
package com.coyotesong.tabs.service.service;

import com.coyotesong.tabs.cache.PlaylistCache;
import com.coyotesong.tabs.model.Playlist;
import com.coyotesong.tabs.model.PlaylistItem;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

// see https://googleapis.dev/java/google-api-services-youtube/latest/com/google/api/services/youtube/YouTube.html

/**
 * Implementation of YouTubeService
 */
@Service
@CacheConfig(cacheNames = { "playlists" })
public class YouTubePlaylistsServiceImpl implements YouTubePlaylistsService {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubePlaylistsServiceImpl.class);

    private static final long MAX_RESULTS = 50L;

    private final YouTube.Builder builder;
    private final PlaylistCache playlistCache;

    @Autowired
    public YouTubePlaylistsServiceImpl(@NotNull PlaylistCache playlistCache, @NotNull YouTube.Builder builder) {
        this.playlistCache = playlistCache;
        this.builder = builder;
    }

    /**
     * @param playlistId playlist to load
     * @return
     * @throws IOException
     */
    @Override
    public Playlist getPlaylist(String playlistId) throws IOException {
        return null;
    }

    /**
     * Get retrieve information about specified playlists
     *
     * @param ids - playlists to load
     * @return requested playlists (when available)
     */
    public List<Playlist> getPlaylists(List<String> ids) throws IOException {
        final List<Playlist> playlists = new ArrayList<>();
        final List<String> expected = new ArrayList<>();
        final List<String> actual = new ArrayList<>();

        final YouTube yt = builder.build();
        final List<String> parts = Arrays.asList("contentDetails", "id", "player", "snippet", "status");

        for (int lower = 0, upper; lower < ids.size(); lower = upper) {
            // System.out.printf("%5d of %5d\n", lower / MAX_PLAYLISTS_RESULTS, (ids.size() + MAX_PLAYLISTS_RESULTS - 1) / MAX_PLAYLISTS_RESULTS);

            upper = Math.min(ids.size(), (int) (lower + MAX_RESULTS));
            expected.clear();
            expected.addAll(ids.subList(lower, upper));

            final PlaylistListResponse response = yt.playlists()
                    .list(parts)
                    .setId(expected)
                    .setMaxResults(MAX_RESULTS)
                    .execute();

            LOG.info("results: {}", response.getPageInfo().getTotalResults());
            if (response.isEmpty() || (response.getItems() == null)) {
                return Collections.emptyList();
            }

            response.getItems().stream().map(Playlist::new).forEach(playlists::add);

            // did we get everything requested?
            // check(lower, upper, playlists.size(), response.getItems().size(), ids, expected);

            final String token = response.getNextPageToken();
            if (isNotBlank(token)) {
                LOG.info("token!! '{}'", token);
            }
        }

        return playlists;
    }

    /**
     * Get retrieve information about specified playlists
     *
     * @param channelId - channel ids to load
     * @return requested playlists (when available)
     */
    public List<Playlist> getPlaylistsForChannelId(String channelId) throws IOException {
        final List<Playlist> playlists = new ArrayList<>();

        final YouTube yt = builder.build();
        final List<String> parts = Arrays.asList("contentDetails", "id", "player", "snippet", "status");

        final PlaylistListResponse response = yt.playlists()
                .list(parts)
                .setChannelId(channelId)
                .setMaxResults(MAX_RESULTS)
                .execute();

        LOG.info("results: {}", response.getPageInfo().getTotalResults());
        if (response.isEmpty() || (response.getItems() == null)) {
            return Collections.emptyList();
        }

        response.getItems().stream().map(Playlist::new).forEach(playlists::add);

        final String token = response.getNextPageToken();
        if (isNotBlank(token)) {
            LOG.info("token!! '{}'", token);
        }

        return playlists;
    }

    /**
     * Get retrieve information about specified playlist items
     *
     * @param ids - playlists to load
     * @return requested playlists items (when available)
     */
    public List<PlaylistItem> getPlaylistItems(List<String> ids) throws IOException {
        final List<PlaylistItem> items = new ArrayList<>();

        final YouTube yt = builder.build();
        // skip 'status'
        final List<String> parts = Arrays.asList("contentDetails", "id", "snippet");

        for (String id : ids) {
            final YouTube.PlaylistItems.List request = yt.playlistItems()
                    .list(parts)
                    .setPlaylistId(id)
                    .setMaxResults(MAX_RESULTS);

            String token = null;
            do {
                if (token != null) {
                    request.setPageToken(token);
                }

                final PlaylistItemListResponse response = request.execute();
                if (response.isEmpty() || (response.getItems() == null)) {
                    return Collections.emptyList();
                }

                response.getItems().stream().map(PlaylistItem::new).forEach(items::add);

                token = response.getNextPageToken();
            } while (token != null);
        }

        return items;
    }
}