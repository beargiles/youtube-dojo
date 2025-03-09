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

import com.coyotesong.dojo.youtube.model.Channel;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Mock repository
 */
@TestComponent
public class MockChannelRepositoryImpl implements ChannelRepository {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(MockChannelRepositoryImpl.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final JavaType LIST_OF_CHANNELS_TYPE = MAPPER.getTypeFactory().
            constructCollectionType(List.class, Channel.class);

    private final Map<String, Channel> cache = new LinkedHashMap<>();
    private final Lock lock = new ReentrantLock();

    public MockChannelRepositoryImpl() throws IOException {
        reload();
    }

    /**
     * Reload the 'channels' cache
     *
     * @throws IOException
     */
    final void reload() throws IOException {
        final Resource resource = new ClassPathResource("/testdata/channels.json");
        try (InputStream is = resource.getInputStream()) {
            DataInput di = new DataInputStream(is);

            /*
            final List<Channel> list = MAPPER.readValue(di, LIST_OF_CHANNELS_TYPE);
            lock.lock();
            try {
                cache.clear();
                list.forEach(ch -> cache.put(ch.getId(), ch));
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
    public void deleteById(String channelId) {
        lock.lock();
        try {
            cache.remove(channelId);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean existsById(String channelId) {
        lock.lock();
        try {
            return cache.containsKey(channelId);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public @NotNull List<Channel> findAll() {
        lock.lock();
        try {
            // create protective copy
            return new ArrayList<>(cache.values());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public @Nullable Channel findById(String channelId) {
        lock.lock();
        try {
            return cache.get(channelId);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Save channel details
     *
     * @param channels
     */
    @Override
    public void insert(Collection<Channel> channels) {
        lock.lock();
        try {
            // FIXME - add exception if id already exists!
            channels.forEach(ch -> this.cache.put(ch.getChannelId(), ch));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void insert(Channel channel) {
        lock.lock();
        try {
            // FIXME - add exception if id already exists!
            cache.put(channel.getChannelId(), channel);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void merge(Channel channel) {
        lock.lock();
        try {
            cache.put(channel.getChannelId(), channel);
        } finally {
            lock.unlock();
        }
    }
}
