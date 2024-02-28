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

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = HomeController.class)
@ContextConfiguration(classes = {
        HomeController.class
})
public class HomeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void Given_HomeController_When_GetHome_Then_Success() throws Exception {
        mockMvc.perform(get(Constants.HOME_PATH)).andExpectAll(
                        // content().contentType(MediaType.TEXT_HTML)
                        handler().handlerType(HomeController.class),
                        handler().methodName("home"),
                        model().attribute(Constants.CHANNEL_SEARCH_FORM_NAME, notNullValue()),
                        model().attribute(Constants.LANG_SELECT_OPTIONS, not(empty())),
                        model().hasNoErrors(),
                        status().isOk(),
                        view().name(Constants.HOME_VIEW_NAME))
                .andDo(print())
                .andReturn();
    }
}
