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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Home controller
 */
@Controller
@RequestMapping("/")
public class HomeController {
    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    /**
     * Landing page
     */
    @RequestMapping({ "/", "/index.html" })
    public ModelAndView home() {
        LOG.info("index page");

        final ModelAndView mv = new ModelAndView();

        mv.setViewName("views/index");

        /*
        mv.getModelMap().addAttribute(Constants.USER_SEARCH_FORM_NAME, new UserSearchForm());
        mv.getModelMap().addAttribute(Constants.CHANNEL_SEARCH_FORM_NAME, new ChannelSearchForm());
         */

        mv.getModelMap().addAttribute("channel", null);
        mv.setStatus(HttpStatusCode.valueOf(200));

        return mv;
    }
}
