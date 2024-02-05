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
package com.coyotesong.tabs.repo;

import com.coyotesong.tabs.model.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Persistence mechanism for YouTube channels
 */
public interface ChannelRepository {

    /**
     * List all channel ids
     *
     * @return
     */
    @NotNull
    List<String> listChannelIds();

    /**
     * Save channel details
     * @param channels
     */
    void save(Collection<Channel> channels);

    void save(Channel channel);

    @NotNull
    List<Channel> findAll();

    void deleteById(String id);

    Optional<Channel> getByCustomUrl(String customUrl);

    Optional<Channel> getById(String channelId);
}
