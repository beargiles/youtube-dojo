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
package com.coyotesong.tabs.repo;

import com.coyotesong.tabs.model.Channel;
import com.coyotesong.tabs.model.Playlist;
import com.coyotesong.tabs.model.Video;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * Persistence mechanism for YouTube channels
 */
public interface ThumbnailRepository {

    /**
     * Save video thumbnails
     * @param videos
     */
    void saveThumbnails(Collection<Video> videos);
}