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

import com.coyotesong.dojo.youtube.config.YouTubeContext;
import com.coyotesong.dojo.youtube.form.VideoSearchForm;
import com.coyotesong.dojo.youtube.model.SearchResult;
import com.coyotesong.dojo.youtube.security.LogSanitizerImpl;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForChannelListFactory;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForSearchListFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test YouTubeSearchService
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                LogSanitizerImpl.class,
                YouTubeContext.class,
                ClientForChannelListFactory.class,
                ClientForSearchListFactory.class,
                YouTubeChannelsServiceImpl.class,
                YouTubeSearchServiceImpl.class
        })
@DirtiesContext
public class YouTubeSearchServiceITest extends AbstractYouTubeServiceITest {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeSearchServiceITest.class);

    @Value("${quotaExceeded}")
    private Boolean quotaExceeded;

    private final YouTubeSearchService searchService;

    @Autowired
    public YouTubeSearchServiceITest(YouTubeChannelsService channelsService, YouTubeSearchService searchService) {
        super(channelsService);
        this.searchService = searchService;
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetSearchWithValidProperties_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final VideoSearchForm form = new VideoSearchForm();
        // form.setQuery("mrballen");
        // form.setChannelId(TestConstants.MRBALLEN_CHANNEL_ID);
        form.setChannelId(TestConstants.ANTON_PETROV_CHANNEL_ID);
        form.setOrder("date");
        form.setMaxResults(100);
        final List<SearchResult> results = searchService.search(form);
        assertThat(results, notNullValue());

        /*
         * @TODO - this due to call to uuid.toString() in 'result.toString()'
        try (StringWriter w = new StringWriter()) {
            for (SearchResult result : results) {
                w.write(result.toString());
                w.write("\n");
            }
            w.write("\n");

            for (SearchResult result : results) {
                w.write(String.format("https://youtube.com/watch?v=%s - %s - %s\n", result.getVideoId(), ISO_INSTANT.format(result.getPublishedAt().truncatedTo(ChronoUnit.SECONDS)), result.getTitle()));
            }

            LOG.info(w.toString());
        }
         */
    }
}
