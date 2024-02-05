/*
 * Copyright (c) 2023 Bear Giles <bgiles@coyotesong.com>.
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
package com.coyotesong.dojo.youtube.service;

import com.coyotesong.tabs.form.YouTubeSearchForm;
import com.coyotesong.tabs.model.SearchResult;

import java.io.IOException;
import java.util.List;

/**
 * YouTube API client for unauthenticated users.
 */
@SuppressWarnings("unused")
public interface YouTubeSearchService {

    /**
     * Perform a search
     * @param form search form
     * @throws IOException a REST client issue problem occurred
     */
    List<SearchResult> search(YouTubeSearchForm form) throws IOException;
}
