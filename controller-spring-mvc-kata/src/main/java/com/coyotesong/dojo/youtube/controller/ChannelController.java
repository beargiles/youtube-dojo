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
import com.coyotesong.dojo.youtube.model.Channel;
import com.coyotesong.dojo.youtube.model.SearchResult;
import com.coyotesong.dojo.youtube.security.LogSanitizer;
import com.coyotesong.dojo.youtube.service.YouTubeChannelsService;
import com.coyotesong.dojo.youtube.service.YouTubeSearchService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.coyotesong.dojo.youtube.controller.Constants.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Controller
public class ChannelController {
    private static final Logger LOG = LoggerFactory.getLogger(ChannelController.class);

    private final YouTubeChannelsService channelsService;
    private final YouTubeSearchService searchService;
    private final LogSanitizer sanitize;

    @Autowired
    public ChannelController(@NotNull YouTubeChannelsService channelsService,
                             @NotNull YouTubeSearchService searchService,
                             @NotNull LogSanitizer sanitize) {
        this.channelsService = channelsService;
        this.searchService = searchService;
        this.sanitize = sanitize;
    }

    /**
     * Landing page
     */
    @RequestMapping({CHANNEL_HOME_PATH, CHANNEL_HOME_INDEX_PATH })
    public ModelAndView home() {
        LOG.info("channels page");

        // final GandalfAuthenticationToken token = authenticationProvider.cheat();
        // session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        final ModelAndView mv = new ModelAndView();
        mv.getModelMap().addAttribute(LANG_SELECT_OPTIONS, SelectOption.LANGUAGE_SELECT_LIST);
        mv.getModelMap().addAttribute(ORDER_SELECT_OPTIONS, SelectOption.SORT_ORDER_SELECT_LIST);
        mv.getModelMap().addAttribute(SAFE_SEARCH_SELECT_OPTIONS, SelectOption.SAFE_SEARCH_SELECT_LIST);

        // no cache or persistence yet...
        // final List<Channel> channels = channelDao.findAll();
        final List<Channel> channels = Collections.emptyList();
        mv.getModelMap().addAttribute(LIST_OF_CHANNELS, channels);

        mv.setViewName(CHANNEL_HOME_VIEW_NAME);
        mv.setStatus(OK);

        return mv;
    }

    /**
     * Show channel details
     */
    @RequestMapping(CHANNEL_FIND_BY_CHANNEL_ID_PATH)
    public ModelAndView findChannelById(@PathVariable(ID_PATH_VARIABLE) String id) throws IOException {
        LOG.info("findChannelById('{}') page", sanitize.forChannelId(id));

        // final GandalfAuthenticationToken token = authenticationProvider.cheat();
        // session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        final ModelAndView mv = new ModelAndView(CHANNEL_FIND_BY_CHANNEL_ID_VIEW_NAME);
        mv.getModelMap().addAttribute(LANG_SELECT_OPTIONS, SelectOption.LANGUAGE_SELECT_LIST);
        mv.getModelMap().addAttribute(ORDER_SELECT_OPTIONS, SelectOption.SORT_ORDER_SELECT_LIST);
        mv.getModelMap().addAttribute(SAFE_SEARCH_SELECT_OPTIONS, SelectOption.SAFE_SEARCH_SELECT_LIST);

        final Channel channel = channelsService.getChannel(id);
        if (channel != null) {
            mv.getModelMap().addAttribute(SINGLE_CHANNEL, channel);
            mv.setStatus(OK);
        } else {
            LOG.info("could not find channel: {}", sanitize.forChannelId(id));
            mv.setViewName(CHANNEL_HOME_PATH);
            // this will probably trigger 404 handler instead of expected page :-(
            mv.setStatus(NOT_FOUND);
        }

        return mv;
    }

    /**
     * Search for matching channels
     *
     * @param searchForm
     * @param result
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = CHANNEL_SEARCH_PATH, method = RequestMethod.POST)
    public ModelAndView search(
            // @Valid
            @ModelAttribute(CHANNEL_SEARCH_FORM_NAME) ChannelSearchForm searchForm,
            BindingResult result,
            ModelMap model) throws IOException {
        LOG.info("Channel search results page");

        // final GandalfAuthenticationToken token = authenticationProvider.cheat();
        // session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        final ModelAndView mv = new ModelAndView();
        mv.getModelMap().addAttribute(LANG_SELECT_OPTIONS, SelectOption.LANGUAGE_SELECT_LIST);
        mv.getModelMap().addAttribute(ORDER_SELECT_OPTIONS, SelectOption.SORT_ORDER_SELECT_LIST);
        mv.getModelMap().addAttribute(SAFE_SEARCH_SELECT_OPTIONS, SelectOption.SAFE_SEARCH_SELECT_LIST);

        mv.getModelMap().addAttribute(USER_SEARCH_FORM_NAME, new UserSearchForm());
        mv.getModelMap().addAttribute(CHANNEL_SEARCH_FORM_NAME, searchForm);

        if (result.hasErrors()) {
            // return to original page for corrections?
            mv.setViewName(HOME_VIEW_NAME);
            return mv;
        }

        mv.setViewName(CHANNEL_SEARCH_VIEW_NAME);
        final List<SearchResult> results = searchService.search(searchForm);
        mv.getModelMap().addAttribute(CHANNEL_SEARCH_RESULTS, results);

        mv.setStatus(OK);

        return mv;
    }
}
