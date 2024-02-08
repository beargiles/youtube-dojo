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

import com.coyotesong.dojo.youtube.model.PlaylistItem;
import com.coyotesong.dojo.youtube.security.LogSanitizer;
import com.coyotesong.dojo.youtube.service.youtubeClient.ClientForPlaylistItemListFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// see https://googleapis.dev/java/google-api-services-youtube/latest/com/google/api/services/youtube/YouTube.html

/**
 * Implementation of YouTubeService
 */
@Service
public class YouTubePlaylistItemsServiceImpl implements YouTubePlaylistItemsService {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubePlaylistItemsServiceImpl.class);

    private final ClientForPlaylistItemListFactory clientForPlaylistItemListFactory;
    private final LogSanitizer sanitize;

    @Autowired
    public YouTubePlaylistItemsServiceImpl(@NotNull ClientForPlaylistItemListFactory clientForPlaylistItemListFactory,
                                           @NotNull LogSanitizer sanitize) {
        this.clientForPlaylistItemListFactory = clientForPlaylistItemListFactory;
        this.sanitize = sanitize;
    }

    /**
     * @param id playlistItem to load
     * @return
     * @throws IOException
     */
    @Override
    @Nullable
    public PlaylistItem getPlaylistItem(@NotNull String id) throws IOException {
        LOG.trace("getPlaylistItem('{}')...", sanitize.forPlaylistItemId(id));
        final List<PlaylistItem> items = getPlaylistItems(Collections.singletonList(id));
        if (!items.isEmpty()) {
            final PlaylistItem item = items.get(0);
            LOG.trace("getPlaylistItem('{}') -> '{}'", sanitize.forPlaylistItemId(id), sanitize.forString(item.getTitle()));
            return item;
        }

        LOG.trace("getPlaylistItem('{}') -> null", sanitize.forPlaylistItemId(id));
        return null;
    }

    /**
     * Get retrieve information about specified playlist items
     *
     * @param ids - playlist items to load
     * @return requested playlist items (when available)
     */
    @Override
    @NotNull
    public List<PlaylistItem> getPlaylistItems(@NotNull @Unmodifiable List<String> ids) throws IOException {
        LOG.trace("call to getPlaylistItems()...");

        final List<PlaylistItem> items = new ArrayList<>();

        // TODO - should handle this in underlying service...
        for (int offset = 0; offset < ids.size(); offset += 50) {
            final List<String> list = ids.subList(offset, Math.min(offset + 50, ids.size()));
            final ClientForPlaylistItemListFactory.ClientForPlaylistItemList client = clientForPlaylistItemListFactory.newBuilder().withIds(list).build();
            while (client.hasNext()) {
                items.addAll(client.next());
            }
        }

        LOG.trace("getPlaylistItems()... {} record(s)", items.size());
        return items;
    }

    /**
     * Get retrieve information about specified playlist items
     *
     * @param playlistId - playlist id to load
     * @return requested playlist items (when available)
     */
    @Override
    @NotNull
    public List<PlaylistItem> getPlaylistItemsForPlaylistId(@NotNull String playlistId) throws IOException {
        LOG.trace("getPlaylistItemsForPlaylistId('{}')...", sanitize.forPlaylistId(playlistId));
        final List<PlaylistItem> items = new ArrayList<>();

        final ClientForPlaylistItemListFactory.ClientForPlaylistItemList client = clientForPlaylistItemListFactory.newBuilder().withPlaylistId(playlistId).build();
        while (client.hasNext()) {
            items.addAll(client.next());
        }

        LOG.trace("getPlaylistItemsForPlaylistId('{}')... {} record(s)", sanitize.forPlaylistId(playlistId), items.size());
        return items;
    }
}