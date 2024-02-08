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
import com.coyotesong.dojo.youtube.service.youtubeClient.ClientForVideoCategoryListFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test YouTubeVideoCategoriesService
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
        ClientForVideoCategoryListFactory.class,
        YouTubeVideoCategoriesServiceImpl.class
})
public class YouTubeVideoCategoriesServiceTest {
    private static final String TEST_HL = "en";
    private static final String BAD_TEST_HL = "bad";

    @Autowired
    private YouTubeVideoCategoriesService service;

    @Test
    public void Given_UserIsAuthenticated_When_GetVideoCategoriesWithValidLang_Then_Success() throws IOException {
        final List<VideoCategory> categories = service.getVideoCategories(TEST_HL);
        assertThat("video categories does not exist", categories, not(empty()));
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetVideoCategoriesWithInvalidLang_Then_Success() throws IOException {
        final List<VideoCategory> categories = service.getVideoCategories(BAD_TEST_HL);
        assertThat("video categories exists", categories, empty());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetVideoCategoriesWithNull_Then_ThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getVideoCategories(null),
                "Expected getVideoCategories(null) to throw exception");
    }
}