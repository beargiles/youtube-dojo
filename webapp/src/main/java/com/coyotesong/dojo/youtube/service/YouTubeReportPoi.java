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
import com.coyotesong.dojo.youtube.model.WikipediaTopic;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;

@Service
public class YouTubeReportPoi implements YouTubeReport {

    // private final FreebaseTopicRepository freebaseTopicRepository;
    // private final WikipediaTopicRepository wikipediaTopicRepository;

    // @TODO - load from database
    private final static Map<String, String> FREEBASE_TOPICS = new HashMap<>();

    static {
        // FREEBASE_TOPICS.put("/m/04rlf", "Music (parent topic)");
        FREEBASE_TOPICS.put("/m/02mscn", "Christian music");
        FREEBASE_TOPICS.put("/m/0ggq0m", "Classical music");
        FREEBASE_TOPICS.put("/m/01lyv", "Country");
        FREEBASE_TOPICS.put("/m/02lkt", "Electronic music");
        FREEBASE_TOPICS.put("/m/0glt670", "Hip hop music");
        FREEBASE_TOPICS.put("/m/05rwpb", "Independent music");
        FREEBASE_TOPICS.put("/m/03_d0", "Jazz");
        FREEBASE_TOPICS.put("/m/028sqc", "Music of Asia");
        FREEBASE_TOPICS.put("/m/0g293", "Music of Latin America");
        FREEBASE_TOPICS.put("/m/064t9", "Pop music");
        FREEBASE_TOPICS.put("/m/06cqb", "Reggae");
        FREEBASE_TOPICS.put("/m/06j6l", "Rhythm and blues");
        FREEBASE_TOPICS.put("/m/06by7", "Rock music");
        FREEBASE_TOPICS.put("/m/0gywn", "Soul music");
        // FREEBASE_TOPICS.put("/m/0bzvm2", "Gaming (parent topic)");
        FREEBASE_TOPICS.put("/m/025zzc", "Action game");
        FREEBASE_TOPICS.put("/m/02ntfj", "Action-adventure game");
        FREEBASE_TOPICS.put("/m/0b1vjn", "Casual game");
        FREEBASE_TOPICS.put("/m/02hygl", "Music video game");
        FREEBASE_TOPICS.put("/m/04q1x3q", "Puzzle video game");
        FREEBASE_TOPICS.put("/m/01sjng", "Racing video game");
        FREEBASE_TOPICS.put("/m/0403l3g", "Role-playing video game");
        FREEBASE_TOPICS.put("/m/021bp2", "Simulation video game");
        FREEBASE_TOPICS.put("/m/022dc6", "Sports game");
        FREEBASE_TOPICS.put("/m/03hf_rm", "Strategy video game");
        // FREEBASE_TOPICS.put("/m/06ntj", "Sports (parent topic)");
        FREEBASE_TOPICS.put("/m/0jm_", "American football");
        FREEBASE_TOPICS.put("/m/018jz", "Baseball");
        FREEBASE_TOPICS.put("/m/018w8", "Basketball");
        FREEBASE_TOPICS.put("/m/01cgz", "Boxing");
        FREEBASE_TOPICS.put("/m/09xp_", "Cricket");
        FREEBASE_TOPICS.put("/m/02vx4", "Football");
        FREEBASE_TOPICS.put("/m/037hz", "Golf");
        FREEBASE_TOPICS.put("/m/03tmr", "Ice hockey");
        FREEBASE_TOPICS.put("/m/01h7lh", "Mixed martial arts");
        FREEBASE_TOPICS.put("/m/0410tth", "Motorsport");
        FREEBASE_TOPICS.put("/m/07bs0", "Tennis");
        FREEBASE_TOPICS.put("/m/07_53", "Volleyball");
        // FREEBASE_TOPICS.put("/m/02jjt", "Entertainment (parent topic)");
        FREEBASE_TOPICS.put("/m/09kqc", "Humor");
        FREEBASE_TOPICS.put("/m/02vxn", "Movies");
        FREEBASE_TOPICS.put("/m/05qjc", "Performing arts");
        FREEBASE_TOPICS.put("/m/066wd", "Professional wrestling");
        FREEBASE_TOPICS.put("/m/0f2f9", "TV shows");
        // FREEBASE_TOPICS.put("/m/019_rr", "Lifestyle (parent topic)");
        FREEBASE_TOPICS.put("/m/032tl", "Fashion");
        FREEBASE_TOPICS.put("/m/027x7n", "Fitness");
        FREEBASE_TOPICS.put("/m/02wbm", "Food");
        FREEBASE_TOPICS.put("/m/03glg", "Hobby");
        FREEBASE_TOPICS.put("/m/068hy", "Pets");
        FREEBASE_TOPICS.put("/m/041xxh", "Physical attractiveness [Beauty]");
        FREEBASE_TOPICS.put("/m/07c1v", "Technology");
        FREEBASE_TOPICS.put("/m/07bxq", "Tourism");
        FREEBASE_TOPICS.put("/m/07yv9", "Vehicles");
        // FREEBASE_TOPICS.put("/m/098wr", "Society (parent topic)");
        FREEBASE_TOPICS.put("/m/09s1f", "Business");
        FREEBASE_TOPICS.put("/m/0kt51", "Health");
        FREEBASE_TOPICS.put("/m/01h6rj", "Military");
        FREEBASE_TOPICS.put("/m/05qt0", "Politics");
        FREEBASE_TOPICS.put("/m/06bvp", "Religion");
        FREEBASE_TOPICS.put("/m/01k8wb", "Knowledge");
    }

