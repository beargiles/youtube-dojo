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
import com.coyotesong.dojo.youtube.service.youtubeClient.ClientForPlaylistItemListFactory;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test YouTubePlaylistItemsService
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
        ClientForPlaylistItemListFactory.class,
        YouTubePlaylistItemsServiceImpl.class
})
public class YouTubePlaylistItemsListServiceTest {
    private static final String TEST_PLAYLIST_ID = "PL9hNFus3sjE5fWOXSJRo4CEj7xnTaysOW";
    private static final String TEST_PLAYLIST_ITEM_ID = "UEw5aE5GdXMzc2pFNWZXT1hTSlJvNENFajd4blRheXNPVy41NkI0NEY2RDEwNTU3Q0M2";
    private static final String BAD_TEST_PLAYLIST_ID = "test-bad-playlist-id";
    private static final String BAD_TEST_PLAYLIST_ITEM_ID = "test-bad-playlist-item-id";

    @Autowired
    private YouTubePlaylistItemsService service;

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistItemWithValidId_Then_Success() throws IOException {
        final PlaylistItem item = service.getPlaylistItem(TEST_PLAYLIST_ITEM_ID);
        assertThat("playlist id does not match", item.getId(), equalTo(TEST_PLAYLIST_ITEM_ID));
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistItemssIdWithValidId_Then_Success() throws IOException {
        final List<PlaylistItem> items = service.getPlaylistItems(Collections.singletonList(TEST_PLAYLIST_ITEM_ID));
        assertThat("no items found", items, not(empty()));
        if (!items.isEmpty()) {
            assertThat("playlist item id does not match", items.get(0).getId(), equalTo(TEST_PLAYLIST_ITEM_ID));
        }
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistItemsIdWithEmptyList_Then_Success() throws IOException {
        final List<PlaylistItem> items = service.getPlaylistItems(Collections.emptyList());
        assertThat("items found", items, empty());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistItemsForPlaylistIdWithValidId_Then_Success() throws IOException {
        final List<PlaylistItem> items = service.getPlaylistItemsForPlaylistId(TEST_PLAYLIST_ID);
        assertThat("no items found", items, not(empty()));
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistWithInvalidId_Then_ReturnNull() throws IOException {
        final PlaylistItem item = service.getPlaylistItem(BAD_TEST_PLAYLIST_ITEM_ID);
        assertThat("item found", item, nullValue());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistsWithInvalidId_Then_ReturnNull() throws IOException {
        final List<PlaylistItem> items = service.getPlaylistItems(Collections.singletonList(BAD_TEST_PLAYLIST_ITEM_ID));
        assertThat("items found", items, empty());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistItemsForPlaylistIdWithInvalidId_Then_Success() throws IOException {
        final List<PlaylistItem> items = service.getPlaylistItemsForPlaylistId(BAD_TEST_PLAYLIST_ID);
        assertThat("playlist items found", items, empty());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistItemWithNull_Then_ThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getPlaylistItem(null),
                "Expected getPlaylistItem(null) to throw exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistItemsWithNull_Then_ThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getPlaylistItems(null),
                "Expected getPlaylistItems(null) to throw exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistItemsForPlaylistIdWithNull_Then_ThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getPlaylistItemsForPlaylistId(null),
                "Expected getPlaylistItemsForPlaylistId(null) to throw exception");
    }
}
