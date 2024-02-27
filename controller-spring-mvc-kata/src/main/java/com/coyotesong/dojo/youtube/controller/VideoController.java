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

import com.coyotesong.dojo.youtube.model.Video;
import com.coyotesong.dojo.youtube.security.LogSanitizer;
import com.coyotesong.dojo.youtube.service.YouTubeVideosService;
import org.jetbrains.annotations.NotNull;
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
 * Video controller
 */
@Controller
@RequestMapping("/video")
public class VideoController {
    private static final Logger LOG = LoggerFactory.getLogger(VideoController.class);

    private final YouTubeVideosService videoService;
    private final LogSanitizer sanitize;

    @Autowired
    public VideoController(@NotNull YouTubeVideosService videoService,
                           @NotNull LogSanitizer sanitize) {
        this.videoService = videoService;
        this.sanitize = sanitize;
    }

    /**
     * Landing page
     */
    @RequestMapping({ "/" })
    public ModelAndView home() {
        LOG.info("index page");

        final ModelAndView mv = new ModelAndView();

        // final GandalfAuthenticationToken token = authenticationProvider.cheat();
        // session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        mv.setViewName("views/video/index");
        mv.setStatus(HttpStatusCode.valueOf(200));

        return mv;
    }

    @RequestMapping({ "/id/{id}" })
    public ModelAndView showVideo(@PathVariable("id") String id) throws IOException {
        LOG.info("video {} page", sanitize.forVideoId(id));

        final ModelAndView mv = new ModelAndView();

        final Video video = videoService.getVideo(id);

        mv.setViewName("views/video/video");
        mv.getModelMap().addAttribute("video", video);
        mv.setStatus(HttpStatusCode.valueOf(200));

        return mv;
    }
}
