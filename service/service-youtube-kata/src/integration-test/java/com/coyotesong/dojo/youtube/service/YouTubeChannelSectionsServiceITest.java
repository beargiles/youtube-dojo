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
import com.coyotesong.dojo.youtube.model.ChannelSection;
import com.coyotesong.dojo.youtube.security.LogSanitizerImpl;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForChannelListFactory;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForChannelSectionListFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.List;

import static com.coyotesong.dojo.youtube.service.TestConstants.BAD_TEST_CHANNEL_USERNAME;
import static com.coyotesong.dojo.youtube.service.TestConstants.TEST_CHANNEL_ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test YouTubeChannelSectionsService
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                LogSanitizerImpl.class,
                YouTubeContext.class,
                ClientForChannelListFactory.class,
                ClientForChannelSectionListFactory.class,
                YouTubeChannelsServiceImpl.class,
                YouTubeChannelSectionsServiceImpl.class
        })
@DirtiesContext
public class YouTubeChannelSectionsServiceITest extends AbstractYouTubeServiceITest {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeChannelSectionsServiceITest.class);

    @Value("${quotaExceeded}")
    private boolean quotaExceeded;

    private final YouTubeChannelSectionsService sectionsService;

    @Autowired
    public YouTubeChannelSectionsServiceITest(YouTubeChannelsService channelsService, YouTubeChannelSectionsService sectionsService) {
        super(channelsService);
        this.sectionsService = sectionsService;
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelSectionsWithValidChannel_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final List<ChannelSection> sections = sectionsService.getChannelSectionsForChannelId(TEST_CHANNEL_ID);
        assertThat("channel sections not found", sections, not(empty()));
        for (ChannelSection section : sections) {
            assertThat("channel section channel id does not match", section.getChannelId(), equalTo(TEST_CHANNEL_ID));
        }
        LOG.debug("\nsections:" + String.join("\n  ", sections.stream().map(ChannelSection::toString).toList()));
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelSectionsForWithInvalidChannel_Then_ReturnEmpty() throws IOException {
        assumeFalse(quotaExceeded);
        final List<ChannelSection> sections = sectionsService.getChannelSectionsForChannelId(BAD_TEST_CHANNEL_USERNAME);
        assertThat("sections should be empty", sections, empty());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelSectionsForChannelIdWithNull_Then_ThrowException() {
        assumeFalse(quotaExceeded);
        assertThrows(
                IllegalArgumentException.class,
                () -> sectionsService.getChannelSectionsForChannelId(null),
                "Expected getChannelSectionsForChannelId(null) to throw exception");
    }
}
