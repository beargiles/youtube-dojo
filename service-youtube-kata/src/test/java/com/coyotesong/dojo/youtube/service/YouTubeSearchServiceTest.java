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
import com.coyotesong.dojo.youtube.config.YouTubeProperties;
import com.coyotesong.dojo.youtube.form.VideoSearchForm;
import com.coyotesong.dojo.youtube.model.SearchResult;
import com.coyotesong.dojo.youtube.security.LogSanitizerImpl;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForSearchListFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assumptions.abort;

/**
 * Test YouTubeSearchService
 * <p>
 * NOTE: this is actually an integration test
 */
// @ContextConfiguration
@ExtendWith(SpringExtension.class)
@Import({
        LogSanitizerImpl.class
})
@ImportAutoConfiguration(classes = {
        YouTubeContext.class,
        YouTubeProperties.class
})
@SpringBootTest(classes = {
        ClientForSearchListFactory.class,
        YouTubeSearchServiceImpl.class
})
public class YouTubeSearchServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeSearchServiceTest.class);

    @Autowired
    private YouTubeSearchService service;

    @Test
    public void Given_UserIsAuthenticated_When_GetSearchWithValidProperties_Then_Success() throws IOException {
        try {
            final VideoSearchForm form = new VideoSearchForm();
            // form.setQuery("mrballen");
            form.setChannelId("UCtPrkXdtCM5DACLufB9jbsA"); // MrBallen
            form.setOrder("date");
            final List<SearchResult> results = service.search(form);
            assertThat(results, notNullValue());

            try (StringWriter w = new StringWriter()) {
                for (SearchResult result : results) {
                    w.write(result.toString());
                    w.write("\n");
                }
                LOG.info(w.toString());
            }
        } catch (YouTubeAccountException e) {
            abort("quota exceeded");
        }
    }
}
