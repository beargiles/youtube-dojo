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
import com.coyotesong.dojo.youtube.model.PlaylistImage;
import com.coyotesong.dojo.youtube.security.LogSanitizerImpl;
import com.coyotesong.dojo.youtube.service.youtubeClient.ClientForPlaylistImageListFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

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
        YouTubeContext.class
})
@SpringBootTest(classes = {
        ClientForPlaylistImageListFactory.class,
        YouTubePlaylistImagesServiceImpl.class
})
public class YouTubePlaylistImagesListServiceTest {
    private static final String TEST_PLAYLIST_ID = "PL9hNFus3sjE5fWOXSJRo4CEj7xnTaysOW";
    private static final String BAD_TEST_PLAYLIST_ID = "test-bad-playlist-id";

    @Autowired
    private YouTubePlaylistImagesService service;

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistImageForPlaylistIdWithValidId_Then_Success() throws IOException {
        final PlaylistImage image = service.getPlaylistImageForPlaylistId(TEST_PLAYLIST_ID);
        // we might not have a playlist with an associated image
        assumeTrue(image != null, "playlist does not have associated image?");
        assertThat("playlist id does not match", image.getPlaylistId(), equalTo(TEST_PLAYLIST_ID));
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistImageForPlaylistIdWithInvalidId_Then_ReturnNull() throws IOException {
        final PlaylistImage image = service.getPlaylistImageForPlaylistId(BAD_TEST_PLAYLIST_ID);
        assertThat("image found", image, nullValue());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetPlaylistForPlaylistIdWithNull_Then_ThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getPlaylistImageForPlaylistId(null),
                "Expected getPlaylistImageForPlaylistId(null) to throw exception");
    }
}
