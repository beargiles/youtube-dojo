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

import com.coyotesong.dojo.youtube.model.I18nRegion;
import com.coyotesong.dojo.youtube.security.LogSanitizer;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForI18nRegionListFactory;
import com.coyotesong.dojo.youtube.service.youTubeClient.YouTubeClient.ListI18nRegions;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

// see https://googleapis.dev/java/google-api-services-youtube/latest/com/google/api/services/youtube/YouTube.html

/**
 * Implementation of YouTubeI18nRegionsService
 */
@Service("YouTubeI18nRegionsService")
public class YouTubeI18NRegionServiceImpl implements YouTubeI18nRegionService {

    private final ClientForI18nRegionListFactory clientForI18nRegionListFactory;
    @SuppressWarnings("unused")
    private final LogSanitizer sanitize;

    @Autowired
    public YouTubeI18NRegionServiceImpl(@NotNull ClientForI18nRegionListFactory clientForI18nRegionListFactory,
                                        @NotNull LogSanitizer sanitize) {
        this.clientForI18nRegionListFactory = clientForI18nRegionListFactory;
        this.sanitize = sanitize;
    }

    /**
     * Get retrieve information about specified i18n regions
     *
     * @param hl - language or language + country
     * @return requested regions (when available)
     */
    @Override
    @NotNull
    public List<I18nRegion> getI18nRegions(@NotNull String hl) throws IOException {
        if (isBlank(hl)) {
            throw new IllegalArgumentException("'hl' must not be blank");
        }

        final List<I18nRegion> regions = new ArrayList<>();

        final ListI18nRegions client = clientForI18nRegionListFactory.newBuilder().withHl(hl).build();
        while (client.hasNext()) {
            regions.addAll(client.next());
        }

        // this isn't returned by the API.  (Perhaps we should use gl?)
        regions.parallelStream().forEach(r -> r.setHl(hl));

        return regions;
    }
}
