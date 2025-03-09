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
import com.coyotesong.dojo.youtube.security.LogSanitizerImpl;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForChannelListFactory;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForPlaylistImageListFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;

import static com.coyotesong.dojo.youtube.service.TestConstants.BAD_TEST_PLAYLIST_ID;
import static com.coyotesong.dojo.youtube.service.TestConstants.TEST_PLAYLIST_ID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test YouTubePlaylistImagesService
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                LogSanitizerImpl.class,
                YouTubeContext.class,
                ClientForChannelListFactory.class,
                ClientForPlaylistImageListFactory.class,
                YouTubeChannelsServiceImpl.class,
                YouTubePlaylistImagesServiceImpl.class
        })
@DirtiesContext
public class YouTubePlaylistImagesListServiceITest extends AbstractYouTubeServiceITest {

    @Value("${quotaExceeded}")
    private Boolean quotaExceeded;

    private final YouTubePlaylistImagesService playlistImagesService;

    @Autowired
    public YouTubePlaylistImagesListServiceITest(YouTubeChannelsService channelsService, YouTubePlaylistImagesService playlistImagesService) {
        super(channelsService);
        this.playlistImagesService = playlistImagesService;
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistImageForPlaylistIdWithValidId_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        // oops - it turns out that we'll need Oauth token for this call!
        assertThrows(
                YouTubeAccessForbiddenException.class,
                () -> playlistImagesService.getPlaylistImageForPlaylistId(TEST_PLAYLIST_ID),
                "Expected AccessForbidden Exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistImageForPlaylistIdWithInvalidId_Then_ReturnNull() throws IOException {
        assumeFalse(quotaExceeded);
        // oops - it turns out that we'll need Oauth token for this call!
        assertThrows(
                YouTubeAccessForbiddenException.class,
                () -> playlistImagesService.getPlaylistImageForPlaylistId(BAD_TEST_PLAYLIST_ID),
                "Expected AccessForbidden Exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistForPlaylistIdWithNull_Then_ThrowException() {
        assumeFalse(quotaExceeded);
        assertThrows(
                IllegalArgumentException.class,
                () -> playlistImagesService.getPlaylistImageForPlaylistId(null),
                "Expected getPlaylistImageForPlaylistId(null) to throw exception");
    }
}
