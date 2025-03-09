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

package com.coyotesong.dojo.youtube.repository;

import com.coyotesong.dojo.youtube.model.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class TestObjectFactory {

    public I18nLanguage newLanguage(int idx) {
        final I18nLanguage language = new I18nLanguage();
        // language.setEtag("etag-" + idx);
        // language.setParentEtag("parentEtag-" + idx);

        switch (idx) {
            case 1:
                language.setCode("en");
                language.setName("English");
                language.setHl("en");
                break;

            case 2:
                language.setCode("es");
                language.setName("Español");
                language.setHl("es");
                break;

            default:
                throw new RuntimeException("index out of range!");
        }

        return language;
    }

    public I18nRegion newRegion(int idx) {
        final I18nRegion region = new I18nRegion();
        // region.setEtag("etag-" + idx);
        // region.setParentEtag("parentEtag-" + idx);

        switch (idx) {
            case 1:
                region.setCode("US");
                region.setName("United States");
                region.setHl("en");
                region.setGl("US");
                break;

            case 2:
                region.setCode("US");
                region.setName("Estados Unidos");
                region.setHl("es");
                region.setGl("US");
                break;

            default:
                throw new RuntimeException("index out of range!");
        }

        return region;
    }

    public Channel newChannel(int idx) {
        final Channel channel = new Channel();
        channel.setChannelId("channel-" + idx);
        channel.setHandle("handle-" + idx);
        channel.setTitle("title-" + idx);
        channel.setDescription("description-" + idx);
        channel.setVideoCount(1000L + idx);
        channel.setSubscriberCount(2000L + idx);
        channel.setViewCount(3000L + idx);
        channel.setUploads("uploads-" + idx);
        channel.setPublishedAt(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        channel.setLastChecked(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        channel.setEtag("etag-" + idx);

        return channel;
    }

    public ChannelSection newChannelSection(int idx) {
        final ChannelSection cs = new ChannelSection();
        cs.setSectionId("cs-" + idx);
        cs.setType("multiplechannels");
        cs.setTitle("section-" + idx);
        cs.setPosition(1);
        cs.setHl("hl-" + idx);
        cs.setLang("en-" + idx);

        return cs;
    }

    public Playlist newPlaylist(int idx) {
        final Playlist playlist = new Playlist();
        playlist.setSummary(true);
        playlist.setId("playlist-id");
        playlist.setTitle("title-" + idx);
        playlist.setDescription("description-" + idx);
        // playlist.setHl("hl-" + idx);
        playlist.setLang("en-" + idx);
        // playlist.setItemCount(0L);
        // playlist.setPlaylistImage();
        // playlist.setEmbedHtml();
        // playlist.setTnDefaultUrl();
        // playlist.setTnVideoId();
        playlist.setPublishedAt(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        playlist.setLastChecked(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        playlist.setEtag("etag-" + idx);
        playlist.setParentEtag("parent-etag-" + idx);

        // playlist.setChannelId("playlist-" + idx);
        // playlist.setChannelKey();

        return playlist;
    }
}
