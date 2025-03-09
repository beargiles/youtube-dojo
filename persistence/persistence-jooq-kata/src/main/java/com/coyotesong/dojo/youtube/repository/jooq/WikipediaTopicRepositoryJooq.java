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

package com.coyotesong.dojo.youtube.repository.jooq;

import com.coyotesong.dojo.youtube.model.WikipediaTopic;
import com.coyotesong.dojo.youtube.repository.WikipediaTopicRepository;
import com.coyotesong.dojo.youtube.repository.jooq.generated.tables.records.WikipediaTopicRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.Configuration;
import org.jooq.Result;
import org.jooq.impl.DAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.coyotesong.dojo.youtube.repository.jooq.generated.tables.WikipediaTopic.WIKIPEDIA_TOPIC;

@Repository
public class WikipediaTopicRepositoryJooq extends DAOImpl<WikipediaTopicRecord, WikipediaTopic, Integer> implements WikipediaTopicRepository {

    @Autowired
    public WikipediaTopicRepositoryJooq(Configuration configuration) {
        super(WIKIPEDIA_TOPIC, WikipediaTopic.class, configuration);
    }

    @Override
    @NotNull
    public Integer getId(@NotNull WikipediaTopic topic) {
        return topic.getKey();
    }

    @Override
    public void delete() {
        // super.delete();
        ctx().deleteFrom(WIKIPEDIA_TOPIC).execute();
    }

    @Override
    public WikipediaTopic findByKey(int key) {
        return ctx().selectFrom(WIKIPEDIA_TOPIC).where(WIKIPEDIA_TOPIC.KEY.eq(key)).fetchSingleInto(WikipediaTopic.class);
    }

    @Override
    public WikipediaTopic findByUrl(URL url) {
        return ctx().selectFrom(WIKIPEDIA_TOPIC).where(WIKIPEDIA_TOPIC.URL.eq(url)).fetchSingleInto(WikipediaTopic.class);
    }

    public List<WikipediaTopic> loadTestDataFromClasspath(String classpath) throws IOException {
        final String csv = new ClassPathResource(classpath).getContentAsString(StandardCharsets.UTF_8);
        final Result<org.jooq.Record> result = ctx().fetchFromCSV(csv);

        final List<WikipediaTopic> topics = new ArrayList<>();
        for (org.jooq.Record record : result) {
            final WikipediaTopic topic = new WikipediaTopic();
            // topic.setId(record.get("id", Integer.class));
            topic.setKey(null);
            topic.setUrl(record.get("url", URL.class));
            topic.setLabel(record.get("label", String.class));
            topic.setCustom(record.get("custom", Boolean.class));
            topics.add(topic);
        }

        return topics;
    }
}
