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
import com.coyotesong.dojo.youtube.form.UserSearchForm;
import com.coyotesong.dojo.youtube.model.Channel;
import com.coyotesong.dojo.youtube.model.SearchResult;
import com.coyotesong.dojo.youtube.security.LogSanitizer;
import com.coyotesong.dojo.youtube.service.YouTubeChannelsService;
import com.coyotesong.dojo.youtube.service.YouTubePlaylistsService;
import com.coyotesong.dojo.youtube.service.YouTubeSearchService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
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

@Controller
@RequestMapping("/channel")
public class ChannelController {
    private static final Logger LOG = LoggerFactory.getLogger(ChannelController.class);

    private final YouTubeChannelsService channelsService;
    private final YouTubePlaylistsService playlistsService;
    private final YouTubeSearchService searchService;
    // private final ChannelRepository channelDao;
    // private final PlaylistRepository playlistDao;
    private final LogSanitizer sanitize;

    @Autowired
    public ChannelController(@NotNull YouTubeChannelsService channelsService,
                             @NotNull YouTubePlaylistsService playlistsService,
                             @NotNull YouTubeSearchService searchService,
                             @NotNull LogSanitizer sanitize) {
        this.channelsService = channelsService;
        this.playlistsService = playlistsService;
        this.searchService = searchService;
        // this.channelDao = channelDao;
        // this.playlistDao = playlistDao;
        this.sanitize = sanitize;
    }


    /**
     * Landing page
     */
    @RequestMapping({"/"})
    public ModelAndView home() {
        LOG.info("channels page");

        final ModelAndView mv = new ModelAndView();

        // final GandalfAuthenticationToken token = authenticationProvider.cheat();
        // session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        // final List<Channel> channels = channelDao.findAll();
        final List<Channel> channels = Collections.emptyList();
        mv.getModelMap().addAttribute("channels", channels);

        mv.setViewName("views/channel/index");
        mv.setStatus(HttpStatusCode.valueOf(200));

        return mv;
    }

    /**
     * Show channel details
     */
    @RequestMapping({"/id/{id}"})
    public ModelAndView showChannel(@PathVariable("id") String id) throws IOException {
        LOG.info("page: /channel/id/{}", sanitize.forChannelId(id));

        final ModelAndView mv = new ModelAndView();

        // final GandalfAuthenticationToken token = authenticationProvider.cheat();
        // session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        final Channel channel = channelsService.getChannel(id);
        if (channel == null) {
            LOG.info("could not find channel: {}", sanitize.forChannelId(id));
        }

        mv.setViewName("views/channel/channel");
        mv.getModelMap().addAttribute("channel", channel);
        mv.setStatus(HttpStatusCode.valueOf(200));

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
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ModelAndView search(
            // @Valid
            @ModelAttribute(Constants.CHANNEL_SEARCH_FORM_NAME) ChannelSearchForm searchForm,
            BindingResult result,
            ModelMap model) throws IOException {

        LOG.info("Channel search results page");

        final ModelAndView mv = new ModelAndView();
        mv.getModelMap().addAttribute(Constants.USER_SEARCH_FORM_NAME, new UserSearchForm());
        mv.getModelMap().addAttribute(Constants.CHANNEL_SEARCH_FORM_NAME, searchForm);
        //. mv.getModelMap().addAttribute(Constants.LANG_SELECT_OPTIONS, SelectOption.LANGUAGE_SELECT_OPTIONS);
        // mv.getModelMap().addAttribute(Constants.ORDER_SELECT_OPTIONS, SelectOption.ORDER_SELECT_OPTIONS);
        // mv.getModelMap().addAttribute(Constants.SAFE_SEARCH_SELECT_OPTIONS, SelectOption.SAFE_SEARCH_SELECT_OPTIONS);

        if (result.hasErrors()) {
            // return to original page for corrections?
            mv.setViewName("views/index");
            return mv;
        }

        // final GandalfAuthenticationToken token = authenticationProvider.cheat();
        // session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        mv.setViewName("views/channel/search");
        final List<SearchResult> results = searchService.search(searchForm);

        mv.setStatus(HttpStatusCode.valueOf(200));

        return mv;
    }
}
