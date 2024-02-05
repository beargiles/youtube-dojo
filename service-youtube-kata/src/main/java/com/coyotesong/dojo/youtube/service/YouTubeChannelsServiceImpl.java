/*
 * Copyright (c) 2023 Bear Giles <bgiles@coyotesong.com>.
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

import com.coyotesong.tabs.cache.ChannelCache;
import com.coyotesong.tabs.cache.LruCache;
import com.coyotesong.tabs.model.Channel;
import com.coyotesong.tabs.repo.ChannelRepository;
import com.coyotesong.tabs.security.LogSanitizer;
import com.coyotesong.tabs.service.youtubeClient.ClientForChannelList;
import com.coyotesong.tabs.service.youtubeClient.ClientForChannelListFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of YouTubeChannelsService
 */
@Service
@CacheConfig(cacheNames = ChannelCache.NAME)
public class YouTubeChannelsServiceImpl implements YouTubeChannelsService {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeChannelsServiceImpl.class);

    private final ClientForChannelListFactory clientForChannelListFactory;
    private final ChannelCache channelCache;
    private final LogSanitizer sanitize;
    private final ChannelRepository channelDao;

    @Autowired
    public YouTubeChannelsServiceImpl(@NotNull LogSanitizer logSanitizer, @NotNull ChannelCache channelCache,
                                      @NotNull ClientForChannelListFactory clientForChannelListFactory,
                                      @NotNull ChannelRepository channelDao) {
        this.sanitize = logSanitizer;
        this.channelCache = channelCache;
        this.clientForChannelListFactory = clientForChannelListFactory;
        this.channelDao = channelDao;
    }

    /**
     * Get retrieve information about channels owned by a specific user
     *
     * @param username - channel owner
     * @return requested channels (when available)
     */
    @Nullable
    public Channel getChannelForUser(String username) throws IOException {
        LOG.info("call to getChannelForUser({})...", sanitize.forUsername(username));
        final ClientForChannelList client = clientForChannelListFactory.newBuilder().withForUsername(username).build();
        final List<Channel> channels = client.next();
        if (!channels.isEmpty()) {
            final Channel channel = channels.get(0);
            LOG.info("getChannel('{}') -> {}", sanitize.forUsername(username), sanitize.forChannel(channel));
            channelCache.putIfAbsent(channel.getId(), channel);
            return channel;
        }

        LOG.info("getChannel('{}') -> null", sanitize.forUsername(username));
        return null;
    }

    /**
     * Retrieve information about specified channel
     *
     * FIXME - '@Cacheable' disabled since it's hanging when the value isn't in the cache.
     *
     * @param id - channel to load
     * @return requested channel (when available)
     */
    // @Cacheable
    @Nullable
    public Channel getChannel(@NotNull String id) throws IOException {
        return getChannels(Collections.singletonList(id)).get(0);
    }

    /**
     * Retrieve information about specified channels
     * <p>
     * Implementation note: caching is handled programmatically
     * </p>
     *
     * @param ids - channels to load
     * @return requested channels (when available)
     */
    @Override
    @NotNull
    public List<Channel> getChannels(List<String> ids) throws IOException {
        LOG.info("call to getChannels()...");

        // check for cached values
        final List<Channel> channels = new ArrayList<>();
        final List<String> misses = new ArrayList<>();

        final Loader loader = new Loader(channelDao, misses);

        for (String id : ids) {
            final Channel channel = channelCache.get(id, loader);
            if (channel != null) {
                channels.add(channel);
            }
        }

        // make REST call for remaining values.
        if (!misses.isEmpty()) {
            final List<Channel> uncachedChannels = new ArrayList<>();
            // TODO - should handle this in underlying service...
            for (int offset = 0; offset < misses.size(); offset += 50) {
                final List<String> list = misses.subList(offset, Math.min(offset + 50, misses.size()));
                final ClientForChannelList client = clientForChannelListFactory.newBuilder().withIds(list).build();
                while (client.hasNext()) {
                    uncachedChannels.addAll(client.next());
                }
            }
            uncachedChannels.forEach(channel -> channelCache.put(channel.getId(), channel));
            channels.addAll(uncachedChannels);
        }

        return channels;
    }

    public static class Loader implements LruCache.LruCacheLoader<Channel> {
        private final ChannelRepository channelDao;
        private final ThreadLocal<String> key = new ThreadLocal<>();
        private final List<String> misses;

        public Loader(ChannelRepository channelDao, List<String> misses) {
            this.channelDao = channelDao;
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
        public Channel call() throws Exception {
            // LOG.info("call('{}')", key.get());
            final Optional<Channel> channel = channelDao.getById(key.get());

            if (channel.isEmpty()) {
                misses.add(key.get());
                return null;
            } else {
                return channel.get();
            }
        }
    }
}
