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
package com.coyotesong.dojo.youtube.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.io.Serializable;

/**
 * YouTube Thumbnails
 * <p>
 * The channel, playlist, and playlist item thumbnails can be reduced to just the URL since
 * they always have the 'default' resolution (90x120).
 * </p><p>
 * TODO: fully replace 'name' string with Size enum.
 * </p>
 * <p>
 * see <a href="https://googleapis.dev/java/google-api-services-youtube/latest/com/google/api/services/youtube/model/Thumbnail.html">Thumbnail</a>
 */
@SuppressWarnings("unused")
public class Thumbnail implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public enum Size {
        DEFAULT(90, 120),
        MEDIUM(180, 320),
        HIGH(360, 480),
        STANDARD(480, 640),
        MAX_RES(720, 1280);

        private final int height;
        private final int width;

        Size(int height, int width) {
            this.height = height;
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }

    private String videoId;
    private Size size;
    private String name; // = default, medium, high, standard, maxres
    private Integer height;
    private String url;
    private Integer width;

    /**
     * Default constructor
     */
    public Thumbnail() {
    }

    public Thumbnail(String videoId, Size size, String url) {
        this.videoId = videoId;
        this.size = size;
        this.url = url;

        if (size != null) {
            this.name = size.name().toLowerCase();
            this.height = size.getHeight();
            this.width = size.getWidth();
        }
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final Thumbnail thumbnail = (Thumbnail) o;

        return new EqualsBuilder().append(videoId, thumbnail.videoId).append(name, thumbnail.name).append(height, thumbnail.height).append(url, thumbnail.url).append(width, thumbnail.width).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(videoId).append(name).append(url).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("videoId", videoId)
                .append("name", name)
                .append("url", url)
                .toString();
    }
}
