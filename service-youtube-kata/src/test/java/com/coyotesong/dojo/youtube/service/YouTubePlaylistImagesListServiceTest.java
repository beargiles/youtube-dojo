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
import com.coyotesong.dojo.youtube.config.YouTubeProperties;
import com.coyotesong.dojo.youtube.security.LogSanitizerImpl;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForPlaylistImageListFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static com.coyotesong.dojo.youtube.service.TestConstants.BAD_TEST_PLAYLIST_ID;
import static com.coyotesong.dojo.youtube.service.TestConstants.TEST_PLAYLIST_ID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.abort;

/**
 * Test YouTubePlaylistImagesService
 * <p>
 * NOTE: this is actually an integration test
 */
// @ContextConfiguration
@ExtendWith(SpringExtension.class)
@Import({
        LogSanitizerImpl.class
})
@ImportAutoConfiguration(classes = {
        YouTubeContext.class,
        YouTubeProperties.class
})
@SpringBootTest(classes = {
        ClientForPlaylistImageListFactory.class,
        YouTubePlaylistImagesServiceImpl.class
})
public class YouTubePlaylistImagesListServiceTest {

    @Autowired
    private YouTubePlaylistImagesService service;

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistImageForPlaylistIdWithValidId_Then_Success() throws IOException {
        try {
            // oops - it turns out that we'll need Oauth token for this call!
            assertThrows(
                    YouTubeAccessForbiddenException.class,
                    () -> service.getPlaylistImageForPlaylistId(TEST_PLAYLIST_ID),
                    "Expected AccessForbidden Exception");
        } catch (YouTubeAccountException e) {
            abort("quota exceeded");
        }
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistImageForPlaylistIdWithInvalidId_Then_ReturnNull() throws IOException {
        try {
            // oops - it turns out that we'll need Oauth token for this call!
            assertThrows(
                    YouTubeAccessForbiddenException.class,
                    () -> service.getPlaylistImageForPlaylistId(BAD_TEST_PLAYLIST_ID),
                    "Expected AccessForbidden Exception");
        } catch (YouTubeAccountException e) {
            abort("quota exceeded");
        }
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistForPlaylistIdWithNull_Then_ThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getPlaylistImageForPlaylistId(null),
                "Expected getPlaylistImageForPlaylistId(null) to throw exception");
    }
}
