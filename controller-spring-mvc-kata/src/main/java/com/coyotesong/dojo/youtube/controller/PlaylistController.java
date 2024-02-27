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

import com.coyotesong.dojo.youtube.model.Playlist;
import com.coyotesong.dojo.youtube.security.LogSanitizer;
import com.coyotesong.dojo.youtube.service.YouTubePlaylistsService;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * Playlist controller
 */
@Controller
@RequestMapping("/playlist")
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
    @RequestMapping({ "/" })
    public ModelAndView home() {
        LOG.info("playlist landing page");

        final ModelAndView mv = new ModelAndView();

        mv.setViewName("views/playlist/index");
        mv.setStatus(HttpStatusCode.valueOf(200));

        return mv;
    }

    @RequestMapping({ "/id/{id}" })
    public ModelAndView showPlaylist(@PathVariable("id") String id) throws IOException {
        LOG.info("playlist {} page", sanitize.forPlaylistId(id));
        final ModelAndView mv = new ModelAndView();

        // final GandalfAuthenticationToken token = authenticationProvider.cheat();
        // session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        final Playlist playlist = playlistService.getPlaylist(id);
        mv.getModelMap().addAttribute("playlist", playlist);

        mv.setViewName("views/playlist/playlist");
        mv.setStatus(HttpStatusCode.valueOf(200));

        return mv;
    }
}
