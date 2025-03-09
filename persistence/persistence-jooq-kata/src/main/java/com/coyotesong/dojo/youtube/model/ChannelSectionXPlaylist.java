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
 * Join table...
 */
public class ChannelSectionXPlaylist implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer parentChannelKey;
    private Integer parentPosition;
    private String playlistId;
    private Integer position;
    private String etag;

    public Integer getParentChannelKey() {
        return parentChannelKey;
    }

    public void setParentChannelKey(Integer parentChannelKey) {
        this.parentChannelKey = parentChannelKey;
    }

    public Integer getParentPosition() {
        return parentPosition;
    }

    public void setParentPosition(Integer parentPosition) {
        this.parentPosition = parentPosition;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ChannelSectionXPlaylist that)) return false;

        return new EqualsBuilder()
                .append(parentChannelKey, that.parentChannelKey)
                .append(parentPosition, that.parentPosition)
                .append(playlistId, that.playlistId)
                .append(position, that.position)
                .append(etag, that.etag)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(parentChannelKey)
                .append(parentPosition)
                .append(playlistId)
                .append(position)
                .append(etag)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("parentChannelKey", parentChannelKey)
                .append("parentPosition", parentPosition)
                .append("playlistId", playlistId)
                .append("position", position)
                // .append("etag", etag)
                .toString();
    }
}
