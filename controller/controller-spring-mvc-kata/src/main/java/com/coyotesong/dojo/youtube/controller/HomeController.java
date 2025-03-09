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

import com.coyotesong.dojo.youtube.form.ChannelSearchForm;
import com.coyotesong.dojo.youtube.form.SelectOption;
import com.coyotesong.dojo.youtube.form.UserSearchForm;
import com.coyotesong.dojo.youtube.model.WikipediaTopic;
import com.coyotesong.dojo.youtube.repository.WikipediaTopicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

import static com.coyotesong.dojo.youtube.controller.Constants.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * Home controller
 */
@Controller
public class HomeController {
    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    private final WikipediaTopicRepository wikipediaTopicRepository;

    @Autowired
    public HomeController(WikipediaTopicRepository wikipediaTopicRepository) {
        this.wikipediaTopicRepository = wikipediaTopicRepository;
    }

    /**
     * Landing page
     */
    @RequestMapping(
            path = {HOME_PATH, Constants.HOME_INDEX_PATH},
            produces = TEXT_HTML_VALUE
    )
    public ModelAndView home() {
        LOG.info("index page");

        // final GandalfAuthenticationToken token = authenticationProvider.cheat();
        // session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        final ModelAndView mv = new ModelAndView(HOME_VIEW_NAME);
        mv.getModelMap().addAttribute(LANG_SELECT_OPTIONS, SelectOption.LANGUAGE_SELECT_LIST);
        mv.getModelMap().addAttribute(ORDER_SELECT_OPTIONS, SelectOption.SORT_ORDER_SELECT_LIST);
        mv.getModelMap().addAttribute(SAFE_SEARCH_SELECT_OPTIONS, SelectOption.SAFE_SEARCH_SELECT_LIST);

        mv.getModelMap().addAttribute(CHANNEL_SEARCH_FORM_NAME, new ChannelSearchForm());
        mv.getModelMap().addAttribute(USER_SEARCH_FORM_NAME, new UserSearchForm());

        final List<WikipediaTopic> topics = wikipediaTopicRepository.findAll();
        Collections.sort(topics, (s, p) -> s.getLabel().compareTo(p.getLabel()));
        mv.getModelMap().addAttribute(LIST_OF_TOPICS, topics);

        mv.setStatus(OK);

        return mv;
    }
}
