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

import com.coyotesong.dojo.youtube.config.YouTubeContext;
import com.coyotesong.dojo.youtube.model.Playlist;
import com.coyotesong.dojo.youtube.security.LogSanitizerImpl;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForChannelListFactory;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForPlaylistListFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.coyotesong.dojo.youtube.service.TestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test YouTubePlaylistsService
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                LogSanitizerImpl.class,
                YouTubeContext.class,
                ClientForChannelListFactory.class,
                ClientForPlaylistListFactory.class,
                YouTubeChannelsServiceImpl.class,
                YouTubePlaylistsServiceImpl.class
        })
@DirtiesContext
public class YouTubePlaylistListServiceITest extends AbstractYouTubeServiceITest {

    @Value("${quotaExceeded}")
    private Boolean quotaExceeded;

    private final YouTubePlaylistsService playlistsService;

    @Autowired
    private YouTubePlaylistListServiceITest(YouTubeChannelsService channelsService, YouTubePlaylistsService playlistsService) {
        super(channelsService);
        this.playlistsService = playlistsService;
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistIdWithValidId_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final Playlist playlist = playlistsService.getPlaylist(TEST_PLAYLIST_ID);
        assertThat("playlist not found", playlist, notNullValue());
        if (playlist != null) {
            assertThat("playlist id does not match", playlist.getId(), equalTo(TEST_PLAYLIST_ID));
        }
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistsIdWithValidId_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final List<Playlist> playlists = playlistsService.getPlaylists(Collections.singletonList(TEST_PLAYLIST_ID));
        assertThat("no playlists found", playlists, not(empty()));
        for (Playlist playlist : playlists) {
            assertThat("playlist id does not match", playlist.getId(), equalTo(TEST_PLAYLIST_ID));
        }
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistsIdWithEmptyList_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final List<Playlist> playlists = playlistsService.getPlaylists(Collections.emptyList());
        assertThat("playlists found", playlists, empty());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistsForChannelIdWithValidId_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final List<Playlist> playlists = playlistsService.getPlaylistsForChannelId(TEST_CHANNEL_ID);
        assertThat("no playlists found", playlists, not(empty()));
        for (Playlist playlist : playlists) {
            assertThat("channel id does not match", playlist.getChannelId(), equalTo(TEST_CHANNEL_ID));
        }
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistWithInvalidId_Then_ReturnNull() throws IOException {
        assumeFalse(quotaExceeded);
        final Playlist playlist = playlistsService.getPlaylist(BAD_TEST_PLAYLIST_ID);
        assertThat("playlists found", playlist, nullValue());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistsWithInvalidId_Then_ReturnNull() throws IOException {
        assumeFalse(quotaExceeded);
        final List<Playlist> playlist = playlistsService.getPlaylists(Collections.singletonList(BAD_TEST_PLAYLIST_ID));
        assertThat("playlists found", playlist, empty());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistsForChannelIdWithInvalidId_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final List<Playlist> playlists = playlistsService.getPlaylistsForChannelId(BAD_TEST_CHANNEL_ID);
        assertThat("playlists found", playlists, empty());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistWithNull_Then_ThrowException() {
        assumeFalse(quotaExceeded);
        assertThrows(
                IllegalArgumentException.class,
                () -> playlistsService.getPlaylist(null),
                "Expected getPlaylist(null) to throw exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistsWithNull_Then_ThrowException() {
        assumeFalse(quotaExceeded);
        assertThrows(
                IllegalArgumentException.class,
                () -> playlistsService.getPlaylists(null),
                "Expected getPlaylists(null) to throw exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistForChannelIdWithNull_Then_ThrowException() {
        assumeFalse(quotaExceeded);
        assertThrows(
                IllegalArgumentException.class,
                () -> playlistsService.getPlaylistsForChannelId(null),
                "Expected getPlaylistForChannelId(null) to throw exception");
    }
}
