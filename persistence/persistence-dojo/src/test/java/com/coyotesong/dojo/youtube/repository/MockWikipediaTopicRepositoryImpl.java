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

import com.coyotesong.dojo.youtube.model.WikipediaTopic;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Mock repository
 */
@TestComponent
public class MockWikipediaTopicRepositoryImpl implements WikipediaTopicRepository {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(MockChannelRepositoryImpl.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final JavaType LIST_OF_WIKIPEDIA_TOPICS_TYPE = MAPPER.getTypeFactory().
            constructCollectionType(List.class, WikipediaTopic.class);

    private final Map<Integer, WikipediaTopic> cache = new LinkedHashMap<>();
    private final AtomicInteger counter = new AtomicInteger();
    private final Lock lock = new ReentrantLock();

    public MockWikipediaTopicRepositoryImpl() throws IOException {
        reload();
    }

    /**
     * Reload the 'channels' cache
     *
     * @throws IOException
     */
    final void reload() throws IOException {
        final Resource resource = new ClassPathResource("/testdata/wikipedia-topics.json");
        try (InputStream is = resource.getInputStream()) {
            DataInput di = new DataInputStream(is);

            /*
            final List<WikipediaTopic> list = MAPPER.readValue(di, LIST_OF_WIKIPEDIA_TOPICS_TYPE);
            lock.lock();
            try {
                cache.clear();
                list.forEach(topic -> cache.put(topic.getId(), topic));
            } finally {
                lock.unlock();
            }
             */
        }
    }

    @Override
    public long count() {
        return cache.size();
    }

    @Override
    public void delete() {
        lock.lock();
        try {
            cache.clear();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void insert(WikipediaTopic topic) {
        lock.lock();
        try {
            // TODO: check for duplicates
            topic.setKey(counter.getAndIncrement());
            cache.put(topic.getKey(), topic);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void insert(Collection<WikipediaTopic> topics) {
        lock.lock();
        try {
            // TODO: check for duplicates
            for (WikipediaTopic topic : topics) {
                topic.setKey(counter.getAndIncrement());
                cache.put(topic.getKey(), topic);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * List all topic categories
     *
     * @return
     */
    @Override
    public List<WikipediaTopic> findAll() {
        lock.lock();
        try {
            // create protective copy
            return new ArrayList<>(cache.values());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public WikipediaTopic findByKey(int id) {
        lock.lock();
        try {
            return cache.get(id);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public WikipediaTopic findByUrl(URL url) {
        lock.lock();
        try {
            final String expectedUrl = url.toExternalForm();
            final Optional<WikipediaTopic> topic = cache.values().stream().filter(s -> expectedUrl.equals(s.getUrl().toExternalForm())).findAny();
            return topic.orElse(null);
        } finally {
            lock.unlock();
        }
    }
}
