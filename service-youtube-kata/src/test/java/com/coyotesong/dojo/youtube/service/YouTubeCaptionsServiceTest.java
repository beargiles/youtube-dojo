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
import com.coyotesong.dojo.youtube.model.Caption;
import com.coyotesong.dojo.youtube.security.LogSanitizerImpl;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForCaptionListFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static com.coyotesong.dojo.youtube.service.TestConstants.BAD_TEST_VIDEO_ID;
import static com.coyotesong.dojo.youtube.service.TestConstants.TEST_VIDEO_ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Test YouTubeCaptionsService
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
        ClientForCaptionListFactory.class,
        YouTubeCaptionsServiceImpl.class
})
public class YouTubeCaptionsServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeCaptionsServiceTest.class);

    @Autowired
    private YouTubeCaptionsService service;

    @Test
    public void Given_UserIsAuthenticated_When_GetCaptionsForVideoIdWithValidId_Then_Success() throws IOException {
        try {
            final List<Caption> captions = service.getCaptionsForVideoId(TEST_VIDEO_ID);
            assertThat("captions not found", captions, not(empty()));
            for (Caption caption : captions) {
                assertThat("caption video id does not match", caption.getVideoId(), equalTo(TEST_VIDEO_ID));
            }
            LOG.debug("\ncaptions:" + String.join("\n  ", captions.stream().map(Caption::toString).toList()));
        } catch (YouTubeAccountException e) {
            assumeTrue(false, "quota exceeded");
        }
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetCaptionsForVideoIdWithInvalidId_Then_ReturnEmptyList() throws IOException {
        try {
            final List<Caption> captions = service.getCaptionsForVideoId(BAD_TEST_VIDEO_ID);
            assertThat("captions found", captions, empty());
        } catch (YouTubeAccountException e) {
            assumeTrue(false, "quota exceeded");
        }
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetCaptionsForVideoIdWithNull_Then_ThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getCaptionsForVideoId(null),
                "Expected getCaptionsForVideoId(null) to throw exception");
    }
}
