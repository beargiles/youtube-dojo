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
import com.coyotesong.dojo.youtube.model.Playlist;
import com.coyotesong.dojo.youtube.testdata.TestPlaylists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static com.coyotesong.dojo.youtube.controller.Constants.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test PlaylistController
 */
@WebMvcTest(controllers = PlaylistController.class)
@ContextConfiguration(classes = {
        PersistenceTestConfiguration.class,
        ServiceTestConfiguration.class,
        ComponentTestConfiguration.class
})
public class PlaylistControllerTest {
    @Autowired
    private MockMvc mockMvc;

    /**
     * Test home page
     *
     * @throws Exception a problem occurred
     */
    @Test
    public void Given_PlaylistController_When_GetHome_Then_Success() throws Exception {
        mockMvc.perform(get(PLAYLIST_HOME_PATH))
                .andExpectAll(
                        // content().contentType(TEXT_HTML),
                        handler().handlerType(PlaylistController.class),
                        handler().methodName("home"),
                        model().attribute(LANG_SELECT_OPTIONS, not(empty())),
                        model().hasNoErrors(),
                        status().isOk(),
                        view().name(PLAYLIST_HOME_VIEW_NAME))
                .andDo(print())
                .andReturn();
    }

    /**
     * Test retrieving a valid video
     *
     * @throws Exception a problem occurred
     */
    @Test
    public void Given_PlaylistController_When_GetPlaylistWithValidId_Then_Success() throws Exception {
        mockMvc.perform(get(PLAYLIST_FIND_BY_PLAYLIST_ID_PATH + TestPlaylists.PLAYLIST.getPlaylistId()))
                .andExpectAll(
                        // content().contentType(TEXT_HTML),
                        handler().handlerType(PlaylistController.class),
                        handler().methodName("findPlaylistById"),
                        model().attribute(SINGLE_PLAYLIST, instanceOf(Playlist.class)),
                        // model().attribute(SINGLE_VIDEO, use our custom matcher...
                        model().hasNoErrors(),
                        request().attribute(ID_PATH_VARIABLE, equalTo(TestPlaylists.PLAYLIST.getPlaylistId())),
                        status().isOk(),
                        view().name(PLAYLIST_FIND_BY_PLAYLIST_ID_VIEW_NAME))
                .andDo(print())
                .andReturn();
    }

    /**
     * Test retrieving a missing video
     *
     * @throws Exception a problem occurred
     */
    @Test
    public void Given_PlaylistController_When_GetPlaylistWithInvalidId_Then_Success() throws Exception {
        mockMvc.perform(get(PLAYLIST_FIND_BY_PLAYLIST_ID_PATH + TestPlaylists.MISSING_PLAYLIST.getPlaylistId())).andExpectAll(
                        // content().contentType(TEXT_HTML),
                        handler().handlerType(PlaylistController.class),
                        handler().methodName("findPlaylistById"),
                        // TODO: is there a way to verify the contents of an attribute?
                        model().attribute(SINGLE_PLAYLIST, nullValue()),
                        model().hasNoErrors(),
                        request().attribute(ID_PATH_VARIABLE, equalTo(TestPlaylists.MISSING_PLAYLIST.getPlaylistId())),
                        status().isNotFound(),
                        view().name(PLAYLIST_HOME_VIEW_NAME))
                .andDo(print())
                .andReturn();
    }
}
