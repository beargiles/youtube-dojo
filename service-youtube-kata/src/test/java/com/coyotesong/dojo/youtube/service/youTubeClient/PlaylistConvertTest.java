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

import com.coyotesong.dojo.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistContentDetails;
import com.google.api.services.youtube.model.PlaylistPlayer;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.google.api.services.youtube.model.PlaylistStatus;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static com.coyotesong.dojo.youtube.service.youTubeClient.TestConstants.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test Playlist conversion
 */
public class PlaylistConvertTest {

    @NotNull
    com.google.api.services.youtube.model.Playlist convert(Playlist expected) {
        final com.google.api.services.youtube.model.Playlist value =
                new com.google.api.services.youtube.model.Playlist();
        value.setId(expected.getId());
        value.setEtag(expected.getEtag());

        final PlaylistContentDetails contentDetails = new PlaylistContentDetails();
        value.setContentDetails(contentDetails);

        final PlaylistPlayer player = new PlaylistPlayer();
        player.setEmbedHtml(expected.getEmbedHtml());
        value.setPlayer(player);

        final PlaylistSnippet snippet = new PlaylistSnippet();
        snippet.setChannelId(expected.getChannelId());
        snippet.setChannelTitle(expected.getChannelTitle());
        snippet.setDefaultLanguage(expected.getLang());
        snippet.setDescription(expected.getDescription());
        // snippet.setPublishedAt(expected.getPublishedAt()); -- reformat
        snippet.setThumbnailVideoId(expected.getTnVideoId());
        snippet.setTitle(expected.getTitle());
        // snippet.setTags();
        value.setSnippet(snippet);

        final PlaylistStatus status = new PlaylistStatus();
        value.setStatus(status);

        return value;
    }

    @Test
    public void Given_Playlist_When_Convert_Then_Success() {
        final Playlist expected = new Playlist();
        expected.setEtag(TEST_ETAG);
        expected.setId(TEST_ID);
        expected.setChannelId(TEST_CHANNEL_ID);
        expected.setChannelTitle(TEST_CHANNEL_TITLE);
        expected.setDescription(TEST_DESCRIPTION);
        expected.setEmbedHtml(TEST_EMBED_HTML);
        expected.setLang(TEST_DEFAULT_LANGUAGE);
        expected.setPublishedAt(TEST_PUBLISHED_AT);
        // expected.setTags(...)
        expected.setTitle(TEST_TITLE);
        expected.setTnUrl(TEST_THUMBNAIL_URL);
        expected.setTnVideoId(TEST_THUMBNAIL_VIDEO_ID);
        expected.setParentEtag(TEST_PARENT_ETAG);

        // note: 'parentEtag' and 'hl' are not set in this method
        final Playlist actual = ClientForPlaylistListFactory.convert(convert(expected));

        assertThat(actual.getEtag(), equalTo(expected.getEtag()));
        assertThat(actual.getId(), equalTo(expected.getId()));
        // assertThat(actual.getHl(), equalTo(expected.getHl()));
        // assertThat(actual.getParentEtag(), equalTo(expected.getParentEtag()));
    }
}
