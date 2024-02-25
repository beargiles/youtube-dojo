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

package com.coyotesong.dojo.youtube.service.youTubeClient;

import com.coyotesong.dojo.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class YTUtils {
    @NotNull
    Map<String, Thumbnail> convertThumbnails(@NotNull String id, @NotNull ThumbnailDetails td) {
        final Map<String, Thumbnail> thumbnails = new LinkedHashMap<>();

        if ((td.getDefault() != null) && !td.getDefault().isEmpty()) {
            thumbnails.put("default", new Thumbnail(id, Thumbnail.Size.DEFAULT, td.getDefault().getUrl()));
        }

        if ((td.getStandard() != null) && !td.getStandard().isEmpty()) {
            thumbnails.put("standard", new Thumbnail(id, Thumbnail.Size.STANDARD, td.getStandard().getUrl()));
        }

        if ((td.getHigh() != null) && !td.getHigh().isEmpty()) {
            thumbnails.put("high", new Thumbnail(id, Thumbnail.Size.HIGH, td.getHigh().getUrl()));
        }

        if ((td.getMedium() != null) && !td.getMedium().isEmpty()) {
            thumbnails.put("medium", new Thumbnail(id, Thumbnail.Size.MEDIUM, td.getMedium().getUrl()));
        }

        if ((td.getMaxres() != null) && !td.getMaxres().isEmpty()) {
            thumbnails.put("maxres", new Thumbnail(id, Thumbnail.Size.MAX_RES, td.getMaxres().getUrl()));
        }

        return thumbnails;
    }
}
