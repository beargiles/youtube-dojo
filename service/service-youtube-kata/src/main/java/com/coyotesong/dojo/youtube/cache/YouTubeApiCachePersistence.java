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

package com.coyotesong.dojo.youtube.cache;

import com.coyotesong.dojo.youtube.model.YouTubeApiCacheEntry;
import com.coyotesong.dojo.youtube.repository.YouTubeApiCacheRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import java.util.List;
import java.util.Map;

/**
 * Helper class that's used by the 'Cleaner' as the cache goes out of scope.
 * It could be used to flush the current contents to a database, etc.
 */
public class YouTubeApiCachePersistence implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeApiCachePersistence.class);
    private final Cache cache;
    private final YouTubeApiCacheRepository repository;

    public YouTubeApiCachePersistence(Cache cache, YouTubeApiCacheRepository repository) {
        this.cache = cache;
        this.repository = repository;
    }

    public void run() {
        final List<YouTubeApiCacheEntry> entries = repository.findAll();
        for (YouTubeApiCacheEntry entry : entries) {
            LOG.info("\n{}\n", entry);
        }

        /*
        if (cache.getNativeCache() instanceof Map) {
            // for now just write contents of native cache to the logger
            final StringBuilder sb = new StringBuilder();
            @SuppressWarnings("unchecked") final Map<Object, Object> nativeCache = (Map<Object, Object>) cache.getNativeCache();
            for (Map.Entry<Object, Object> entry : nativeCache.entrySet()) {
                sb.append(String.format("%-30.30s\n%s\n\n", entry.getKey(), entry.getValue()));
            }
            LOG.info("\n{}", sb);
        }
         */
    }
}
