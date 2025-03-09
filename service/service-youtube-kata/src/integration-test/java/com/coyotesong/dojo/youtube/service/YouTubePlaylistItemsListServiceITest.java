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
import com.coyotesong.dojo.youtube.model.PlaylistItem;
import com.coyotesong.dojo.youtube.security.LogSanitizerImpl;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForChannelListFactory;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForPlaylistItemListFactory;
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
 * Test YouTubePlaylistItemsService
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                LogSanitizerImpl.class,
                YouTubeContext.class,
                ClientForChannelListFactory.class,
                ClientForPlaylistItemListFactory.class,
                YouTubeChannelsServiceImpl.class,
                YouTubePlaylistItemsServiceImpl.class
        })
@DirtiesContext
public class YouTubePlaylistItemsListServiceITest extends AbstractYouTubeServiceITest {

    @Value("${quotaExceeded}")
    private Boolean quotaExceeded;

    private final YouTubePlaylistItemsService playlistItemsService;

    @Autowired
    public YouTubePlaylistItemsListServiceITest(YouTubeChannelsService channelsService, YouTubePlaylistItemsService playlistItemsService) {
        super(channelsService);
        this.playlistItemsService = playlistItemsService;
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistItemWithValidId_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final PlaylistItem item = playlistItemsService.getPlaylistItem(TEST_PLAYLIST_ITEM_ID);
        assertThat("playlist id does not match", item.getId(), equalTo(TEST_PLAYLIST_ITEM_ID));
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistItemsIdWithValidId_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final List<PlaylistItem> items = playlistItemsService.getPlaylistItems(Collections.singletonList(TEST_PLAYLIST_ITEM_ID));
        assertThat("no items found", items, not(empty()));
        for (PlaylistItem item : items) {
            assertThat("playlist item id does not match", item.getId(), equalTo(TEST_PLAYLIST_ITEM_ID));
        }
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistItemsIdWithEmptyList_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final List<PlaylistItem> items = playlistItemsService.getPlaylistItems(Collections.emptyList());
        assertThat("items found", items, empty());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistItemsForPlaylistIdWithValidId_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final List<PlaylistItem> items = playlistItemsService.getPlaylistItemsForPlaylistId(TEST_PLAYLIST_ID);
        assertThat("no items found", items, not(empty()));
        for (PlaylistItem item : items) {
            assertThat("playlist item id does not match", item.getPlaylistId(), equalTo(TEST_PLAYLIST_ID));
        }
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistWithInvalidId_Then_ReturnNull() throws IOException {
        assumeFalse(quotaExceeded);
        final PlaylistItem item = playlistItemsService.getPlaylistItem(BAD_TEST_PLAYLIST_ITEM_ID);
        assertThat("item found", item, nullValue());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistsWithInvalidId_Then_ReturnNull() throws IOException {
        assumeFalse(quotaExceeded);
        final List<PlaylistItem> items = playlistItemsService.getPlaylistItems(Collections.singletonList(BAD_TEST_PLAYLIST_ITEM_ID));
        assertThat("items found", items, empty());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistItemsForPlaylistIdWithInvalidId_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final List<PlaylistItem> items = playlistItemsService.getPlaylistItemsForPlaylistId(BAD_TEST_PLAYLIST_ID);
        assertThat("playlist items found", items, empty());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistItemWithNull_Then_ThrowException() {
        assumeFalse(quotaExceeded);
        assertThrows(
                IllegalArgumentException.class,
                () -> playlistItemsService.getPlaylistItem(null),
                "Expected getPlaylistItem(null) to throw exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistItemsWithNull_Then_ThrowException() {
        assumeFalse(quotaExceeded);
        assertThrows(
                IllegalArgumentException.class,
                () -> playlistItemsService.getPlaylistItems(null),
                "Expected getPlaylistItems(null) to throw exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistItemsForPlaylistIdWithNull_Then_ThrowException() {
        assumeFalse(quotaExceeded);
        assertThrows(
                IllegalArgumentException.class,
                () -> playlistItemsService.getPlaylistItemsForPlaylistId(null),
                "Expected getPlaylistItemsForPlaylistId(null) to throw exception");
    }
}

