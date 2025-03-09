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

// import com.coyotesong.dojo.youtube.cache.YouTubeApiCache;
import com.coyotesong.dojo.youtube.model.YouTubeApiCacheEntry;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Database repository to maintain the YouTube API cache between runs.
 */
public interface YouTubeApiCacheRepository {

    List<YouTubeApiCacheEntry> findAll();

    Optional<YouTubeApiCacheEntry> findOptionalByRequestJson(String requestJson);

    // YouTubeApiCache getById(String id);

    void insert(YouTubeApiCacheEntry entry);

    void insert(Collection<YouTubeApiCacheEntry> entries);
}
