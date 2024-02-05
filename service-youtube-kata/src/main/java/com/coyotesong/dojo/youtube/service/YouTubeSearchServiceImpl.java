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
package com.coyotesong.tabs.service.service;

import com.coyotesong.tabs.form.YouTubeSearchForm;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Implementation of YouTubeSearchService
 */
@Service
@CacheConfig(cacheNames = "searches" )
public class YouTubeSearchServiceImpl implements YouTubeSearchService {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeSearchServiceImpl.class);

    private final YouTube.Builder builder;

    @Autowired
    public YouTubeSearchServiceImpl(@NotNull YouTube.Builder builder) {
        this.builder = builder;
    }

    @Cacheable(cacheNames = "searches")
    public List<SearchResult> search(YouTubeSearchForm form) throws IOException {
        final YouTube yt = builder.build();
        final YouTube.Search.List request = yt.search()
                .list(singletonList("snippet"));

        request.setType(form.getTypes());
        request.setMaxResults(50L);

        // concrete values
        if (isNotBlank(form.getChannelId())) {
            request.setChannelId(form.getChannelId());
        }
        // if (isNotBlank(form.getLocation())) {
        //     request.setLocationform.getLocation());
        // }
        // if (isNotBlank(form.getLocationRadius())) {
        //     request.setLocationRadiusform.getLocationRadius());
        // }
        if (isNotBlank(form.getQuery())) {
            request.setQ(form.getQuery());
        }

        // enums
        if (isNotBlank(form.getChannelType())) {
            request.setChannelType(form.getChannelType());
        }
        if (isNotBlank(form.getEventType())) {
            request.setEventType(form.getEventType());
        }
        if (isNotBlank(form.getOrder())) {
            request.setOrder(form.getOrder());
        }
        if (isNotBlank(form.getLang())) {
            request.setRelevanceLanguage(form.getLang());
        }

        // .setPublishedAfter()
        // .setPublishedBefore()
        // .setRegionCode()
        // .setRelevanceLanguage()
        // .setSafeSearch()
        // .setTopicId();

        // if (instanceof VideoSearchForm) {
        //   .setVideoCaption();
        //   .setVideoCategoryId()
        //   .setVideoDefinition()
        //   .setVideoDimension()
        //   .setVideoDuration()
        //   .setVideoEmbeddable()
        //   .setVideoLicense()
        //   .setVideoSyndicated()
        //   .setVideoType()
        // }

        final SearchListResponse response = request.execute();

        if (response.isEmpty() || (response.getItems() == null)) {
            return Collections.emptyList();
        }

        final String token = response.getNextPageToken();
        if (isNotBlank(token)) {
            LOG.info("token!! '{}'", token);
        }

        return response.getItems().stream().map(SearchResult::new).toList();
    }
}
