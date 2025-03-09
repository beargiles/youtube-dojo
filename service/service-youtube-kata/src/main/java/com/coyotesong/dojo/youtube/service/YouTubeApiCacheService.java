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

import com.coyotesong.dojo.youtube.cache.YouTubeApiCacheEntryFactory;
// import com.coyotesong.dojo.youtube.cache.YouTubeApiCachePersistence;
import com.coyotesong.dojo.youtube.cache.YouTubeApiCachePersistence;
import com.coyotesong.dojo.youtube.model.YouTubeApiCacheEntry;
import com.coyotesong.dojo.youtube.repository.YouTubeApiCacheRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.api.client.json.GenericJson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.ref.Cleaner;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Cache used by YouTube API implemented in order to reduce costs and improve
 * performance during routine testing.
 */
@Service
public class YouTubeApiCacheService extends ConcurrentMapCache implements AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeApiCacheService.class);

    private static final Cleaner cleaner = Cleaner.create();
    private final Cleaner.Cleanable cleanable;

    private final YouTubeApiCacheEntryFactory entryFactory;
    private final YouTubeApiCacheRepository repository;

    @Autowired
    public YouTubeApiCacheService(@NotNull YouTubeApiCacheRepository repository) {
        super("etag", false);
        this.repository = repository;

        // we don't need injection yet.
        this.entryFactory = new YouTubeApiCacheEntryFactory();

        this.cleanable = cleaner.register(this, new YouTubeApiCachePersistence(this, repository));
    }

    @SuppressWarnings("unused")
    static <T> TypeReference<List<T>> listOf(Class<T> ignored) {
        return new TypeReference<>(){};
    }

    void store(@NotNull Object key, @NotNull YouTubeApiCacheEntry entry) {
        getNativeCache().put(key, entry);
        repository.insert(entry);
    }

    @Override
    public void close() {
        // cleanable.clean();
    }

    @Override
    @Nullable
    public <T> T get(@NotNull Object key, @NotNull Callable<T> valueLoader) {
        LOG.info("get('{}')", key);
        try {
            Optional<YouTubeApiCacheEntry> dbEntry = repository.findOptionalByRequestJson((String) key);
            if (dbEntry.isPresent()) {
                LOG.info("found in database!");
                YouTubeApiCacheEntry e = dbEntry.get();
                LOG.info("found:\n{}\n", e);
                Object o = fromStoreValue(dbEntry.get());
                LOG.info("converted:\n{}\n", o);
                return (T) o;
            }

            LOG.info("calling service!");
            T t = valueLoader.call();
            final YouTubeApiCacheEntry entry = entryFactory.newInstance((GenericJson) t, key.toString());
            store(key, entry);
            return t;
        } catch (Exception e) {
            throw new Cache.ValueRetrievalException(key, valueLoader, e);
        }
    }

    @Override
    public ValueWrapper putIfAbsent(@NotNull Object key, @Nullable Object value) {
        LOG.info("putIfAbsent('{}', value)", key);
        if (value == null) {
            // we don't do anything with null values...
            return super.putIfAbsent(key, null);
        }

        LOG.info("also need to persist to database...");
        final YouTubeApiCacheEntry entry = entryFactory.newInstance((GenericJson) value, key.toString());
        store(key, entry);
        return super.putIfAbsent(key, entry);
    }

    @Override
    public void put(@NotNull Object key, @Nullable Object value) {
        if (value == null) {
            // we don't do anything with null values...
            super.put(key, null);
            return;
        }

        final YouTubeApiCacheEntry entry = entryFactory.newInstance((GenericJson) value, key.toString());
        store(key, entry);
        super.put(key, value);
    }

    /**
     * Convert the given user value, as passed into the put method, to a value in the internal store (adapting null)
     * @param userValue
     * @return
     */
    @Override
    @NotNull
    protected YouTubeApiCacheEntry toStoreValue(@Nullable Object userValue) {
        if (userValue == null) {
            return new YouTubeApiCacheEntry();
        }
        return entryFactory.newInstance((GenericJson) userValue);
    }

    /**
     * Convert the given value from the internal store to a user value returned from the get method (adapting null).
     * @param storeValue the store value
     * @return the value to return to the user
     */
    @Override
    @Nullable
    protected Object fromStoreValue(Object storeValue) {
        if (storeValue == null) {
            LOG.warn("Null values not supported!");
            return null;
        }

        if (storeValue instanceof YouTubeApiCacheEntry entry) {
            return entry.valueOf();
        }

        LOG.warn("Unrecognized class: {}", storeValue.getClass().getName());
        return null;
    }
}
