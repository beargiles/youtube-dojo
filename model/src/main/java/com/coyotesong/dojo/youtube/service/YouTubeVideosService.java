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

import com.coyotesong.tabs.model.Video;

import java.io.IOException;
import java.util.List;

/**
 * YouTube API client for unauthenticated users.
 */
@SuppressWarnings("unused")
public interface YouTubeVideosService {
    /**
     * Retrieve information about specified video
     * @param id video to load
     * @return requested video (when available)
     */
    Video getVideo(String id) throws IOException;

    /**
     * Retrieve information about specified video categories

     /**
     * Retrieve information about specified videos
     * @param ids videos to load
     * @return requested videos (when available)
     */
    List<Video> getVideos(List<String> ids) throws IOException;
}
