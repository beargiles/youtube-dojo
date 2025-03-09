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

import com.coyotesong.dojo.youtube.model.Channel;
import com.coyotesong.dojo.youtube.model.ChannelSection;
import com.coyotesong.dojo.youtube.model.Playlist;
import com.coyotesong.dojo.youtube.model.WikipediaTopic;

import java.io.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ExternalDatabaseImpl {

    String nullp(String s) {
        if (s == null) {
            return "\\N";
        }
        return s.trim().replace("\t", "\\t").replace("\r", "").replace("\n", "\\\\n");
    }

    String nullp(Instant instant) {
        if (instant == null) {
            return "\\N";
        }

        return ISO_INSTANT.format(instant.truncatedTo(ChronoUnit.SECONDS)).replace("T", " ").replace("Z", "");
    }

    public void createDatabase(String filename, Collection<Channel> channels) throws IOException {
        final Properties channelCategories = new Properties();
        try (Reader rp = new FileReader("/tmp/channel-category.properties")) {
            channelCategories.load(rp);
        }

        try (Writer w = new FileWriter(filename);
             PrintWriter pw = new PrintWriter(w)) {

            pw.println("delete from channel_x_freebase_topic;");
            pw.println("delete from channel_x_wikipedia_topic;");
            pw.println("delete from channel_section;");
            pw.println("delete from playlist;");
            pw.println("delete from channel;");
            pw.println("delete from wikipedia_topic;");
            pw.println("delete from freebase_topic;");
            pw.println();

            final Map<String, WikipediaTopic> topics = new LinkedHashMap<>();

            pw.println("COPY channel(id, uploads, etag, custom_url, title, country, lang, tn_url, category, description, published_at, last_checked) FROM '/tmp/channels.tsv';");
            pw.println("COPY playlist(id, channel_id, etag, lang, title, tn_url, description, published_at, last_checked) FROM '/tmp/playlists.tsv';");
            pw.println("COPY channel_section(id, channel_id, etag, parent_etag, title, lang, hl, position, style, type, last_checked) FROM '/tmp/channel_sections.tsv';");
            pw.println("COPY channel_x_freebase_topic (channel_id, topic_id) from '/tmp/channel_x_freebase_topics.tsv';");
            pw.println("COPY channel_x_wikipedia_topic (channel_id, url) from '/tmp/channel_x_wikipedia_topics.tsv';");
            pw.println("COPY wikipedia_topic(url, label, custom) from '/tmp/wikipedia_topics.tsv';");

            // with (encoding 'tsv', null '\N', ...)
            try (Writer w1 = new FileWriter("/tmp/channels.tsv");
                 PrintWriter pw1 = new PrintWriter(w1);
                 Writer w2 = new FileWriter("/tmp/playlists.tsv");
                 PrintWriter pw2 = new PrintWriter(w2);
                 Writer w3 = new FileWriter("/tmp/channel_sections.tsv");
                 PrintWriter pw3 = new PrintWriter(w3);
                 Writer w4 = new FileWriter("/tmp/channel_x_freebase_topics.tsv");
                 PrintWriter pw4 = new PrintWriter(w4);
                 Writer w5 = new FileWriter("/tmp/channel_x_wikipedia_topics.tsv");
                 PrintWriter pw5 = new PrintWriter(w5);
            ) {

                for (Channel channel : channels) {
                    // channelCategories.put(channel.getCustomUrl().substring(1), "");

                    // merge with external list of channel categories
                    if (channelCategories.containsKey(channel.getHandle().substring(1))) {
                        final String category = channelCategories.getProperty(channel.getHandle().substring(1));
                        if (isNotBlank(category)) {
                            channel.setCategory(category);
                        }
                    }

                    pw1.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
                            channel.getChannelId(), nullp(channel.getUploads()), channel.getEtag(),
                            channel.getHandle(),
                            channel.getTitle().replace("\n", " "),
                            nullp(channel.getCountry()), nullp(channel.getLang()),
                            nullp(channel.getTnUrl()), nullp(channel.getCategory()), nullp(channel.getDescription()),
                            nullp(channel.getPublishedAt()),
                            nullp(channel.getLastChecked())
                    );
                    // nsfw

                    for (Playlist playlist : channel.getPlaylists()) {
                        pw2.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
                                playlist.getId(), playlist.getChannelId(), playlist.getEtag(),
                                nullp(playlist.getLang()), playlist.getTitle().replace("\n", " "),
                                nullp(playlist.getTnDefaultUrl()), nullp(playlist.getDescription()),
                                nullp(playlist.getPublishedAt()), nullp(playlist.getLastChecked()));
                        // embedHtml
                        // channel_title
                    }

                    for (ChannelSection section : channel.getSections()) {
                        pw3.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%d\t%s\t%s\t%s\n",
                                section.getSectionId(), section.getChannelId(), section.getEtag(), section.getParentEtag(),
                                nullp(section.getTitle()),
                                nullp(section.getLang()), nullp(section.getHl()), section.getPosition(),
                                nullp(section.getStyle()), nullp(section.getType()),
                                nullp(section.getLastChecked()));

                        // from 'ChannelSectionContentDetails'
                        //private List<String> channelIds = new ArrayList<>();
                        //private List<String> playlistIds = new ArrayList<>();
                    }

                    if (!channel.getTopicIds().isEmpty()) {
                        channel.getTopicIds().forEach(topic -> {
                            pw4.printf("%s\t%s\n", channel.getChannelId(), topic);
                        });
                    }

                    if (!channel.getTopicCategories().isEmpty()) {
                        channel.getTopicCategories().forEach(category -> {
                            pw5.printf("%s\t%s\n", channel.getChannelId(), category.getUrl());
                        });
                    }

                    if (!channel.getTopicCategories().isEmpty()) {
                        channel.getTopicCategories().forEach(s -> topics.put(s.getUrl().toExternalForm(), s));
                    }
                }
            }

            try (Writer w1 = new FileWriter("/tmp/wikipedia_topics.tsv");
                 PrintWriter pw1 = new PrintWriter(w1)) {

                topics.values().forEach(topic -> pw1.printf("%s\t%s\t%s\n", topic.getUrl().toExternalForm(), topic.getLabel(), false));
            }
        }

        /*
        try (Writer wp = new FileWriter("/tmp/channel-category.properties")) {
            channelCategories.store(wp, "channel categories");
        }
         */
    }
}
