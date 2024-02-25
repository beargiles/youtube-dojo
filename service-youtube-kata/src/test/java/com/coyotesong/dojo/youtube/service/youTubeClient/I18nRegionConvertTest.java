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

import com.coyotesong.dojo.youtube.model.I18nRegion;
import com.google.api.services.youtube.model.I18nRegionSnippet;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static com.coyotesong.dojo.youtube.service.youTubeClient.TestConstants.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test I18nRegion conversion
 */
public class I18nRegionConvertTest {

    @NotNull
    com.google.api.services.youtube.model.I18nRegion convert(I18nRegion expected) {
        final com.google.api.services.youtube.model.I18nRegion value = new com.google.api.services.youtube.model.I18nRegion();
        value.setId(expected.getId());
        value.setEtag(expected.getEtag());
        // value.setHl(expected.getHl()); -- does not exist
        // value.setParentEtag(expected.getParentEtag()); -- does not exist

        final I18nRegionSnippet snippet = new I18nRegionSnippet();
        snippet.setGl(expected.getGl());
        snippet.setName(expected.getName());
        value.setSnippet(snippet);

        return value;
    }

    @Test
    public void Given_I18nRegion_When_Convert_Then_Success() {
        final I18nRegion expected = new I18nRegion();
        expected.setEtag(TEST_ETAG);
        expected.setId(TEST_ID);
        expected.setGl(TEST_GL);
        expected.setHl(TEST_HL);
        expected.setName(TEST_LANGUAGE_NAME);
        expected.setParentEtag(TEST_PARENT_ETAG);

        // note: 'parentEtag' and 'hl' are not set in this method
        final I18nRegion actual = ClientForI18nRegionListFactory.convert(convert(expected));

        assertThat(actual.getEtag(), equalTo(expected.getEtag()));
        assertThat(actual.getGl(), equalTo(expected.getGl()));
        assertThat(actual.getId(), equalTo(expected.getId()));
        assertThat(actual.getName(), equalTo(expected.getName()));
        // assertThat(actual.getHl(), equalTo(expected.getHl()));
        // assertThat(actual.getParentEtag(), equalTo(expected.getParentEtag()));
    }
}
