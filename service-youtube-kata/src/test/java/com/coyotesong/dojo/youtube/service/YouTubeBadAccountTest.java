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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Verify we properly handle bad credentials
 * <p>
 * NOTE: this is actually an integration test
 */
// @ContextConfiguration
@ActiveProfiles("BadYouTubeAccount")
@ExtendWith(SpringExtension.class)
@Import({
        LogSanitizerImpl.class
})
@ImportAutoConfiguration(classes = {
        YouTubeContext.class,
        BadYouTubeProperties.class
})
@SpringBootTest(classes = {
        ClientForChannelListFactory.class,
        YouTubeChannelsServiceImpl.class
})
public class YouTubeBadAccountTest {

    @Autowired
    private YouTubeChannelsService service;

    @Test
    public void Given_BadAccountCredentials_When_RestCall_Then_Failure() {
        assertThrows(
                YouTubeAuthenticationFailureException.class,
                () -> service.getChannel(TestConstants.TEST_CHANNEL_ID),
                "Expected request to throw AuthenticationFailureException");
    }
}
