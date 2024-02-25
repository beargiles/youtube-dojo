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

import com.coyotesong.dojo.youtube.model.VideoCategory;
import com.google.api.services.youtube.model.VideoCategorySnippet;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static com.coyotesong.dojo.youtube.service.youTubeClient.TestConstants.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test VideoCategory conversion
 */
public class VideoCategoryConvertTest {

    @NotNull
    com.google.api.services.youtube.model.VideoCategory convert(VideoCategory expected) {
        final com.google.api.services.youtube.model.VideoCategory value = new com.google.api.services.youtube.model.VideoCategory();
        value.setId(expected.getId());
        value.setEtag(expected.getEtag());

        final VideoCategorySnippet snippet = new VideoCategorySnippet();
        snippet.setChannelId(expected.getChannelId());
        snippet.setTitle(expected.getTitle());
        snippet.setAssignable(expected.getAssignable());
        value.setSnippet(snippet);

        return value;
    }

    @Test
    public void Given_VideoCategory_When_Convert_Then_Success() {
        final VideoCategory expected = new VideoCategory();
        expected.setAssignable(TEST_ASSIGNABLE);
        expected.setChannelId(TEST_CHANNEL_ID);
        expected.setEtag(TEST_ETAG);
        expected.setId(TEST_ID);
        expected.setParentEtag(TEST_PARENT_ETAG);
        expected.setTitle(TEST_TITLE);

        // note: 'parentEtag' and 'hl' are not set in this method
        final VideoCategory actual = ClientForVideoCategoryListFactory.convert(convert(expected));

        assertThat(actual.getAssignable(), equalTo(expected.getAssignable()));
        assertThat(actual.getChannelId(), equalTo(expected.getChannelId()));
        assertThat(actual.getEtag(), equalTo(expected.getEtag()));
        assertThat(actual.getId(), equalTo(expected.getId()));
        assertThat(actual.getTitle(), equalTo(expected.getTitle()));
        // assertThat(actual.getHl(), equalTo(expected.getHl()));
        // assertThat(actual.getParentEtag(), equalTo(expected.getParentEtag()));
    }
}
