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
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForChannelListFactory;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForVideoListFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.coyotesong.dojo.youtube.service.TestConstants.BAD_TEST_VIDEO_ID;
import static com.coyotesong.dojo.youtube.service.TestConstants.TEST_VIDEO_ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test YouTubeVideosService
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                LogSanitizerImpl.class,
                YouTubeContext.class,
                ClientForChannelListFactory.class,
                ClientForVideoListFactory.class,
                YouTubeChannelsServiceImpl.class,
                YouTubeVideosServiceImpl.class
        }
)
@DirtiesContext
public class YouTubeVideosServiceITest extends AbstractYouTubeServiceITest {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeVideosServiceITest.class);

    @Value("${quotaExceeded}")
    private Boolean quotaExceeded;

    private final YouTubeVideosService videosService;

    @Autowired
    public YouTubeVideosServiceITest(YouTubeChannelsService channelsService, YouTubeVideosService videoService) {
        super(channelsService);
        this.videosService = videoService;
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetVideoWithValidId_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final Video video = videosService.getVideo(TEST_VIDEO_ID);
        assertThat("video not found", video, notNullValue());
        if (video != null) {
            assertThat("video id does not match", video.getId(), equalTo(TEST_VIDEO_ID));
        }
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetVideosWithValidId_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final List<Video> videos = videosService.getVideos(Collections.singletonList(TEST_VIDEO_ID));
        assertThat("no videos found", videos, not(empty()));
        for (Video video : videos) {
            assertThat("video id does not match", video.getId(), equalTo(TEST_VIDEO_ID));
        }
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetVideosWithEmptyList_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final List<Video> videos = videosService.getVideos(Collections.emptyList());
        assertThat("videos found", videos, empty());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetVideoWithInvalidId_Then_ReturnNull() throws IOException {
        assumeFalse(quotaExceeded);
        final Video video = videosService.getVideo(BAD_TEST_VIDEO_ID);
        assertThat("video is null", video, nullValue());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetVideoWithNull_Then_ThrowException() {
        assumeFalse(quotaExceeded);
        assertThrows(
                IllegalArgumentException.class,
                () -> videosService.getVideo(null),
                "Expected getVideo(null) to throw exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetVideosWithNull_Then_ThrowException() {
        assumeFalse(quotaExceeded);
        assertThrows(
                IllegalArgumentException.class,
                () -> videosService.getVideos(null),
                "Expected getVideos(null) to throw exception");
    }
}
