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

package com.coyotesong.tabs.service.service;

import com.coyotesong.tabs.cache.LruCache;
import com.coyotesong.tabs.cache.VideoCache;
import com.coyotesong.tabs.model.Video;
import com.coyotesong.tabs.repo.VideoRepository;
import com.coyotesong.tabs.security.LogSanitizer;
import com.coyotesong.tabs.service.youtubeClient.ClientForVideoList;
import com.coyotesong.tabs.service.youtubeClient.ClientForVideoListFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@CacheConfig(cacheNames = VideoCache.NAME)
public class YouTubeVideosServiceImpl implements YouTubeVideosService {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeVideosServiceImpl.class);

    private final VideoCache videoCache;
    private final VideoRepository videoDao;

    private final ClientForVideoListFactory clientForVideoListFactory;
    private final LogSanitizer sanitize;

    @Autowired
    public YouTubeVideosServiceImpl(LogSanitizer sanitize, VideoCache videoCache,
                                      @NotNull ClientForVideoListFactory clientForVideoListFactory,
                                      @NotNull VideoRepository videoDao) {
        this.sanitize = sanitize;
        this.videoCache = videoCache;
        this.clientForVideoListFactory = clientForVideoListFactory;
        this.videoDao = videoDao;
    }

    /**
     * Retrieve information about video
     *
     * @param id video to load
     * @return
     * @throws IOException
     */
    @Override
    public Video getVideo(String id) throws IOException {
        return getVideos(Collections.singletonList(id)).get(0);
    }

    /**
     * Get retrieve information about specified videos
     *
     * @param ids - videos to load
     * @return requested videos (when available)
     */
    public List<Video> getVideos(List<String> ids) throws IOException {
        LOG.info("call to getVideos()...");

        // check for cached values
        final List<Video> videos = new ArrayList<>();
        final List<String> misses = new ArrayList<>();

        final Loader loader = new Loader(videoDao, misses);

        for (String id : ids) {
            final Video video = videoCache.get(id, loader);
            if (video != null) {
                videos.add(video);
            }
        }

        // make REST call for remaining values.
        if (!misses.isEmpty()) {
            final List<Video> uncachedVideos = new ArrayList<>();
            // TODO - should handle this in underlying service...
            for (int offset = 0; offset < misses.size(); offset += 50) {
                final List<String> list = misses.subList(offset, Math.min(offset + 50, misses.size()));
                final ClientForVideoList client = clientForVideoListFactory.newBuilder().withIds(list).build();
                while (client.hasNext()) {
                    uncachedVideos.addAll(client.next());
                }
            }
            uncachedVideos.forEach(video -> videoCache.put(video.getId(), video));
            videos.addAll(uncachedVideos);
        }

        return videos;
    }

    public static class Loader implements LruCache.LruCacheLoader<Video> {
        private final VideoRepository videoDao;
        private final ThreadLocal<String> key = new ThreadLocal<>();
        private final List<String> misses;

        public Loader(VideoRepository videoDao, List<String> misses) {
            this.videoDao = videoDao;
            this.misses = misses;
        }

        public void setKey(String key) {
            this.key.set(key);
        }

        /**
         * @return
         * @throws Exception
         */
        @Override
        public Video call() throws Exception {
            // LOG.info("call('{}')", key.get());
            final Optional<Video> video = videoDao.getById(key.get());

            if (video.isEmpty()) {
                misses.add(key.get());
                return null;
            } else {
                return video.get();
            }
        }
    }
}
