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

import com.coyotesong.dojo.youtube.model.Video;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.util.List;

/**
 * YouTube 'videos' API client
 * <p>
 * Other options:
 *     <ul>
 *         <li>Chart (?)</li>
 *         <li>HL/Locale</li>
 *         <li>MaxWidth [72..8192]/MaxHeight [72..8192]</li>
 *         <li>MyRating</li>
 *         <li>RegionCode</li>
 *         <li>VideoCategoryId</li>
 *     </ul>
 *     The 'OnBehalfOfContentOwner' is intended for use by YouTube content partners.
 * </p>
 * <p>
 * see <a href="https://googleapis.dev/java/google-api-services-youtube/latest/index.html?com/google/api/services/youtube/YouTube.Videos.List.html">YouTube.Videos.List</a>
 */
@SuppressWarnings("unused")
public interface YouTubeVideosService {
    /**
     * Retrieve information about specified video
     *
     * @param id video to load
     * @return requested video (when available)
     */
    @Nullable
    Video getVideo(@NotNull String id) throws IOException;

    /**
     * Retrieve information about specified videos
     *
     * @param ids videos to load
     * @return requested videos (when available)
     */
    @NotNull
    List<Video> getVideos(@NotNull @Unmodifiable List<String> ids) throws IOException;
}
