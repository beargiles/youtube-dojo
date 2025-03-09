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

package com.coyotesong.dojo.youtube.service.youTubeClient;

import com.coyotesong.dojo.youtube.model.I18nLanguage;
import com.google.api.services.youtube.model.I18nLanguageSnippet;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static com.coyotesong.dojo.youtube.service.youTubeClient.TestConstants.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test I18nLanguage conversion
 */
public class I18nLanguageConvertTest {

    @NotNull
    com.google.api.services.youtube.model.I18nLanguage convert(I18nLanguage expected) {
        final com.google.api.services.youtube.model.I18nLanguage value = new com.google.api.services.youtube.model.I18nLanguage();
        value.setId(expected.getCode());
        // value.setParentEtag(expected.getParentEtag()); -- does not exist

        final I18nLanguageSnippet snippet = new I18nLanguageSnippet();
        snippet.setHl(expected.getHl());
        snippet.setName(expected.getName());
        value.setSnippet(snippet);

        return value;
    }

    @Test
    public void Given_I18nLanguage_When_Convert_Then_Success() {
        final I18nLanguage expected = new I18nLanguage();
        expected.setHl(TEST_LOCALE);
        expected.setCode(TEST_ID);
        expected.setName(TEST_LANGUAGE_NAME);

        // note: 'parentEtag' is not set in this method
        final I18nLanguage actual = ClientForI18nLanguageListFactory.convert(convert(expected));

        assertThat(actual.getHl(), equalTo(expected.getHl()));
        assertThat(actual.getCode(), equalTo(expected.getCode()));
        assertThat(actual.getName(), equalTo(expected.getName()));
        // assertThat(actual.getParentEtag(), equalTo(expected.getParentEtag()));
    }
}
