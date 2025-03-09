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
import com.coyotesong.dojo.youtube.model.VideoCategory;
import com.coyotesong.dojo.youtube.security.LogSanitizerImpl;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForChannelListFactory;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForVideoCategoryListFactory;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForVideoListFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.List;

import static com.coyotesong.dojo.youtube.service.TestConstants.BAD_TEST_HL;
import static com.coyotesong.dojo.youtube.service.TestConstants.TEST_HL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test YouTubeVideoCategoriesService
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                LogSanitizerImpl.class,
                YouTubeContext.class,
                ClientForChannelListFactory.class,
                ClientForVideoCategoryListFactory.class,
                YouTubeChannelsServiceImpl.class,
                YouTubeVideoCategoriesServiceImpl.class
        })
@DirtiesContext
public class YouTubeVideoCategoriesServiceITest extends AbstractYouTubeServiceITest {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeVideoCategoriesServiceITest.class);

    @Value("${quotaExceeded}")
    private Boolean quotaExceeded;

    private final YouTubeVideoCategoriesService categoriesService;

    @Autowired
    public YouTubeVideoCategoriesServiceITest(YouTubeChannelsService channelsService, YouTubeVideoCategoriesService categoriesService) {
        super(channelsService);
        this.categoriesService = categoriesService;
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetVideoCategoriesWithValidLang_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final List<VideoCategory> categories = categoriesService.getVideoCategories(TEST_HL);
        assertThat("video categories does not exist", categories, not(empty()));
        LOG.debug("\ncategories:" + String.join("\n  ", categories.stream().map(VideoCategory::toString).toList()));
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetVideoCategoriesWithInvalidLang_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        assertThrows(
                YouTubeClientException.class,
                () -> categoriesService.getVideoCategories(BAD_TEST_HL),
                "Excpected getVideoCategories('bad' to throw exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetVideoCategoriesWithNull_Then_ThrowException() {
        assumeFalse(quotaExceeded);
        assertThrows(
                IllegalArgumentException.class,
                () -> categoriesService.getVideoCategories(null),
                "Expected getVideoCategories(null) to throw exception");
    }
}
