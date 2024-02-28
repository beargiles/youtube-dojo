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
import com.coyotesong.dojo.youtube.form.PlaylistSearchForm;
import com.coyotesong.dojo.youtube.form.SelectOption;
import com.coyotesong.dojo.youtube.form.VideoSearchForm;
import com.coyotesong.dojo.youtube.model.SearchResult;
import com.coyotesong.dojo.youtube.service.YouTubeSearchService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

import static com.coyotesong.dojo.youtube.controller.Constants.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Search controller
 */
@Controller
public class SearchController {
    private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);

    private final YouTubeSearchService searchService;

    @Autowired
    public SearchController(@NotNull YouTubeSearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * Landing page
     */
    @RequestMapping({ SEARCH_HOME_PATH, SEARCH_HOME_INDEX_PATH })
    public ModelAndView home(ModelMap model) {
        LOG.info("search landing page");

        final ModelAndView mv = new ModelAndView(SEARCH_HOME_VIEW_NAME);
        mv.getModelMap().addAttribute(LANG_SELECT_OPTIONS, SelectOption.LANGUAGE_SELECT_LIST);
        mv.getModelMap().addAttribute(ORDER_SELECT_OPTIONS, SelectOption.SORT_ORDER_SELECT_LIST);
        mv.getModelMap().addAttribute(SAFE_SEARCH_SELECT_OPTIONS, SelectOption.SAFE_SEARCH_SELECT_LIST);

        mv.getModelMap().addAttribute(CHANNEL_SEARCH_FORM_NAME, new ChannelSearchForm());
        mv.getModelMap().addAttribute(PLAYLIST_SEARCH_FORM_NAME, new PlaylistSearchForm());
        mv.getModelMap().addAttribute(VIDEO_SEARCH_FORM_NAME, new VideoSearchForm());

        return mv;
    }

    /**
     * Search channels
     */
    @RequestMapping(value = SEARCH_CHANNEL_PATH, method = RequestMethod.POST)
    public ModelAndView channel(
            // @Valid
            @ModelAttribute(CHANNEL_SEARCH_FORM_NAME) ChannelSearchForm searchForm,
            BindingResult result,
            ModelMap model) throws IOException {
        LOG.info("channel search page");

        // final GandalfAuthenticationToken token = authenticationProvider.cheat();
        // session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        if (result.hasErrors()) {
            // for now...
            final ModelAndView mv = new ModelAndView(SEARCH_HOME_VIEW_NAME);
            mv.getModelMap().addAttribute(CHANNEL_SEARCH_FORM_NAME, searchForm);
            mv.setStatus(INTERNAL_SERVER_ERROR);
            return mv;
        }

        final List<SearchResult> results = searchService.search(searchForm);
        final Map<String, List<SearchResult>> mapResults = mapSearchResults(results);

        final ModelAndView mv = new ModelAndView();
        mv.getModelMap().addAttribute(LANG_SELECT_OPTIONS, SelectOption.LANGUAGE_SELECT_LIST);
        mv.getModelMap().addAttribute(ORDER_SELECT_OPTIONS, SelectOption.SORT_ORDER_SELECT_LIST);
        mv.getModelMap().addAttribute(SAFE_SEARCH_SELECT_OPTIONS, SelectOption.SAFE_SEARCH_SELECT_LIST);

        mv.getModelMap().addAttribute(CHANNEL_SEARCH_RESULTS, mapResults);

        mv.setViewName(SEARCH_CHANNEL_VIEW_NAME);
        mv.setStatus(OK);

        return mv;
    }

    /**
     * Search playlists
     */
    @RequestMapping(SEARCH_PLAYLIST_PATH)
    public ModelAndView playlist(
            // @Valid
            @ModelAttribute(PLAYLIST_SEARCH_FORM_NAME) PlaylistSearchForm searchForm,
            BindingResult result,
            ModelMap model) throws IOException {
        LOG.info("playlist search page");

        // final GandalfAuthenticationToken token = authenticationProvider.cheat();
        // session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        if (result.hasErrors()) {
            // for now...
            final ModelAndView mv = new ModelAndView(SEARCH_HOME_VIEW_NAME);
            mv.getModelMap().addAttribute(CHANNEL_SEARCH_FORM_NAME, searchForm);
            mv.setStatus(INTERNAL_SERVER_ERROR);
            return mv;
        }

        final List<SearchResult> results = searchService.search(searchForm);
        final Map<String, List<SearchResult>> mapResults = mapSearchResults(results);

        final ModelAndView mv = new ModelAndView();
        mv.getModelMap().addAttribute(LANG_SELECT_OPTIONS, SelectOption.LANGUAGE_SELECT_LIST);
        mv.getModelMap().addAttribute(ORDER_SELECT_OPTIONS, SelectOption.SORT_ORDER_SELECT_LIST);
        mv.getModelMap().addAttribute(SAFE_SEARCH_SELECT_OPTIONS, SelectOption.SAFE_SEARCH_SELECT_LIST);

        mv.getModelMap().addAttribute(PLAYLIST_SEARCH_RESULTS, mapResults);

        mv.setViewName(SEARCH_PLAYLIST_VIEW_NAME);
        mv.setStatus(OK);

        return mv;
    }

    /**
     * Search videos
     */
    @RequestMapping(SEARCH_VIDEO_PATH)
    public ModelAndView video(
            // @Valid
            @ModelAttribute(PLAYLIST_SEARCH_FORM_NAME) PlaylistSearchForm searchForm,
            BindingResult result,
            ModelMap model) throws IOException {
        LOG.info("video search page");

        // final GandalfAuthenticationToken token = authenticationProvider.cheat();
        // session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        if (result.hasErrors()) {
            // for now...
            final ModelAndView mv = new ModelAndView(SEARCH_HOME_VIEW_NAME);
            mv.getModelMap().addAttribute(VIDEO_SEARCH_FORM_NAME, searchForm);
            mv.setStatus(INTERNAL_SERVER_ERROR);
            return mv;
        }

        final List<SearchResult> results = searchService.search(searchForm);
        final Map<String, List<SearchResult>> mapResults = mapSearchResults(results);

        final ModelAndView mv = new ModelAndView();
        mv.getModelMap().addAttribute(LANG_SELECT_OPTIONS, SelectOption.LANGUAGE_SELECT_LIST);
        mv.getModelMap().addAttribute(ORDER_SELECT_OPTIONS, SelectOption.SORT_ORDER_SELECT_LIST);
        mv.getModelMap().addAttribute(SAFE_SEARCH_SELECT_OPTIONS, SelectOption.SAFE_SEARCH_SELECT_LIST);

        mv.getModelMap().addAttribute(VIDEO_SEARCH_RESULTS, mapResults);

        mv.setViewName(SEARCH_VIDEO_VIEW_NAME);
        mv.setStatus(OK);

        return mv;
    }

    /**
     * Convert a List&lt;StringResult&gt; to a Map keyed by ChannelTitle
     * <p>
     * Should this also sort the search results within each channel?
     * </p>
     *
     * @param results raw search results
     * @return search results put into a Map keyed by the channel title
     */
    Map<String, List<SearchResult>> mapSearchResults(List<SearchResult> results) {
        // create intermediate map
        final Map<String, List<SearchResult>> tMap = new LinkedHashMap<>();
        for (SearchResult result : results) {
            final String key = result.getChannelTitle();
            if (!tMap.containsKey(key)) {
                tMap.put(key, new ArrayList<>());
            }
            tMap.get(key).add(result);
        }

        final List<String> keys = new ArrayList<>(tMap.keySet());
        Collections.sort(keys);
        final Map<String, List<SearchResult>> map = new LinkedHashMap<>();
        for (String key : keys) {
            map.put(key, tMap.get(key));
        }

        return map;
    }
}
