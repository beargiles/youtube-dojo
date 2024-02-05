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
package com.coyotesong.dojo.youtube.service;

import com.coyotesong.tabs.model.Playlist;
import com.coyotesong.tabs.model.PlaylistItem;

import java.io.IOException;
import java.util.List;

/**
 * YouTube API client for unauthenticated users.
 */
@SuppressWarnings("unused")
public interface YouTubePlaylistsService {

    /**
     * Retrieve information about specified playlist
     * @param playlistId playlist to load
     * @return requested playlist (when available)
     */
    Playlist getPlaylist(String playlistId) throws IOException;

    /**
     * Retrieve information about specified playlists
     * @param playlistIds playlists to load
     * @return requested playlists (when available)
     */
    List<Playlist> getPlaylists(List<String> playlistIds) throws IOException;

    /**
     * Retrieve information about specified playlists
     * @param channelId channel id
     * @return requested playlists (when available)
     */
    List<Playlist> getPlaylistsForChannelId(String channelId) throws IOException;

    /**
     * Retrieve information about specified playlist items
     * @param ids playlist (not playlist item) to load
     * @return requested playlist items (when available)
     */
    List<PlaylistItem> getPlaylistItems(List<String> ids) throws IOException;
}
