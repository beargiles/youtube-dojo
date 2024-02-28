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

import com.coyotesong.dojo.youtube.data.TestVideos;
import com.coyotesong.dojo.youtube.model.Video;
import com.coyotesong.dojo.youtube.security.LogSanitizerImpl;
import com.coyotesong.dojo.youtube.service.MockYouTubeVideosServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.coyotesong.dojo.youtube.controller.Constants.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test VideoController
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = VideoController.class)
@ContextConfiguration(classes = {
        // dependencies
        VideoController.class,
        LogSanitizerImpl.class,
        // mocked dependencies
        MockYouTubeVideosServiceImpl.class
})
public class VideoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    /**
     * Test home page
     *
     * @throws Exception a problem occurred
     */
    @Test
    public void Given_VideoController_When_GetHome_Then_Success() throws Exception {
        mockMvc.perform(get(VIDEO_HOME_PATH))
                .andExpectAll(
                        // content().contentType(TEXT_HTML),
                        handler().handlerType(VideoController.class),
                        handler().methodName("home"),
                        model().attribute(LANG_SELECT_OPTIONS, not(empty())),
                        model().hasNoErrors(),
                        status().isOk(),
                        view().name(VIDEO_HOME_VIEW_NAME))
                .andDo(print())
                .andReturn();
    }

    /**
     * Test retrieving a valid video
     *
     * @throws Exception a problem occurred
     */
    @Test
    public void Given_VideoController_When_GetVideoWithValidId_Then_Success() throws Exception {
        mockMvc.perform(get(VIDEO_FIND_BY_VIDEO_ID_PATH + TestVideos.VIDEO.getVideoId()))
                .andExpectAll(
                        // content().contentType(TEXT_HTML),
                        handler().handlerType(VideoController.class),
                        handler().methodName("findVideoById"),
                        // TODO: is there a way to verify the contents of an attribute?
                        model().attribute(SINGLE_VIDEO, instanceOf(Video.class)),
                        model().hasNoErrors(),
                        request().attribute(ID_PATH_VARIABLE, equalTo(TestVideos.VIDEO.getVideoId())),
                        status().isOk(),
                        view().name(VIDEO_FIND_BY_VIDEO_ID_VIEW_NAME))
                .andDo(print())
                .andReturn();
    }

    /**
     * Test retrieving a missing video
     *
     * @throws Exception a problem occurred
     */
    @Test
    public void Given_VideoController_When_GetVideoWithInvalidId_Then_Success() throws Exception {
        mockMvc.perform(get(VIDEO_FIND_BY_VIDEO_ID_PATH + TestVideos.MISSING_VIDEO.getVideoId())).andExpectAll(
                        // content().contentType(TEXT_HTML),
                        handler().handlerType(VideoController.class),
                        handler().methodName("findVideoById"),
                        // TODO: is there a way to verify the contents of an attribute?
                        model().attribute(SINGLE_VIDEO, nullValue()),
                        model().hasNoErrors(),
                        request().attribute(ID_PATH_VARIABLE, equalTo(TestVideos.MISSING_VIDEO.getVideoId())),
                        status().isNotFound(),
                        view().name(VIDEO_HOME_VIEW_NAME))
                .andDo(print())
                .andReturn();
    }
}
