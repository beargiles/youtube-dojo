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

import com.coyotesong.dojo.youtube.model.YouTubeApiCacheEntry;
import com.coyotesong.dojo.youtube.repository.YouTubeApiCacheRepository;
import com.coyotesong.dojo.youtube.repository.jooq.generatedCache.tables.records.YoutubeApiCacheRecord;
import org.jooq.*;
import org.jooq.impl.DAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.coyotesong.dojo.youtube.repository.jooq.generatedCache.tables.YoutubeApiCache.YOUTUBE_API_CACHE;

@Repository
public class YouTubeApiCacheRepositoryJooq extends DAOImpl<YoutubeApiCacheRecord, YouTubeApiCacheEntry, Integer> implements YouTubeApiCacheRepository {

    @Autowired
    public YouTubeApiCacheRepositoryJooq(Configuration configuration) {
        super(YOUTUBE_API_CACHE, YouTubeApiCacheEntry.class, configuration);
    }

    public Integer getId(final YouTubeApiCacheEntry entry) {
        return entry.getKey();
    }

    @Override
    public Optional<YouTubeApiCacheEntry> findOptionalByRequestJson(String requestJson) {
        return ctx()
                .selectFrom(YOUTUBE_API_CACHE)
                .where(YOUTUBE_API_CACHE.REQUEST_JSON.cast(String.class).eq(requestJson))
                .fetchOptionalInto(YouTubeApiCacheEntry.class);
    }
}
