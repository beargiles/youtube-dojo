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

import com.coyotesong.dojo.youtube.data.TestChannels;
import com.coyotesong.dojo.youtube.model.Channel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Mock implementation of YouTubeChannelsService
 */
public class MockYouTubeChannelsServiceImpl implements YouTubeChannelsService {
    @Override
    public @Nullable Channel getChannel(@NotNull String id) throws IOException {
        if (TestChannels.CHANNEL.getChannelId().equals(id)) {
            return TestChannels.CHANNEL.getChannel();
        } else if (TestChannels.MISSING_CHANNEL.getChannelId().equals(id)) {
            return TestChannels.MISSING_CHANNEL.getChannel();
        }

        fail("unexpected channelId: " + id);
        return null;
    }

    @Override
    public @NotNull List<Channel> getChannels(@NotNull @Unmodifiable List<String> ids) throws IOException {
        return Collections.emptyList();
    }

    /**
     * Retrieve information about channel owned by a specific handle
     *
     * @param handle YouTube handle
     * @throws IOException a REST client issue problem occurred
     */
    @Override
    public @Nullable Channel getChannelForHandle(@NotNull String handle) throws IOException {
        return null;
    }

    /**
     * Retrieve information about channel owned by a specific user
     *
     * @param username YouTube username
     * @throws IOException a REST client issue problem occurred
     */
    @Override
    public @Nullable Channel getChannelForUsername(@NotNull String username) throws IOException {
        return null;
    }
}
