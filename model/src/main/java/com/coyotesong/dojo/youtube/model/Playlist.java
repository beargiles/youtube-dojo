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

/**
 * YouTube Playlist
 *
 * See [Playlist](https://googleapis.dev/java/google-api-services-youtube/latest/com/google/api/services/youtube/model/Playlist.html)
 */
@SuppressWarnings("unused")
public class Playlist implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer key;
    private Integer channelKey;

    private String id;
    private String channelId;
    private Integer position;
    private Boolean summary;  // true if search result

    private String title;
    private String description;
    private String lang;
    private Instant publishedAt;
    private String channelTitle;
    private String embedHtml;
    private String tnDefaultUrl;
    private String tnMediumUrl;
    private String tnHighUrl;
    private String tnStandardUrl;
    private String tnMaxResUrl;
    private String tnVideoId;
    private Long itemCount;

    private PlaylistImage image;

    private String etag;
    private String parentEtag;

    private Instant lastChecked;

    // note: description is always empty and title is always "Uploads from ${channel_title}"
    // note: thumbnailVideoId seems to always be empty
    // note: 'tags' is deprecated


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
        //if (publishedAt != null) {
        //    this.publishedAt = ZonedDateTime.ofInstant(publishedAt, ZoneOffset.UTC).toInstant().truncatedTo(ChronoUnit.SECONDS);
        //} else {
        //    this.publishedAt = null;
        //}
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

    public Long getItemCount() {
        return itemCount;
    }

    public void setItemCount(Long itemCount) {
        this.itemCount = itemCount;
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

    public String getTnDefaultUrl() {
        return tnDefaultUrl;
    }

    public void setTnDefaultUrl(String tnDefaultUrl) {
        this.tnDefaultUrl = tnDefaultUrl;
    }

    public String getTnMediumUrl() {
        return tnMediumUrl;
    }

    public void setTnMediumUrl(String tnMediumUrl) {
        this.tnMediumUrl = tnMediumUrl;
    }

    public String getTnHighUrl() {
        return tnHighUrl;
    }

    public void setTnHighUrl(String tnHighUrl) {
        this.tnHighUrl = tnHighUrl;
    }

    public String getTnStandardUrl() {
        return tnStandardUrl;
    }

    public void setTnStandardUrl(String tnStandardUrl) {
        this.tnStandardUrl = tnStandardUrl;
    }

    public String getTnMaxResUrl() {
        return tnMaxResUrl;
    }

    public void setTnMaxResUrl(String tnMaxResUrl) {
        this.tnMaxResUrl = tnMaxResUrl;
    }

    public String getTnVideoId() {
        return tnVideoId;
    }

    public void setTnVideoId(String tnVideoId) {
        this.tnVideoId = tnVideoId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setPlaylistImage(PlaylistImage image) {
        this.image = image;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Boolean getSummary() {
        return summary;
    }

    public void setSummary(Boolean summary) {
        this.summary = summary;
    }

    public Instant getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(Instant lastChecked) {
        this.lastChecked = lastChecked;
        //if (lastChecked != null) {
        //    this.lastChecked = ZonedDateTime.ofInstant(lastChecked, ZoneOffset.UTC).toInstant().truncatedTo(ChronoUnit.SECONDS);
        //} else {
        //    this.lastChecked = null;
        //}
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final Playlist that = (Playlist) o;

        return new EqualsBuilder()
                // DO NOT INCLUDE 'KEY'!
                .append(id, that.id)
                .append(channelId, that.channelId)
                .append(position, that.position)
                .append(summary, that.summary)
                .append(title, that.title)
                .append(description, that.description)
                // .append(itemCount, that.itemCount)
                .append(publishedAt, that.publishedAt)
                // .append(tnDefaultUrl, that.tnDefaultUrl)
                // .append(tnMediumUrl, that.tnMediumUrl)
                // .append(tnHighUrl, that.tnHighUrl)
                // .append(tnStandardUrl, that.tnStandardUrl)
                // .append(tnMaxResUrl, that.tnMaxResUrl)
                //.append(channelTitle, that.channelTitle)
                // .append(embedHtml, that.embedHtml)
                //.append(summary, that.summary)
                .append(lastChecked, that.lastChecked)
                // these are often null
                // .append(tnVideoId, that.tnVideoId)
                .append(lang, that.lang)
                //.append(etag, that.etag)
                //.append(parentEtag, that.parentEtag)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(channelId)
                .append(title)
                // .append(etag)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, MyToStringStyle.DEFAULT_STYLE)
                .append("key", key)
                .append("channelKey", channelKey)
                .append("id", id)
                .append("channelId", channelId)
                .append("position", position)
                .append("summary", summary)
                .append("title", title)
                .append("description", description)
                // .append("itemCount", itemCount)
                .append("publishedAt", publishedAt)
                //.append("channelTitle", channelTitle)
                // .append("embedHtml", embedHtml)
                // .append("tnDefaultUrl", tnDefaultUrl)
                // .append("tnMediumUrl", tnMediumUrl)
                // .append("tnHighUrl", tnHighUrl)
                // .append("tnStandardUrl", tnStandardUrl)
                // .append("tnMaxResUrl", tnMaxResUrl)
                // .append("tnVideoId", tnVideoId)
                .append("lang", lang)
                .append("lastChecked", lastChecked)
                // .append("etag", etag)
                .toString();
    }
}
