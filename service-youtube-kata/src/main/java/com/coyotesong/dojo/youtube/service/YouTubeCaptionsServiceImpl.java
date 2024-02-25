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

import com.coyotesong.dojo.youtube.model.Caption;
import com.coyotesong.dojo.youtube.security.LogSanitizer;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForCaptionListFactory;
import com.coyotesong.dojo.youtube.service.youTubeClient.YouTubeClient.ListCaptions;
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
 * Implementation of YouTubeCaptionsService
 */
@Service("YouTubeCaptionsService")
public class YouTubeCaptionsServiceImpl implements YouTubeCaptionsService {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeCaptionsServiceImpl.class);

    private final ClientForCaptionListFactory clientForCaptionListFactory;
    private final LogSanitizer sanitize;

    @Autowired
    public YouTubeCaptionsServiceImpl(@NotNull ClientForCaptionListFactory clientForCaptionListFactory,
                                      LogSanitizer sanitize) {
        this.clientForCaptionListFactory = clientForCaptionListFactory;
        this.sanitize = sanitize;
    }

    /**
     * Get captions for video
     *
     * @param videoId video id
     * @return requested captions (when available)
     */
    @Override
    @NotNull
    public List<Caption> getCaptionsForVideoId(@NotNull String videoId) throws IOException {
        if (isBlank(videoId)) {
            throw new IllegalArgumentException("'videoId' must not be blank");
        }

        LOG.trace("getCaptionForVideoId({})...", sanitize.forVideoId(videoId));

        // check for cached values
        final List<Caption> captions = new ArrayList<>();

        final ListCaptions client = clientForCaptionListFactory.newBuilder().withVideoId(videoId).build();
        while (client.hasNext()) {
            captions.addAll(client.next());
        }

        LOG.trace("getCaptionsForVideoId({}) -> {} record(s)", sanitize.forVideoId(videoId), captions.size());
        return captions;
    }
}
