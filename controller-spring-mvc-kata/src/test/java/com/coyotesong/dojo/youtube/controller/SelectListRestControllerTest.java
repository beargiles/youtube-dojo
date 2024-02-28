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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.coyotesong.dojo.youtube.controller.Constants.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = SelectListRestController.class)
@ContextConfiguration(classes = {
        SelectListRestController.class
})
public class SelectListRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void Given_SelectListRestController_When_GetLanguageValues_Then_Success() throws Exception {
        mockMvc.perform(get(SELECT_LIST_GET_LANGUAGE_VALUES_PATH)).andExpectAll(
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.languageValues", notNullValue()),
                        jsonPath("$.languageValues[*].label", notNullValue()),
                        jsonPath("$.languageValues[*].value", notNullValue()),
                        status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    public void Given_SelectListRestController_When_GetSafeSearchValues_Then_Success() throws Exception {
        mockMvc.perform(get(SELECT_LIST_GET_SAFE_SEARCH_VALUES_PATH)).andExpectAll(
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.safeSearchValues", notNullValue()),
                        jsonPath("$.safeSearchValues[*].label", notNullValue()),
                        jsonPath("$.safeSearchValues[*].value", notNullValue()),
                        status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    public void Given_SelectListRestController_When_GetSortOrderValues_Then_Success() throws Exception {
        mockMvc.perform(get(SELECT_LIST_GET_SORT_ORDER_VALUES_PATH)).andExpectAll(
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.sortOrderValues", notNullValue()),
                        jsonPath("$.sortOrderValues[*].label", notNullValue()),
                        jsonPath("$.sortOrderValues[*].value", notNullValue()),
                        status().isOk())
                .andDo(print())
                .andReturn();
    }
}
