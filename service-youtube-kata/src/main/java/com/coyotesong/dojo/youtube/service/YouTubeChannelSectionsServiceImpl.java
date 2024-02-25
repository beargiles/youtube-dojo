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
import com.coyotesong.dojo.youtube.security.LogSanitizer;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForChannelSectionListFactory;
import com.coyotesong.dojo.youtube.service.youTubeClient.YouTubeClient.ListChannelSections;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Implementation of YouTubeChannelSectionsService
 */
@Service("YouTubeChannelSectionsService")
public class YouTubeChannelSectionsServiceImpl implements YouTubeChannelSectionsService {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeChannelSectionsServiceImpl.class);

    private final ClientForChannelSectionListFactory clientForChannelSectionListFactory;
    private final LogSanitizer sanitize;

    @Autowired
    public YouTubeChannelSectionsServiceImpl(@NotNull ClientForChannelSectionListFactory clientForChannelSectionListFactory,
                                             @NotNull LogSanitizer sanitize) {
        this.clientForChannelSectionListFactory = clientForChannelSectionListFactory;
        this.sanitize = sanitize;
    }

    /**
     * Get retrieve information about channel sections for a specified channel
     *
     * @param channelId - channel to load
     * @return requested channel sections (when available)
     */
    @Override
    @NotNull
    public List<ChannelSection> getChannelSectionsForChannelId(@NotNull String channelId) throws IOException {
        if (isBlank(channelId)) {
            throw new IllegalArgumentException("'channelId' must not be blank");
        }

        LOG.trace("getChannelSectionsForChannelId('{}')...", sanitize.forChannelId(channelId));

        final List<ChannelSection> sections = new ArrayList<>();
        final ListChannelSections client = clientForChannelSectionListFactory.newBuilder().withChannelId(channelId).build();
        while (client.hasNext()) {
            sections.addAll(client.next());
        }

        LOG.trace("getChannelSectionsForChannelId({})... -> {} record(s)", sanitize.forChannelId(channelId), sections.size());
        return sections;
    }
}
