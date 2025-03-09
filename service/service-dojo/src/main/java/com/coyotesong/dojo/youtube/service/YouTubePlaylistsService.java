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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.util.List;

/**
 * YouTube 'playlists' API client
 *
 * Other options:
 * - HL
 * - Mine
 *
 * The 'OnBehalfOfContentOwner' is intended for use by YouTube content partners.
 *
 * See [YouTube.Playlists.List](https://googleapis.dev/java/google-api-services-youtube/latest/index.html?com/google/api/services/youtube/YouTube.Playlists.List.html)
 */
@SuppressWarnings("unused")
public interface YouTubePlaylistsService {

    /**
     * Retrieve information about specified playlist
     *
     * @param playlistId playlist to load
     * @return requested playlist (when available)
     */
    @Nullable
    Playlist getPlaylist(@NotNull String playlistId) throws IOException;

    /**
     * Retrieve information about specified playlists
     *
     * @param playlistIds playlists to load
     * @return requested playlists (when available)
     */
    @NotNull
    List<Playlist> getPlaylists(@NotNull @Unmodifiable List<String> playlistIds) throws IOException;

    /**
     * Retrieve information about specified playlists
     *
     * @param channelId channel id
     * @return requested playlists (when available)
     */
    @NotNull
    List<Playlist> getPlaylistsForChannelId(@NotNull String channelId) throws IOException;
}
