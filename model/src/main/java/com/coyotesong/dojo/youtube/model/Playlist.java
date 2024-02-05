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

import com.google.api.services.youtube.model.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.Instant;

public class Playlist {
    private static final YTUtils utils = new YTUtils();

    private String id;
    private String etag;
    private Instant publishedAt;
    private String channelId;
    private String title;
    private String description;
    private String lang;
    private String channelTitle;
    private String embedHtml;
    private String tnUrl;
    private String tnVideoId;
    private Instant lastChecked;

    private String tags;

    // tags
    // thumbnails

    // note: description is always empty and title is always "Uploads from ${channel_title}"
    // note: thumbnailVideoId seems to always be empty
    // note: 'tags' is deprecated

    public Playlist() {

    }

    public Playlist(com.google.api.services.youtube.model.Playlist playlist) {
        this.id = playlist.getId();
        this.etag = playlist.getEtag();

        final PlaylistSnippet snippet = playlist.getSnippet();
        if (snippet != null) {
            if (snippet.getPublishedAt() != null) {
                this.publishedAt = Instant.ofEpochMilli(snippet.getPublishedAt().getValue());
            }

            this.channelId = snippet.getChannelId();
            this.title = snippet.getTitle();
            this.description = snippet.getDescription();
            this.channelTitle = snippet.getChannelTitle();
            this.lang = snippet.getDefaultLanguage();

            this.tnVideoId = snippet.getThumbnailVideoId();

            if (snippet.getTags() != null) {
                this.tags = "\"" + String.join("\", \"", snippet.getTags()) + "\"";
            }

            final ThumbnailDetails td = snippet.getThumbnails();
            if ((td != null) && !td.isEmpty() && !td.getDefault().isEmpty()) {
                this.tnUrl = td.getDefault().getUrl();
            }
        }

        final PlaylistPlayer player = playlist.getPlayer();
        if (player != null) {
            this.embedHtml = player.getEmbedHtml();
        }
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

        Playlist playlist = (Playlist) o;

        return new EqualsBuilder().append(etag, playlist.etag)
                .append(channelId, playlist.channelId)
                .append(title, playlist.title)
                .append(description, playlist.description)
                .append(publishedAt, playlist.publishedAt)
                .append(tnUrl, playlist.tnUrl)
                .append(channelTitle, playlist.channelTitle)
                .append(embedHtml, playlist.embedHtml)
                .append(lastChecked, playlist.lastChecked)
                // these are often null
                .append(lang, playlist.lang)
                .append(tnVideoId, playlist.tnVideoId)
                .append(tags, playlist.tags)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(etag).append(channelId).append(title).toHashCode();
    }
}
