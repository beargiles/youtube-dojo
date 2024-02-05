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
package com.coyotesong.dojo.youtube.service;

import com.coyotesong.dojo.youtube.model.Channel;
import com.coyotesong.dojo.youtube.security.LogSanitizer;
import com.coyotesong.dojo.youtube.service.youtubeClient.ClientForChannelListFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of YouTubeChannelsService
 */
@Service
public class YouTubeSubscriptionsServiceImpl implements YouTubeChannelsService {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeSubscriptionsServiceImpl.class);

    private final ClientForChannelListFactory clientForChannelListFactory;
    private final LogSanitizer sanitize;

    @Autowired
    public YouTubeSubscriptionsServiceImpl(@NotNull ClientForChannelListFactory clientForChannelListFactory,
                                           @NotNull LogSanitizer logSanitizer) {
        this.clientForChannelListFactory = clientForChannelListFactory;
        this.sanitize = logSanitizer;
    }

    /**
     * Get retrieve information about channels owned by a specific user
     *
     * @param username - channel owner
     * @return requested channels (when available)
     */
    @Nullable
    public Channel getChannelForUser(String username) throws IOException {
        LOG.trace("call to getChannelForUser({})...", sanitize.forUsername(username));
        final ClientForChannelListFactory.ClientForChannelList client = clientForChannelListFactory.newBuilder().withForUsername(username).build();
        final List<Channel> channels = client.next();
        if (!channels.isEmpty()) {
            final Channel channel = channels.get(0);
            LOG.trace("getChannelForUser('{}') -> {}", sanitize.forUsername(username), sanitize.forChannelId(channel.getId()));
            return channel;
        }

        LOG.info("getChannelForUser('{}') -> null", sanitize.forUsername(username));
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
        LOG.trace("call to getChannel('{}')...", sanitize.forChannelId(id));
        final List<Channel> channels = getChannels(Collections.singletonList(id));
        if (channels.isEmpty()) {
            LOG.info("getChannel('{}') -> null", id);
        }
        return channels.isEmpty() ? null : channels.get(0);
    }

    /**
     * Retrieve information about specified channels
     * <p>
     * Implementation note: caching is handled programmatically
     * </p>
     *
     * @param ids - channels to load
     * @return requested channels (when available)
     * @throws IOException (often GoogleJsonResponseException)
     */
    @Override
    @NotNull
    public List<Channel> getChannels(List<String> ids) throws IOException {
        LOG.trace("call to getChannels()...");

        // check for cached values
        final List<Channel> channels = new ArrayList<>();

        // make REST call for remaining values.
        // TODO - should handle this in underlying service...
        for (int offset = 0; offset < ids.size(); offset += 50) {
            final List<String> list = ids.subList(offset, Math.min(offset + 50, ids.size()));
            final ClientForChannelListFactory.ClientForChannelList client = clientForChannelListFactory.newBuilder().withIds(list).build();
            while (client.hasNext()) {
                channels.addAll(client.next());
            }
        }
        LOG.trace("found {} match(es)", channels.size());

        return channels;
    }
}
