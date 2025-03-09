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

package com.coyotesong.dojo.youtube.service;

import com.coyotesong.dojo.youtube.form.VideoSearchForm;
import com.coyotesong.dojo.youtube.model.Channel;
import com.coyotesong.dojo.youtube.model.SearchResult;
import com.coyotesong.dojo.youtube.model.Video;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * File persistence service
 * <p>
 * This service is intended for use by developers - it provides a consistent
 * mechanism to allow YouTube objects to be stored in a local file. It does not
 * require a database connection.
 * </p>
 */
public interface FilePersistenceService {
    void writeChannelsToFile(@NotNull String filename, @NotNull Collection<Channel> channels) throws IOException;

    @NotNull
    List<Channel> readChannelsFromFile(@NotNull String filename) throws IOException;

    void writeVideosToFile(@NotNull String filename, @NotNull Collection<Video> videos) throws IOException;

    @NotNull
    List<Video> readVideosFromFile(@NotNull String filename) throws IOException;

    void writeVideoSearchResultsToFile(String filename, @NotNull VideoSearchForm form, @NotNull Collection<SearchResult> results) throws IOException;

    @NotNull
    VideoSearchForm readVideoSearchFormFromFile(@NotNull String filename) throws IOException;

    @NotNull
    List<SearchResult> readSearchResultsFromFile(@NotNull String filename) throws IOException;
}
