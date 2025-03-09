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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.util.List;

/**
 * YouTube 'playlistItems' API client
 * <p>
 * see <a href="https://googleapis.dev/java/google-api-services-youtube/latest/index.html?com/google/api/services/youtube/YouTube.PlaylistItems.List.html">YouTube.PlaylistItems.List</a>
 */
@SuppressWarnings("unused")
public interface YouTubePlaylistItemsService {

    /**
     * Retrieve information about specified playlist item
     *
     * @param playlistItemId playlist item to load
     * @return requested playlist item (when available)
     */
    @Nullable
    PlaylistItem getPlaylistItem(@NotNull String playlistItemId) throws IOException;

    /**
     * Retrieve information about specified playlist items
     *
     * @param playlistItemIds playlist items to load
     * @return requested playlist items (when available)
     */
    @NotNull
    List<PlaylistItem> getPlaylistItems(@NotNull @Unmodifiable List<String> playlistItemIds) throws IOException;

    /**
     * Retrieve information about specified playlist items
     *
     * @param playlistId playlist id
     * @return requested playlist items (when available)
     */
    @NotNull
    List<PlaylistItem> getPlaylistItemsForPlaylistId(@NotNull String playlistId) throws IOException;
}
