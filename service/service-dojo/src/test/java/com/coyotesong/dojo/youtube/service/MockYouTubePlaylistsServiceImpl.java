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
import com.coyotesong.dojo.youtube.testdata.TestPlaylists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.boot.test.context.TestComponent;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Mock implementation of YouTubePlaylistsService
 */
@TestComponent
public class MockYouTubePlaylistsServiceImpl implements YouTubePlaylistsService {
    @Override
    public @Nullable Playlist getPlaylist(@NotNull String id) throws IOException {
        if (TestPlaylists.PLAYLIST.getPlaylistId().equals(id)) {
            return TestPlaylists.PLAYLIST.getPlaylist();
        } else if (TestPlaylists.MISSING_PLAYLIST.getPlaylistId().equals(id)) {
            return TestPlaylists.MISSING_PLAYLIST.getPlaylist();
        }

        fail("unexpected videoId: " + id);
        return null;
    }

    @Override
    public @NotNull List<Playlist> getPlaylists(@NotNull @Unmodifiable List<String> ids) throws IOException {
        return Collections.emptyList();
    }

    /**
     * Retrieve information about specified playlists
     *
     * @param channelId channel id
     * @return requested playlists (when available)
     */
    @Override
    public @NotNull List<Playlist> getPlaylistsForChannelId(@NotNull String channelId) throws IOException {
        return Collections.emptyList();
    }
}
