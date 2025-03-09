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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.util.List;

/**
 * YouTube 'channels' API client
 *
 * Other options:
 * - CategoryId
 * - HL/Locale
 * - ManagedByMe
 * - Mine
 * - MySubscribers
 *
 * The 'OnBehalfOfContentOwner' is intended for use by YouTube content partners.
 *
 * See [YouTube.Channels.List](https://googleapis.dev/java/google-api-services-youtube/latest/index.html?com/google/api/services/youtube/YouTube.Channels.List.html)
 */
@SuppressWarnings("unused")
public interface YouTubeChannelsService {

    /**
     * Retrieve information about specified channel
     *
     * @param id - channel to load
     * @return requested channel (when available)
     * @throws IOException a REST client issue problem occurred
     */
    @Nullable
    Channel getChannel(@NotNull String id) throws IOException;

    /**
     * Retrieve information about specified channels
     *
     * @param ids - channels to load
     * @return requested channels (when available)
     * @throws IOException a REST client issue problem occurred
     */
    @NotNull
    List<Channel> getChannels(@NotNull @Unmodifiable List<String> ids) throws IOException;

    /**
     * Retrieve information about channel owned by a specific handle,
     * e.g., 'GoogleDevelopers' or '@GoogleDevelopers'.
     *
     * @param handle YouTube handle
     * @throws IOException a REST client issue problem occurred
     */
    @Nullable
    Channel getChannelForHandle(@NotNull String handle) throws IOException;

    /**
     * Retrieve information about channel owned by a specific user
     *
     * @param username YouTube username
     * @throws IOException a REST client issue problem occurred
     */
    @Nullable
    Channel getChannelForUsername(@NotNull String username) throws IOException;
}
