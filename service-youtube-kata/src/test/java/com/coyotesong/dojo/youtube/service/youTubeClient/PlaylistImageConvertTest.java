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

import com.coyotesong.dojo.youtube.model.PlaylistImage;
import com.google.api.services.youtube.model.PlaylistImageSnippet;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static com.coyotesong.dojo.youtube.service.youTubeClient.TestConstants.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test PlaylistImage conversion
 */
public class PlaylistImageConvertTest {

    @NotNull
    com.google.api.services.youtube.model.PlaylistImage convert(PlaylistImage expected) {
        final com.google.api.services.youtube.model.PlaylistImage value =
                new com.google.api.services.youtube.model.PlaylistImage();
        value.setId(expected.getId());

        final PlaylistImageSnippet snippet = new PlaylistImageSnippet();
        snippet.setHeight(expected.getHeight());
        snippet.setPlaylistId(expected.getPlaylistId());
        snippet.setType(expected.getType());
        snippet.setWidth(expected.getWidth());
        value.setSnippet(snippet);

        return value;
    }

    @Test
    public void Given_PlaylistImage_When_Convert_Then_Success() {
        final PlaylistImage expected = new PlaylistImage();
        // expected.setEtag(TEST_ETAG); // there's no setter in response
        expected.setId(TEST_ID);
        expected.setHeight(TEST_PLAYLIST_IMAGE_HEIGHT);
        expected.setPlaylistId(TEST_PLAYLIST_ID);
        expected.setType(TEST_PLAYLIST_IMAGE_TYPE);
        expected.setWidth(TEST_PLAYLIST_IMAGE_WIDTH);
        expected.setParentEtag(TEST_PARENT_ETAG);

        // note: 'parentEtag' and 'hl' are not set in this method
        final PlaylistImage actual = ClientForPlaylistImageListFactory.convert(convert(expected));

        // assertThat(actual.getEtag(), equalTo(expected.getEtag())); // there's no setter in response
        assertThat(actual.getHeight(), equalTo(expected.getHeight()));
        assertThat(actual.getId(), equalTo(expected.getId()));
        assertThat(actual.getPlaylistId(), equalTo(expected.getPlaylistId()));
        assertThat(actual.getType(), equalTo(expected.getType()));
        assertThat(actual.getWidth(), equalTo(expected.getWidth()));
        // assertThat(actual.getHl(), equalTo(expected.getHl()));
        // assertThat(actual.getParentEtag(), equalTo(expected.getParentEtag()));
    }
}
