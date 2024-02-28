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

import com.coyotesong.dojo.youtube.form.SelectOption;
import com.coyotesong.dojo.youtube.model.Playlist;
import com.coyotesong.dojo.youtube.security.LogSanitizer;
import com.coyotesong.dojo.youtube.service.YouTubePlaylistsService;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import static com.coyotesong.dojo.youtube.controller.Constants.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

/**
 * Playlist controller
 */
@Controller
public class PlaylistController {
    private static final Logger LOG = LoggerFactory.getLogger(PlaylistController.class);

    private final YouTubePlaylistsService playlistService;
    private final LogSanitizer sanitize;

    @Autowired
    public PlaylistController(@NotNull YouTubePlaylistsService playlistService,
                              @NotNull LogSanitizer sanitize) {
        this.playlistService = playlistService;
        this.sanitize = sanitize;
    }

    /**
     * Landing page
     */
    @RequestMapping({ PLAYLIST_HOME_PATH, PLAYLIST_HOME_INDEX_PATH })
    public ModelAndView home() {
        LOG.info("playlist landing page");

        // final GandalfAuthenticationToken token = authenticationProvider.cheat();
        // session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        final ModelAndView mv = new ModelAndView(PLAYLIST_HOME_VIEW_NAME);
        mv.getModelMap().addAttribute(LANG_SELECT_OPTIONS, SelectOption.LANGUAGE_SELECT_LIST);
        mv.getModelMap().addAttribute(ORDER_SELECT_OPTIONS, SelectOption.SORT_ORDER_SELECT_LIST);
        mv.getModelMap().addAttribute(SAFE_SEARCH_SELECT_OPTIONS, SelectOption.SAFE_SEARCH_SELECT_LIST);

        mv.setStatus(OK);

        return mv;
    }

    @RequestMapping(PLAYLIST_FIND_BY_PLAYLIST_ID_PATH + "{" + ID_PATH_VARIABLE + "}")
    public ModelAndView findPlaylistById(@PathVariable(ID_PATH_VARIABLE) String id) throws IOException {
        LOG.info("findPlaylistById('{}') page", sanitize.forPlaylistId(id));

        // final GandalfAuthenticationToken token = authenticationProvider.cheat();
        // session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        final ModelAndView mv = new ModelAndView(PLAYLIST_FIND_BY_PLAYLIST_ID_VIEW_NAME);
        mv.getModelMap().addAttribute(LANG_SELECT_OPTIONS, SelectOption.LANGUAGE_SELECT_LIST);
        mv.getModelMap().addAttribute(ORDER_SELECT_OPTIONS, SelectOption.SORT_ORDER_SELECT_LIST);
        mv.getModelMap().addAttribute(SAFE_SEARCH_SELECT_OPTIONS, SelectOption.SAFE_SEARCH_SELECT_LIST);

        final Playlist playlist = playlistService.getPlaylist(id);
        if (playlist != null) {
            mv.getModelMap().addAttribute(SINGLE_PLAYLIST, playlist);
            mv.setStatus(OK);
        } else {
            LOG.info("could not find playlist: {}", sanitize.forPlaylistId(id));
            mv.setViewName(PLAYLIST_HOME_VIEW_NAME);
            // this will probably trigger 404 handler instead of expected page :-(
            mv.setStatus(NOT_FOUND);
        }

        return mv;
    }
}
