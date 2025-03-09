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
import com.coyotesong.dojo.youtube.model.I18nLanguage;
import com.coyotesong.dojo.youtube.security.LogSanitizerImpl;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForChannelListFactory;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForI18nLanguageListFactory;
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
 * Test YouTubeI18nLanguagesService
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                LogSanitizerImpl.class,
                YouTubeContext.class,
                ClientForChannelListFactory.class,
                ClientForI18nLanguageListFactory.class,
                YouTubeChannelsServiceImpl.class,
                YouTubeI18nLanguagesServiceImpl.class
        })
@DirtiesContext
public class YouTubeI18nLanguagesServiceITest extends AbstractYouTubeServiceITest {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeI18nLanguagesServiceITest.class);

    @Value("${quotaExceeded}")
    private Boolean quotaExceeded;

    private final YouTubeI18nLanguagesService languagesService;

    @Autowired
    public YouTubeI18nLanguagesServiceITest(YouTubeChannelsService channelsService, YouTubeI18nLanguagesService languageService) {
        super(channelsService);
        this.languagesService = languageService;
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetI18nLanguagesWithValidLang_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final List<I18nLanguage> languages = languagesService.getI18nLanguages(TEST_HL);
        assertThat("languages do not exist", languages, not(empty()));
        LOG.debug("\nlanguages:" + String.join("\n  ", languages.stream().map(I18nLanguage::toString).toList()));
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetI18nLanguagesWithInvalidLang_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        assertThrows(
                YouTubeClientException.class,
                () -> languagesService.getI18nLanguages(BAD_TEST_HL),
                "Expected getI18nLanguages('bad') tho throw exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetI18nLanguagesWithNull_Then_ThrowException() {
        assumeFalse(quotaExceeded);
        assertThrows(
                IllegalArgumentException.class,
                () -> languagesService.getI18nLanguages(null),
                "Expected getI18nLanguages(null) to throw exception");
    }
}
