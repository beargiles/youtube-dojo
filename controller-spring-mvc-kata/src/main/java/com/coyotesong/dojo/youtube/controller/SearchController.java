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
import com.coyotesong.dojo.youtube.form.VideoSearchForm;
import com.coyotesong.dojo.youtube.model.SearchResult;
import com.coyotesong.dojo.youtube.security.LogSanitizer;
import com.coyotesong.dojo.youtube.service.YouTubeSearchService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

/**
 * Search controller
 */
@Controller
@RequestMapping("/search")
public class SearchController {
    private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);

    private final YouTubeSearchService searchService;
    private final LogSanitizer sanitize;

    @Autowired
    public SearchController(@NotNull YouTubeSearchService searchService, @NotNull LogSanitizer sanitize) {
        this.searchService = searchService;
        this.sanitize = sanitize;
    }

    /**
     * Landing page
     */
    @RequestMapping({ "/"})
    public ModelAndView home() {
        LOG.info("search landing page");
        return new ModelAndView("/views/search/index");
    }

    /**
     * Search channels
     */
    @RequestMapping({ "/channel/"})
    public ModelAndView channel() {
        LOG.info("channel search page");

        final ModelAndView mv = new ModelAndView();

        // final GandalfAuthenticationToken token = authenticationProvider.cheat();
        // session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        try {
            final ChannelSearchForm form = new ChannelSearchForm();
            final List<SearchResult> results = searchService.search(form);
            final Map<String, List<SearchResult>> mapResults = mapSearchResults(results);
            mv.getModelMap().addAttribute("mapResults", mapResults);
        } catch (IOException e) {
            mv.setViewName("views/ioException");
            mv.getModelMap().addAttribute("ioException", e);
            mv.setStatus(HttpStatusCode.valueOf(500));
            return mv;
        }

        mv.setViewName("views/search/channel");
        mv.setStatus(HttpStatusCode.valueOf(200));

        return mv;
    }

    /**
     * Search playlists
     */
    @RequestMapping({ "/playlist/"})
    public ModelAndView playlist() {
        LOG.info("playlist search page");

        final ModelAndView mv = new ModelAndView();

        // final GandalfAuthenticationToken token = authenticationProvider.cheat();
        // session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        try {
            final PlaylistSearchForm form = new PlaylistSearchForm();
            final List<SearchResult> results = searchService.search(form);
            final Map<String, List<SearchResult>> mapResults = mapSearchResults(results);
            mv.getModelMap().addAttribute("mapResults", mapResults);
        } catch (IOException e) {
            mv.setViewName("views/ioException");
            mv.getModelMap().addAttribute("ioException", e);
            mv.setStatus(HttpStatusCode.valueOf(500));
            return mv;
        }

        mv.setViewName("views/search/playlist");
        mv.setStatus(HttpStatusCode.valueOf(200));

        return mv;
    }

    /**
     * Search videos
     */
    @RequestMapping({ "/video/"})
    public ModelAndView video() {
        LOG.info("video search page");

        final ModelAndView mv = new ModelAndView();

        // final GandalfAuthenticationToken token = authenticationProvider.cheat();
        // session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        try {
            final VideoSearchForm form = new VideoSearchForm();
            final List<SearchResult> results = searchService.search(form);
            final Map<String, List<SearchResult>> mapResults = mapSearchResults(results);
            mv.getModelMap().addAttribute("mapResults", mapResults);
        } catch (IOException e) {
            mv.setViewName("views/ioException");
            mv.getModelMap().addAttribute("ioException", e);
            mv.setStatus(HttpStatusCode.valueOf(500));
            return mv;
        }

        mv.setViewName("views/search/video");
        mv.setStatus(HttpStatusCode.valueOf(200));

        return mv;
    }

    /**
     * Convert a List&lt;StringResult&gt; to a Map keyed by ChannelTitle
     * <p>
     * Should this also sort the search results within each channel?
     * </p>
     *
     * @param results
     * @return
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
