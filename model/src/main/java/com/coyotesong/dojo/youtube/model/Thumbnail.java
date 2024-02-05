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
package com.coyotesong.tabs.model;

//

/**
 * Thumbnails
 *
 * The channel, playlist, and playlist item thumbnails can be reduced to just the URL since
 * they always have the 'default' resolution (90x120).
 *
 * The video thumbnails can be one of five resolutions:
 *
 * - default: 90x120
 * - medium: 180x320
 * - high: 360x480
 * - standard: 480x640
 * - maxres: 720x1280
 */
public class Thumbnail {
    private String videoId;
    private String name; // = default, medium, high, standard, maxres
    private Integer height;
    private String url;
    private Integer width;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Thumbnail() {

    }

    public Thumbnail(String videoId, String name, com.google.api.services.youtube.model.Thumbnail thumbnail) {
        this.videoId = videoId;
        this.name = name;
        if (thumbnail.getHeight() != null) {
            this.height = thumbnail.getHeight().intValue();
        }
        this.url = thumbnail.getUrl();
        if (thumbnail.getWidth() != null) {
            this.width = thumbnail.getWidth().intValue();
        }
    }
}
