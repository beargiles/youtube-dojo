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

public interface Constants {
    // content
    String SINGLE_CHANNEL = "channel";
    String SINGLE_PLAYLIST = "playlist";
    String SINGLE_VIDEO = "video";

    String LIST_OF_TOPICS = "topics";
    String LIST_OF_CHANNELS = "channels";
    String LIST_OF_PLAYLISTS = "playlists";
    String LIST_OF_VIDEOS = "videos";

    // for now these all use the same thymeleaf macro
    String CHANNEL_SEARCH_RESULTS = "mapResults";
    String PLAYLIST_SEARCH_RESULTS = "mapResults";
    String VIDEO_SEARCH_RESULTS = "mapResults";

    // search forms
    String CHANNEL_SEARCH_FORM_NAME = "channelSearchForm";
    String PLAYLIST_SEARCH_FORM_NAME = "playlistSearchForm";
    String USER_SEARCH_FORM_NAME = "userSearchForm";
    String VIDEO_SEARCH_FORM_NAME = "videoSearchForString";

    // select options
    String CHANNEL_TYPE_SELECT_OPTIONS = "channelTypeOptions";
    String EVENT_TYPE_SELECT_OPTIONS = "eventTypeOptions";
    String LANG_SELECT_OPTIONS = "langOptions";
    String ORDER_SELECT_OPTIONS = "orderOptions";
    String SAFE_SEARCH_SELECT_OPTIONS = "safeSearchOptions";

    // rest paths
    String REST_ROOT_PATH = "/api/v1/";
    String SELECT_LIST_ROOT_PATH = REST_ROOT_PATH + "search-list/";
    String SELECT_LIST_GET_LANGUAGE_VALUES_PATH = SELECT_LIST_ROOT_PATH + "languageValues";
    String SELECT_LIST_GET_SAFE_SEARCH_VALUES_PATH = SELECT_LIST_ROOT_PATH + "safeSearchValues";
    String SELECT_LIST_GET_SORT_ORDER_VALUES_PATH = SELECT_LIST_ROOT_PATH + "sortOrderValues";

    // html paths
    String HOME_PATH = "/";
    String HOME_INDEX_PATH = HOME_PATH + "index.html";

    String CHANNEL_HOME_PATH = "/channel/";
    String CHANNEL_HOME_INDEX_PATH = CHANNEL_HOME_PATH + "index.html";
    String CHANNEL_FIND_BY_CHANNEL_ID_PATH = CHANNEL_HOME_PATH + "id/";
    String CHANNEL_SEARCH_PATH = "/channel/search";

    String PLAYLIST_HOME_PATH = "/playlist/";
    String PLAYLIST_HOME_INDEX_PATH = PLAYLIST_HOME_PATH + "index.html";
    String PLAYLIST_FIND_BY_PLAYLIST_ID_PATH = PLAYLIST_HOME_PATH + "id/";

    String SEARCH_HOME_PATH = "/search/";
    String SEARCH_HOME_INDEX_PATH = SEARCH_HOME_PATH + "index.html";
    String SEARCH_CHANNEL_PATH = SEARCH_HOME_PATH + "channel/";
    String SEARCH_PLAYLIST_PATH = SEARCH_HOME_PATH + "playlist/";
    String SEARCH_VIDEO_PATH = SEARCH_HOME_PATH + "video/";

    String VIDEO_HOME_PATH = "/video/";
    String VIDEO_HOME_INDEX_PATH = VIDEO_HOME_PATH + "index.html";
    String VIDEO_FIND_BY_VIDEO_ID_PATH = VIDEO_HOME_PATH + "id/";

    String ID_PATH_VARIABLE = "id";

    //
    // view names
    //
    String ROOT_VIEW_NAME = "views/";

    String HOME_VIEW_NAME = ROOT_VIEW_NAME + "index";

    // channel view names
    String CHANNEL_ROOT_VIEW_NAME = ROOT_VIEW_NAME + "channel/";
    String CHANNEL_HOME_VIEW_NAME = CHANNEL_ROOT_VIEW_NAME + "index";
    String CHANNEL_FIND_BY_CHANNEL_ID_VIEW_NAME = CHANNEL_ROOT_VIEW_NAME + "channel";
    String CHANNEL_SEARCH_VIEW_NAME = CHANNEL_ROOT_VIEW_NAME + "search";

    // playlist view names
    String PLAYLIST_ROOT_VIEW_NAME = ROOT_VIEW_NAME + "playlist/";
    String PLAYLIST_HOME_VIEW_NAME = PLAYLIST_ROOT_VIEW_NAME + "index";
    String PLAYLIST_FIND_BY_PLAYLIST_ID_VIEW_NAME = PLAYLIST_ROOT_VIEW_NAME + "playlist";

    // search view names
    String SEARCH_ROOT_VIEW_NAME = ROOT_VIEW_NAME + "search/";
    String SEARCH_HOME_VIEW_NAME = SEARCH_ROOT_VIEW_NAME + "index";
    String SEARCH_CHANNEL_VIEW_NAME = SEARCH_ROOT_VIEW_NAME + "channel/index";
    String SEARCH_PLAYLIST_VIEW_NAME = SEARCH_ROOT_VIEW_NAME + "playlist/index";
    String SEARCH_VIDEO_VIEW_NAME = SEARCH_ROOT_VIEW_NAME + "video/index";

    // video view names
    String VIDEO_ROOT_VIEW_NAME = ROOT_VIEW_NAME + "video/";
    String VIDEO_HOME_VIEW_NAME = VIDEO_ROOT_VIEW_NAME + "index";
    String VIDEO_FIND_BY_VIDEO_ID_VIEW_NAME = VIDEO_ROOT_VIEW_NAME + "video";
}
