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
import com.coyotesong.dojo.youtube.model.I18nRegion;
import com.coyotesong.dojo.youtube.security.LogSanitizerImpl;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForChannelListFactory;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForI18nRegionListFactory;
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
 * Test YouTubeI18nRegionsService
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                LogSanitizerImpl.class,
                YouTubeContext.class,
                ClientForChannelListFactory.class,
                ClientForI18nRegionListFactory.class,
                YouTubeChannelsServiceImpl.class,
                YouTubeI18NRegionServiceImpl.class
        })
@DirtiesContext
public class YouTubeI18nRegionServiceITest extends AbstractYouTubeServiceITest {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeI18nRegionServiceITest.class);

    @Value("${quotaExceeded}")
    private Boolean quotaExceeded;

    private final YouTubeI18nRegionService regionService;

    @Autowired
    public YouTubeI18nRegionServiceITest(YouTubeChannelsService channelsService, YouTubeI18nRegionService regionsService) {
        super(channelsService);
        this.regionService = regionsService;
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetI18nRegionsWithValidLang_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final List<I18nRegion> regions = regionService.getI18nRegions(TEST_HL);
        assertThat("regions do not exist", regions, not(empty()));
        LOG.debug("regions:" + String.join("\n  ", regions.stream().map(I18nRegion::toString).toList()));
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetI18nRegionsWithInvalidLang_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        assertThrows(
                YouTubeClientException.class,
                () -> regionService.getI18nRegions(BAD_TEST_HL),
                "Expected getI18nRegions('bad') to throw exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetI18nRegionsWithNull_Then_ThrowException() {
        assumeFalse(quotaExceeded);
        assertThrows(
                IllegalArgumentException.class,
                () -> regionService.getI18nRegions(null),
                "Expected getI18nRegions(null) to throw exception");
    }
}
