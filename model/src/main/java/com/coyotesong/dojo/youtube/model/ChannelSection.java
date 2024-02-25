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

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * YouTube Channel Section
 * <p>
 * 'Type' will be one of
 * <ul>
 *     <li>channelsectiontypeundefined</li>
 *     <li>recentuploads</li>
 *     <li>popularuploads</li>
 *     <li>singleplaylist</li>
 *     <li>multipleplaylists</li>
 *     <li>singlechannel ?</li>
 *     <li>multiplechannels ?</li>
 * </ul>
 * and possibly other values.
 * </p>
 * see <a href="https://googleapis.dev/java/google-api-services-youtube/latest/com/google/api/services/youtube/model/ChannelSection.html">ChannelSection</a>
 */
@SuppressWarnings("unused")
public class ChannelSection {

    private String id;
    private String etag;
    private String parentEtag;

    // from 'ChannelSectionContentDetails'
    private List<String> channelIds = new ArrayList<>();
    private List<String> playlistIds = new ArrayList<>();

    // from 'ChannelSectionSnippet'
    private String channelId;
    private String lang;
    // localized.title
    private Long position;
    private String style;
    private String title;
    private String type;
    private String hl;
    private Instant lastChecked;

    // from 'getLocalizations'
    // Map<String, ChannelSectionLocalization>

    // from 'ChannelSectionTargeting'
    // List<String> of countries, languages, and regions

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<String> getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(List<String> channelIds) {
        this.channelIds = channelIds;
    }

    public List<String> getPlaylistIds() {
        return playlistIds;
    }

    public void setPlaylistIds(List<String> playlistIds) {
        this.playlistIds = playlistIds;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
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

    public Instant getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(Instant lastChecked) {
        this.lastChecked = lastChecked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final ChannelSection that = (ChannelSection) o;

        return new EqualsBuilder()
                .append(etag, that.etag)
                .append(parentEtag, that.parentEtag)
                .append(channelIds, that.channelIds)
                .append(playlistIds, that.playlistIds)
                .append(channelId, that.channelId)
                .append(lang, that.lang)
                .append(position, that.position)
                .append(style, that.style)
                .append(title, that.title)
                .append(type, that.type)
                .append(hl, that.hl)
                .append(lastChecked, that.lastChecked)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(etag)
                .append(channelId)
                .append(title)
                .append(type)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("etag", etag)
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
