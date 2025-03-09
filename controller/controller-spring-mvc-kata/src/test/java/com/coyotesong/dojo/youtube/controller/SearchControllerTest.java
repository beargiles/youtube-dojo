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

package com.coyotesong.dojo.youtube.controller;

import com.coyotesong.dojo.youtube.config.ComponentTestConfiguration;
import com.coyotesong.dojo.youtube.config.PersistenceTestConfiguration;
import com.coyotesong.dojo.youtube.config.ServiceTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static com.coyotesong.dojo.youtube.controller.Constants.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test SearchController
 */
@WebMvcTest(controllers = SearchController.class)
@ContextConfiguration(classes = {
        PersistenceTestConfiguration.class,
        ServiceTestConfiguration.class,
        ComponentTestConfiguration.class
})
public class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    /**
     * Test home page
     *
     * @throws Exception a problem occurred
     */
    @Test
    public void Given_SearchController_When_GetHome_Then_Success() throws Exception {
        mockMvc.perform(get(SEARCH_HOME_PATH))
                .andExpectAll(
                        // content().contentType(TEXT_HTML),
                        handler().handlerType(SearchController.class),
                        handler().methodName("home"),
                        model().attribute(LANG_SELECT_OPTIONS, not(empty())),
                        model().hasNoErrors(),
                        status().isOk(),
                        view().name(SEARCH_HOME_VIEW_NAME))
                .andDo(print())
                .andReturn();
    }

    /**
     * Test retrieving search results
     *
     * @throws Exception a problem occurred
     */
    @Test
    public void Given_SearchController_When_GetSearchWithValidData_Then_Success() throws Exception {
        // TODO: add body containing search form
        mockMvc.perform(post(SEARCH_CHANNEL_PATH))
                .andExpectAll(
                        // content().contentType(TEXT_HTML),
                        handler().handlerType(SearchController.class),
                        handler().methodName("channel"),
                        model().attribute(CHANNEL_SEARCH_RESULTS, instanceOf(Map.class)),
                        model().hasNoErrors(),
                        // request().attribute("search form", ...)
                        status().isOk(),
                        view().name(SEARCH_CHANNEL_VIEW_NAME))
                .andDo(print())
                .andReturn();
    }
}
