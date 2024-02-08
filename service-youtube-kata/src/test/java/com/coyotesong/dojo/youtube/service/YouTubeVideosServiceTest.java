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
import com.coyotesong.dojo.youtube.model.Video;
import com.coyotesong.dojo.youtube.security.LogSanitizerImpl;
import com.coyotesong.dojo.youtube.service.youtubeClient.ClientForVideoListFactory;
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
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test YouTubeVideosService
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
        ClientForVideoListFactory.class,
        YouTubeVideosServiceImpl.class
})
public class YouTubeVideosServiceTest {
    private static final String TEST_VIDEO_ID = "r56jAAychzY";
    private static final String BAD_TEST_VIDEO_ID = "test-bad-video-id";

    @Autowired
    private YouTubeVideosService service;

    @Test
    public void Given_UserIsAuthenticated_When_GetVideoWithValidId_Then_Success() throws IOException {
        final Video video = service.getVideo(TEST_VIDEO_ID);
        assertThat("video id does not match", video.getId(), equalTo(TEST_VIDEO_ID));
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetVideosWithValidId_Then_Success() throws IOException {
        final List<Video> videos = service.getVideos(Collections.singletonList(TEST_VIDEO_ID));
        assertThat("no videos found", not(videos.isEmpty()));
        if (!videos.isEmpty()) {
            assertThat("video id does not match", videos.get(0).getId(), equalTo(TEST_VIDEO_ID));
        }
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetVideosWithEmptyList_Then_Success() throws IOException {
        final List<Video> videos = service.getVideos(Collections.emptyList());
        assertThat("videos found", not(videos.isEmpty()));
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetVideoWithInvalidId_Then_ReturnNull() throws IOException {
        final Video video = service.getVideo(BAD_TEST_VIDEO_ID);
        assertThat("video is null", video, nullValue());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetVideoWithNull_Then_ThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getVideo(null),
                "Expected getVideo(null) to throw exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetVideosWithNull_Then_ThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getVideos(null),
                "Expected getVideos(null) to throw exception");
    }
}
