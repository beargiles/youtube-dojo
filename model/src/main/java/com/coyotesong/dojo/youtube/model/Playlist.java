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
import java.time.Instant;

/**
 * YouTube Playlist
 * <p>
 * see <a href="https://googleapis.dev/java/google-api-services-youtube/latest/com/google/api/services/youtube/model/Playlist.html">Playlist</a>
 */
@SuppressWarnings("unused")
public class Playlist implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String etag;
    private String parentEtag;
    private Instant publishedAt;
    private String channelId;
    private String title;
    private String description;
    private String lang;
    private String channelTitle;
    private String embedHtml;
    private String tnUrl;
    private String tnVideoId;
    private String hl;

    private Instant lastChecked;

    private String tags;

    // tags
    // thumbnails

    // note: description is always empty and title is always "Uploads from ${channel_title}"
    // note: thumbnailVideoId seems to always be empty
    // note: 'tags' is deprecated

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

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getEmbedHtml() {
        return embedHtml;
    }

    public void setEmbedHtml(String embedHtml) {
        this.embedHtml = embedHtml;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTnUrl() {
        return tnUrl;
    }

    public void setTnUrl(String tnUrl) {
        this.tnUrl = tnUrl;
    }

    public String getTnVideoId() {
        return tnVideoId;
    }

    public void setTnVideoId(String tnVideoId) {
        this.tnVideoId = tnVideoId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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

        final Playlist that = (Playlist) o;

        return new EqualsBuilder().append(etag, that.etag)
                .append(parentEtag, that.parentEtag)
                .append(channelId, that.channelId)
                .append(title, that.title)
                .append(description, that.description)
                .append(publishedAt, that.publishedAt)
                .append(tnUrl, that.tnUrl)
                .append(channelTitle, that.channelTitle)
                .append(embedHtml, that.embedHtml)
                .append(lastChecked, that.lastChecked)
                // these are often null
                .append(lang, that.lang)
                .append(tnVideoId, that.tnVideoId)
                .append(tags, that.tags)
                .append(hl, that.hl)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(etag)
                .append(channelId)
                .append(title)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("etag", etag)
                .append("publishedAt", publishedAt)
                .append("channelId", channelId)
                .append("title", title)
                .append("description", description)
                .append("lang", lang)
                .append("channelTitle", channelTitle)
                .append("embedHtml", embedHtml)
                .append("tnUrl", tnUrl)
                .append("tnVideoId", tnVideoId)
                .append("lastChecked", lastChecked)
                .append("tags", tags)
                .toString();
    }
}
