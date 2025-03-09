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
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Verify we properly handle bad credentials
 */
@ActiveProfiles("BadYouTubeAccount")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {

                LogSanitizerImpl.class,
                YouTubeContext.class,
                BadYouTubeProperties.class,
                ClientForChannelListFactory.class,
                YouTubeChannelsServiceImpl.class
        })
@DirtiesContext
class YouTubeBadAccountITest extends AbstractYouTubeServiceITest {

    @Value("${quotaExceeded}")
    private boolean quotaExceeded;

    private final YouTubeChannelsService channelsService;

    @Autowired
    public YouTubeBadAccountITest(YouTubeChannelsService channelsService) {
        super(channelsService);
        this.channelsService = channelsService;
    }

    @Test
    @Ignore("'quotaExceeded' currently includes a check for bad credentials")
    public void Given_BadAccountCredentials_When_RestCall_Then_Failure() {
        assumeFalse(quotaExceeded);
        assertThrows(
                YouTubeAuthenticationFailureException.class,
                () -> channelsService.getChannel(TestConstants.TEST_CHANNEL_ID),
                "Expected request to throw AuthenticationFailureException");
    }
}
