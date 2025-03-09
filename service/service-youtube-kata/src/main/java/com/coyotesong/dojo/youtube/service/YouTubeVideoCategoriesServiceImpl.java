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

import com.coyotesong.dojo.youtube.model.VideoCategory;
import com.coyotesong.dojo.youtube.security.LogSanitizer;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForVideoCategoryListFactory;
import com.coyotesong.dojo.youtube.service.youTubeClient.YouTubeClient.ListVideoCategories;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

// see https://googleapis.dev/java/google-api-services-youtube/latest/com/google/api/services/youtube/YouTube.html

/**
 * Implementation of YouTubeVideoCategoriesService
 */
@Service("YouTubeVideoCategoriesService")
public class YouTubeVideoCategoriesServiceImpl implements YouTubeVideoCategoriesService {

    private final ClientForVideoCategoryListFactory clientForVideoCategoryListFactory;
    @SuppressWarnings("unused")
    private final LogSanitizer sanitize;

    @Autowired
    public YouTubeVideoCategoriesServiceImpl(@NotNull ClientForVideoCategoryListFactory clientForVideoCategoryListFactory,
                                             @NotNull LogSanitizer sanitize) {
        this.clientForVideoCategoryListFactory = clientForVideoCategoryListFactory;
        this.sanitize = sanitize;
    }

    /**
     * Get retrieve information about specified video categories
     *
     * @param hl - language or language + country
     * @return requested video categories (when available)
     */
    @Override
    @NotNull
    public List<VideoCategory> getVideoCategories(@NotNull String hl) throws IOException {
        if (isBlank(hl)) {
            throw new IllegalArgumentException("'hl' must not be blank");
        }

        final List<VideoCategory> categories = new ArrayList<>();

        final List<String> ids = new ArrayList<>();

        // I know there's gaps up to 17, and nothing after 44.
        ids.add("1");
        ids.add("2");
        ids.add("10");
        ids.add("15");

        for (int i = 17; i <= 44; i++) {
            ids.add("" + i);
        }

        final ListVideoCategories client = clientForVideoCategoryListFactory.newBuilder().withIds(ids).withHl(hl).build();
        while (client.hasNext()) {
            categories.addAll(client.next());
        }

        return categories;
    }
}
