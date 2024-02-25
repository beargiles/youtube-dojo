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

import com.coyotesong.dojo.youtube.model.ChannelSection;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * YouTube 'channelSections' API client
 * <p>
 * see <a href="https://googleapis.dev/java/google-api-services-youtube/latest/index.html?com/google/api/services/youtube/YouTube.ChannelSections.List.html">YouTube.ChannelSections.List</a>
 */
@SuppressWarnings("unused")
public interface YouTubeChannelSectionsService {

    /**
     * Retrieve information about specified channel
     *
     * @param channelId - channel to load
     * @return requested channel sections (when available)
     * @throws IOException a REST client issue problem occurred
     */
    @NotNull
    List<ChannelSection> getChannelSectionsForChannelId(@NotNull String channelId) throws IOException;

    // also: id (stubby / apiary)

}
