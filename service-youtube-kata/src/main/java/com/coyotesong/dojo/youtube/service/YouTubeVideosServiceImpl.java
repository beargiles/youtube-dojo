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

import com.coyotesong.dojo.youtube.model.Video;
import com.coyotesong.dojo.youtube.security.LogSanitizer;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForVideoListFactory;
import com.coyotesong.dojo.youtube.service.youTubeClient.YouTubeClient.ListVideos;
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
 * Implementation of YouTubeVideosService
 */
@Service("YouTubeVideosService")
public class YouTubeVideosServiceImpl implements YouTubeVideosService {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeVideosServiceImpl.class);

    private final ClientForVideoListFactory clientForVideoListFactory;
    private final LogSanitizer sanitize;

    @Autowired
    public YouTubeVideosServiceImpl(@NotNull ClientForVideoListFactory clientForVideoListFactory,
                                    LogSanitizer sanitize) {
        this.clientForVideoListFactory = clientForVideoListFactory;
        this.sanitize = sanitize;
    }

    /**
     * Retrieve information about video
     *
     * @param id video to load
     * @return
     * @throws IOException
     */
    @Override
    @Nullable
    public Video getVideo(@NotNull String id) throws IOException {
        if (isBlank(id)) {
            throw new IllegalArgumentException("'id' may not be blank");
        }

        LOG.trace("getVideo('{}')...", sanitize.forVideoId(id));
        final List<Video> videos = getVideos(Collections.singletonList(id));
        if (!videos.isEmpty()) {
            LOG.trace("getVideo('{}') -> '{}'", sanitize.forVideoId(id),
                    sanitize.forString(videos.get(0).getTitle()));
            return videos.get(0);
        }

        LOG.trace("getVideo('{}') -> null", sanitize.forChannelId(id));
        return null;
    }

    /**
     * Get retrieve information about specified videos
     *
     * @param ids - videos to load
     * @return requested videos (when available)
     */
    @Override
    @NotNull
    public List<Video> getVideos(@NotNull @Unmodifiable List<String> ids) throws IOException {
        if (ids == null) {
            throw new IllegalArgumentException("'ids' may null");
        } else if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        // TODO: check if list contains nulls or blanks

        LOG.trace("getVideos()...");

        // check for cached values
        final List<Video> videos = new ArrayList<>();

        // make REST call for remaining values.
        if (!ids.isEmpty()) {
            // TODO - should handle this in underlying service...
            for (int offset = 0; offset < ids.size(); offset += 50) {
                final List<String> list = ids.subList(offset, Math.min(offset + 50, ids.size()));
                final ListVideos client = clientForVideoListFactory.newBuilder().withIds(list).build();
                while (client.hasNext()) {
                    videos.addAll(client.next());
                }
            }
        }

        LOG.trace("getVideos({}) -> {} record(s)", ids.size(), videos.size());
        return videos;
    }
}
