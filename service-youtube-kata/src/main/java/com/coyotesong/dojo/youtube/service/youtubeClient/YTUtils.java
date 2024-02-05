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

package com.coyotesong.dojo.youtube.model;

import com.google.api.services.youtube.model.ThumbnailDetails;

import java.util.LinkedHashMap;
import java.util.Map;

public class YTUtils {
    Map<String, Thumbnail> convertThumbnails(String id, ThumbnailDetails td) {
        final Map<String, Thumbnail> thumbnails = new LinkedHashMap<>();

        if ((td.getDefault() != null) && !td.getDefault().isEmpty()) {
            thumbnails.put("default", new Thumbnail(id, "default", td.getDefault()));
        }

        if ((td.getStandard() != null) && !td.getStandard().isEmpty()) {
            thumbnails.put("standard", new Thumbnail(id, "standard", td.getStandard()));
        }

        if ((td.getHigh() != null) && !td.getHigh().isEmpty()) {
            thumbnails.put("high", new Thumbnail(id, "high", td.getHigh()));
        }

        if ((td.getMedium() != null) && !td.getMedium().isEmpty()) {
            thumbnails.put("medium", new Thumbnail(id, "medium", td.getMedium()));
        }

        if ((td.getMaxres() != null) && !td.getMaxres().isEmpty()) {
            thumbnails.put("maxres", new Thumbnail(id, "maxres", td.getMaxres()));
        }

        return thumbnails;
    }
}
