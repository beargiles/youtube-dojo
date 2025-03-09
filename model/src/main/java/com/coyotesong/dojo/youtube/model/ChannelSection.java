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

import com.coyotesong.dojo.youtube.lang3.MyToStringStyle;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * YouTube Channel Section
 *
 * `Type` will be one of
 * - channelsectiontypeundefined
 * - recentuploads
 * - popularuploads
 * - singleplaylist
 * - multipleplaylists
 * - singlechannel ?
 * - multiplechannels ?
 *
 * and possibly other values.
 *
 * See [ChannelSection](https://googleapis.dev/java/google-api-services-youtube/latest/com/google/api/services/youtube/model/ChannelSection.html)
 */
@SuppressWarnings("unused")
public class ChannelSection implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer key;
    private Integer channelKey;
    private String sectionId;
    private String etag;
    private String parentEtag;

    // from 'ChannelSectionContentDetails'
    // 'ArrayList' for serialization
    private ArrayList<String> channelIds = new ArrayList<>();
    private ArrayList<String> playlistIds = new ArrayList<>();

    // from 'ChannelSectionSnippet'
    private String channelId;
    private String lang;
    // localized.title
    private Integer position;
    private String style;
    private String title;
    private String type;
    private String hl;
    private Instant lastChecked;

    // from 'getLocalizations'
    // Map<String, ChannelSectionLocalization>

    // from 'ChannelSectionTargeting'
    // List<String> of countries, languages, and regions

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Integer getChannelKey() {
        return channelKey;
    }

    public void setChannelKey(Integer channelKey) {
        this.channelKey = channelKey;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public List<String> getPlaylistIds() {
        return playlistIds;
    }

    public void setPlaylistIds(List<String> playlistIds) {
        if (playlistIds instanceof ArrayList) {
            this.playlistIds = (ArrayList<String>) playlistIds;
        } else {
            this.playlistIds = new ArrayList<>(playlistIds);
        }
    }

    public List<String> getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(List<String> channelIds) {
        if (channelIds instanceof ArrayList) {
            this.channelIds = (ArrayList<String>) channelIds;
        } else {
            this.channelIds = new ArrayList<>(channelIds);
        }
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHl() {
        return hl;
    }

    public void setHl(String hl) {
        this.hl = hl;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getParentEtag() {
        return parentEtag;
    }

    public void setParentEtag(String parentEtag) {
        this.parentEtag = parentEtag;
    }

    public Instant getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(Instant lastChecked) {
        this.lastChecked = ZonedDateTime.ofInstant(lastChecked, ZoneOffset.UTC).toInstant().truncatedTo(ChronoUnit.SECONDS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final ChannelSection that = (ChannelSection) o;

        return new EqualsBuilder()
                // DO NOT INCLUDE 'KEY'
                //.append(key, that.key)
                //.append(channelKey, that.channelKey)
                .append(channelId, that.channelId)
                .append(position, that.position)
                .append(title, that.title)
                .append(type, that.type)
                .append(lang, that.lang)
                .append(hl, that.hl)
                .append(style, that.style)
                .append(channelIds, that.channelIds)
                .append(playlistIds, that.playlistIds)
                .append(lastChecked, that.lastChecked)
                //.append(etag, that.etag)
                //.append(parentEtag, that.parentEtag)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                // .append(etag)
                .append(channelId)
                .append(position)
                .append(title)
                .append(type)
                .toHashCode();
    }

    @Override
    public String toString() {
        // return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
        return new ToStringBuilder(this, MyToStringStyle.DEFAULT_STYLE)
                .append("key", key)
                .append("channelKey", channelKey)
                .append("sectionId", sectionId)
                // .append("etag", etag)
                .append("channelId", channelId)
                .append("title", title)
                .append("type", type)
                .append("style", style)
                .append("position", position)
                .append("channelIds", channelIds)
                .append("playlistIds", playlistIds)
                .toString();
    }
}