    // @Autowired
    // public YouTubeReportPoi(WikipediaTopicRepository wikipediaTopicRepository) {
    //    this.wikipediaTopicRepository = wikipediaTopicRepository;
    //}

    Map<String, WikipediaTopic> listWikipediaTopics(Collection<Channel> channels) {
        final Map<String, WikipediaTopic> working = new HashMap<>();
        for (Channel channel : channels) {
            for (WikipediaTopic topic : channel.getTopicCategories()) {
                if (!working.containsKey(topic.getLabel())) {
                    working.put(topic.getLabel(), topic);
                }
            }
        }

        final Map<String, WikipediaTopic> wikipediaTopics = new LinkedHashMap<>();
        final List<String> keys = new ArrayList<>(working.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            wikipediaTopics.put(key, working.get(key));
        }

        return wikipediaTopics;
    }

    @Override
    public void listChannels(String filename, Collection<Channel> channels) throws IOException {

        List<Channel> channelList = new ArrayList<>(channels);
        Collections.sort(channelList, (s, p) -> s.getTitle().compareTo(p.getTitle()));

        final Map<String, WikipediaTopic> wikipediaTopics = listWikipediaTopics(channels);

        final List<String> freebaseTopics = new ArrayList<>();
        try (Writer w = new FileWriter(filename)) {
            try (PrintWriter pw = new PrintWriter(w)) {

                for (String label : wikipediaTopics.keySet()) {
                    final WikipediaTopic wikipediaTopic = wikipediaTopics.get(label);
                    pw.printf("%s\n", label);
                    for (Channel channel : channelList) {
                        if (channel.getTopicCategories().contains(wikipediaTopic)) {
                            freebaseTopics.clear();
                            for (String k: channel.getTopicIds()) {
                                if (FREEBASE_TOPICS.containsKey(k)) {
                                    freebaseTopics.add(FREEBASE_TOPICS.get(k));
                                }
                            }
                            Collections.sort(freebaseTopics);
                            pw.printf("%s, %s, %s, [%s]\n", channel.getChannelId(), channel.getHandle(), channel.getTitle(),
                                    String.join(",", freebaseTopics));
                        }
                    }
                    pw.println();
                    pw.println();
                }
            }
        }
    }
}
