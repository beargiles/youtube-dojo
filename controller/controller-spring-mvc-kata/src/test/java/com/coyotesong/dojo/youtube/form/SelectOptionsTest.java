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

package com.coyotesong.dojo.youtube.form;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

/**
 * Verify 'select options' are what we expect
 */
public class SelectOptionsTest {
    @Test
    public void Given_ChannelTypeSearchOptions_When_VerifyNotEmpty_Then_Success() {
        assertThat("channelTypeOptions is empty", SelectOption.CHANNEL_TYPE_SELECT_LIST, not(empty()));
    }

    @Test
    public void Given_EventTypeSearchOptions_When_VerifyNotEmpty_Then_Success() {
        assertThat("eventTypeOptions is empty", SelectOption.EVENT_TYPE_SELECT_LIST, not(empty()));
    }

    @Test
    public void Given_LanguageSearchOptions_When_VerifyNotEmpty_Then_Success() {
        assertThat("languageSelectOptions is empty", SelectOption.LANGUAGE_SELECT_LIST, not(empty()));
    }

    @Test
    public void Given_OrderSearchOptions_When_VerifyNotEmpty_Then_Success() {
        assertThat("orderSelectOptions is empty", SelectOption.SORT_ORDER_SELECT_LIST, not(empty()));
    }

    @Test
    public void Given_SaveSearchOptions_When_VerifyNotEmpty_Then_Success() {
        assertThat("safeSearchOptions is empty", SelectOption.SAFE_SEARCH_SELECT_LIST, not(empty()));
    }
}
