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

import com.coyotesong.dojo.youtube.model.PlaylistImage;
import com.coyotesong.dojo.youtube.security.LogSanitizer;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForPlaylistImageListFactory;
import com.coyotesong.dojo.youtube.service.youTubeClient.YouTubeClient.ListPlaylistImages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

// see https://googleapis.dev/java/google-api-services-youtube/latest/com/google/api/services/youtube/YouTube.html

/**
 * Implementation of YouTubeService
 */
@Service("YouTubePlaylistImages")
public class YouTubePlaylistImagesServiceImpl implements YouTubePlaylistImagesService {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubePlaylistImagesServiceImpl.class);

    private final ClientForPlaylistImageListFactory clientForPlaylistImageListFactory;
    private final LogSanitizer sanitize;

    @Autowired
    public YouTubePlaylistImagesServiceImpl(@NotNull ClientForPlaylistImageListFactory clientForPlaylistImageListFactory,
                                            @NotNull LogSanitizer sanitize) {
        this.clientForPlaylistImageListFactory = clientForPlaylistImageListFactory;
        this.sanitize = sanitize;
    }

    /**
     * @param playlistId playlistImage to load
     * @return
     * @throws IOException
     */
    @Override
    @Nullable
    public PlaylistImage getPlaylistImageForPlaylistId(@NotNull String playlistId) throws IOException {
        if (isBlank(playlistId)) {
            throw new IllegalArgumentException("'playlistId' must not be blank");
        }

        LOG.trace("getPlaylistImageForPlaylistId('{}')...", sanitize.forPlaylistId(playlistId));

        final List<PlaylistImage> images = new ArrayList<>();
        final ListPlaylistImages client = clientForPlaylistImageListFactory.newBuilder().withPlaylistId(playlistId).build();
        while (client.hasNext()) {
            images.addAll(client.next());
        }

        if (!images.isEmpty()) {
            final PlaylistImage image = images.get(0);
            LOG.trace("getPlaylistImageForPlaylistId('{}') -> '{}'", sanitize.forPlaylistId(playlistId), sanitize.forString(image.getType()));
            return image;
        }

        LOG.trace("getPlaylistImageForPlaylistId('{}') -> null", sanitize.forPlaylistId(playlistId));
        return null;
    }
}
