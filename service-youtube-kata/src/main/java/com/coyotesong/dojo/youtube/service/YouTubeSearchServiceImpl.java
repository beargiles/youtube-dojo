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

import com.coyotesong.dojo.youtube.form.YouTubeSearchForm;
import com.coyotesong.dojo.youtube.model.SearchResult;
import com.coyotesong.dojo.youtube.security.LogSanitizer;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForSearchListFactory;
import com.coyotesong.dojo.youtube.service.youTubeClient.YouTubeClient.ListSearchResults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of YouTubeSearchService
 */
@Service("YouTubeSearchService")
public class YouTubeSearchServiceImpl implements YouTubeSearchService {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeSearchServiceImpl.class);

    private final ClientForSearchListFactory clientForSearchListFactory;
    private final LogSanitizer sanitize;

    @Autowired
    public YouTubeSearchServiceImpl(@NotNull ClientForSearchListFactory clientForSearchListFactory,
                                    LogSanitizer sanitize) {
        this.clientForSearchListFactory = clientForSearchListFactory;
        this.sanitize = sanitize;
    }

    /**
     * Retrieve search results
     *
     * @param searchForm search criteria
     * @return
     * @throws IOException
     */
    @Override
    @Nullable
    public List<SearchResult> search(@NotNull YouTubeSearchForm searchForm) throws IOException {
        LOG.trace("search()...");

        int counter = 0;
        final List<SearchResult> results = new ArrayList<>();
        final ListSearchResults client = clientForSearchListFactory.newBuilder().withSearchForm(searchForm).build();
        while (client.hasNext() && counter++ < 3) {
            results.addAll(client.next());
        }

        LOG.trace("search() -> {} record(s)", results.size());
        return results;
    }
}
