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
import com.coyotesong.dojo.youtube.model.Channel;
import com.coyotesong.dojo.youtube.security.LogSanitizerImpl;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForChannelListFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;

import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Verify the YouTube account is valid.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                LogSanitizerImpl.class,
                YouTubeContext.class,
                ClientForChannelListFactory.class,
                YouTubeChannelsServiceImpl.class
        })
@DirtiesContext
public class YouTubeAccountITest extends AbstractYouTubeServiceITest {

    @Value("${quotaExceeded}")
    private Boolean quotaExceeded;

    private final YouTubeChannelsService channelsService;

    @Autowired
    public YouTubeAccountITest(YouTubeChannelsService channelsService) {
        super(channelsService);
        this.channelsService = channelsService;
    }

    @Test
    public void Given_GoodAccountCredentials_When_RestCall_Then_Success() throws IOException {
        // assumeFalse(quotaExceeded);
        @SuppressWarnings("unused") final Channel channel = channelsService.getChannel(TestConstants.TEST_CHANNEL_ID);
    }
}
