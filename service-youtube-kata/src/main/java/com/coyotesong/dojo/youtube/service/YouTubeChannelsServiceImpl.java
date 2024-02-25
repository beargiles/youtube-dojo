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

import com.coyotesong.dojo.youtube.model.Channel;
import com.coyotesong.dojo.youtube.security.LogSanitizer;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForChannelListFactory;
import com.coyotesong.dojo.youtube.service.youTubeClient.YouTubeClient.ListChannels;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Implementation of YouTubeChannelsService
 */
@Service("YouTubeChannelsService")
public class YouTubeChannelsServiceImpl implements YouTubeChannelsService {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeChannelsServiceImpl.class);

    private final ClientForChannelListFactory clientForChannelListFactory;
    private final LogSanitizer sanitize;

    @Autowired
    public YouTubeChannelsServiceImpl(@NotNull ClientForChannelListFactory clientForChannelListFactory,
                                      @NotNull LogSanitizer sanitize) {
        this.clientForChannelListFactory = clientForChannelListFactory;
        this.sanitize = sanitize;
    }

    /**
     * Get retrieve information about channels owned by a specific handle
     *
     * @param handle - handle
     * @return requested channels (when available)
     */
    @Override
    @Nullable
    public Channel getChannelForHandle(@NotNull String handle) throws IOException {
        if (isBlank(handle)) {
            throw new IllegalArgumentException("'handle' must not be null");
        }

        LOG.trace("getChannelForHandle('{}')...", sanitize.forHandle(handle));
        final ListChannels client = clientForChannelListFactory.newBuilder().withForHandle(handle).build();
        final List<Channel> channels = client.next();
        if (!channels.isEmpty()) {
            final Channel channel = channels.get(0);
            LOG.trace("getChannelForHandle('{}') -> '{}'", sanitize.forHandle(handle), sanitize.forString(channel.getTitle()));
            return channel;
        }

        LOG.trace("getChannelForHandle('{}') -> null", sanitize.forHandle(handle));
        return null;
    }

    /**
     * Get retrieve information about channels owned by a specific user
     *
     * @param username - channel owner
     * @return requested channels (when available)
     */
    @Override
    @Nullable
    public Channel getChannelForUsername(@NotNull String username) throws IOException {
        if (isBlank(username)) {
            throw new IllegalArgumentException("'username' must not be null");
        }

        LOG.trace("getChannelForUsername('{}')...", sanitize.forUsername(username));
        final ListChannels client = clientForChannelListFactory.newBuilder().withForUsername(username).build();
        final List<Channel> channels = client.next();
        if (!channels.isEmpty()) {
            final Channel channel = channels.get(0);
            LOG.trace("getChannelForUsername('{}') -> '{}'", sanitize.forUsername(username), sanitize.forString(channel.getTitle()));
            return channel;
        }

        LOG.trace("getChannelForUsername('{}') -> null", sanitize.forUsername(username));
        return null;
    }

    /**
     * Retrieve information about specified channel
     *
     * @param id - channel to load
     * @return requested channel (when available)
     */
    @Override
    @Nullable
    public Channel getChannel(@NotNull String id) throws IOException {
        if (isBlank(id)) {
            throw new IllegalArgumentException("'id' must not be null");
        }

        LOG.trace("getChannel('{}')...", sanitize.forChannelId(id));
        final List<Channel> channels = getChannels(Collections.singletonList(id));
        if (!channels.isEmpty()) {
            final Channel channel = channels.get(0);
            LOG.trace("getChannel('{}') -> '{}'", sanitize.forChannelId(id), sanitize.forString(channel.getTitle()));
            return channel;
        }

        LOG.trace("getChannel('{}') -> null", sanitize.forChannelId(id));
        return null;
    }

    /**
     * Retrieve information about specified channels
     *
     * @param ids - channels to load
     * @return requested channels (when available)
     */
    @Override
    @NotNull
    public List<Channel> getChannels(@NotNull @Unmodifiable List<String> ids) throws IOException {
        if (ids == null) {
            throw new IllegalArgumentException("'ids' must not be null");
        } else if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        // TODO: check for blank elements

        LOG.trace("getChannels()...");

        final List<Channel> channels = new ArrayList<>();

        // TODO - should handle this in underlying service...
        for (int offset = 0; offset < ids.size(); offset += 50) {
            final List<String> list = ids.subList(offset, Math.min(offset + 50, ids.size()));
            final ListChannels client = clientForChannelListFactory.newBuilder().withIds(list).build();
            while (client.hasNext()) {
                channels.addAll(client.next());
            }
        }

        LOG.trace("getChannels({})... -> {} record(s)", ids.size(), channels.size());
        return channels;
    }
}
