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
import com.coyotesong.dojo.youtube.service.youtubeClient.ClientForPlaylistListFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test YouTubePlaylistsService
 * <p>
 * NOTE: this is actually an integration test
 */
// @ContextConfiguration
@ExtendWith(SpringExtension.class)
@Import({
        LogSanitizerImpl.class
})
@ImportAutoConfiguration(classes = {
        YouTubeContext.class
})
@SpringBootTest(classes = {
        ClientForPlaylistListFactory.class,
        YouTubePlaylistsServiceImpl.class
})
public class YouTubePlaylistListServiceTest {
    private static final String TEST_CHANNEL_ID = "UCciQ8wFcVoIIMi-lfu8-cjQ";
    private static final String TEST_PLAYLIST_ID = "PL9hNFus3sjE5fWOXSJRo4CEj7xnTaysOW";
    private static final String BAD_TEST_CHANNEL_ID = "test-bad-channel-id";
    private static final String BAD_TEST_PLAYLIST_ID = "test-bad-playlist-id";

    @Autowired
    private YouTubePlaylistsService service;

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistIdWithValidId_Then_Success() throws IOException {
        final Playlist playlist = service.getPlaylist(TEST_PLAYLIST_ID);
        assertThat("playlist id does not match", playlist.getId(), equalTo(TEST_PLAYLIST_ID));
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistsIdWithValidId_Then_Success() throws IOException {
        final List<Playlist> playlists = service.getPlaylists(Collections.singletonList(TEST_PLAYLIST_ID));
        assertThat("no playlists found", playlists, not(empty()));
        if (!playlists.isEmpty()) {
            assertThat("playlist id does not match", playlists.get(0).getId(), equalTo(TEST_PLAYLIST_ID));
        }
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistsIdWithEmptyList_Then_Success() throws IOException {
        final List<Playlist> playlists = service.getPlaylists(Collections.emptyList());
        assertThat("playlists found", playlists, empty());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistsForChannelIdWithValidId_Then_Success() throws IOException {
        final List<Playlist> playlists = service.getPlaylistsForChannelId(TEST_CHANNEL_ID);
        assertThat("no playlists found", playlists, not(empty()));
        if (!playlists.isEmpty()) {
            assertThat("channel id does not match", playlists.get(0).getChannelId(), equalTo(TEST_CHANNEL_ID));
        }
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistWithInvalidId_Then_ReturnNull() throws IOException {
        final Playlist playlist = service.getPlaylist(BAD_TEST_PLAYLIST_ID);
        assertThat("playlists found", playlist, nullValue());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistsWithInvalidId_Then_ReturnNull() throws IOException {
        final List<Playlist> playlist = service.getPlaylists(Collections.singletonList(BAD_TEST_PLAYLIST_ID));
        assertThat("playlists found", playlist, empty());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistsForChannelIdWithInvalidId_Then_Success() throws IOException {
        final List<Playlist> playlists = service.getPlaylistsForChannelId(BAD_TEST_CHANNEL_ID);
        assertThat("playlists found", playlists, empty());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistWithNull_Then_ThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getPlaylist(null),
                "Expected getPlaylist(null) to throw exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistsWithNull_Then_ThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getPlaylists(null),
                "Expected getPlaylists(null) to throw exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistForChannelIdWithNull_Then_ThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getPlaylistsForChannelId(null),
                "Expected getPlaylistForChannelId(null) to throw exception");
    }
}
