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
import java.util.ArrayList;
import java.util.List;

/**
 * YouTube Channel
 * <p>
 * see <a href="https://googleapis.dev/java/google-api-services-youtube/latest/com/google/api/services/youtube/model/Channel.html">Channel</a>
 */
@SuppressWarnings("unused")
public class Channel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String etag;
    private String parentEtag;

    private String username;
    private String contentOwner;
    private String country;
    private String customUrl;
    private String lang;
    private Instant publishedAt;
    private String description;
    private String title;
    private String uploads;
    private String tnUrl;
    private String hl;

    private Boolean nsfw;
    private Instant lastChecked;

    private ArrayList<Playlist> playlists;

    // wikipedia
    private ArrayList<WikipediaTopic> topicCategories;
    // topicIds = freebase topic ids
    private ArrayList<String> topicIds;

    // statistics = commentCount, hiddenSubscriberCount(boolean), subscriberCount, videoCount, viewCount),
    // status = isLinked, longUploadStatus, madeForKids, privacyStatus, selfDeclaredMadeForKids

    // values that seem to be null in the channels I care: view_count, favorites, likes, watch_history, watch_later

    private ArrayList<Video> videos;

    public Channel() {
        // fields are explicitly ArrayList<> due to serialization warnings
        this.playlists = new ArrayList<>();
        this.topicCategories = new ArrayList<>();
        this.topicIds = new ArrayList<>();
        this.videos = new ArrayList<>();
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContentOwner() {
        return contentOwner;
    }

    public void setContentOwner(String contentOwner) {
        this.contentOwner = contentOwner;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCustomUrl() {
        return customUrl;
    }

    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUploads() {
        return uploads;
    }

    public void setUploads(String uploads) {
        this.uploads = uploads;
    }

    public String getTnUrl() {
        return tnUrl;
    }

    public void setTnUrl(String tnUrl) {
        this.tnUrl = tnUrl;
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

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        if (playlists instanceof ArrayList) {
            this.playlists = (ArrayList<Playlist>) playlists;
        } else {
            this.playlists.clear();
            this.playlists.addAll(playlists);
        }
    }

    public List<WikipediaTopic> getTopicCategories() {
        return topicCategories;
    }

    public void setTopicCategories(List<WikipediaTopic> topicCategories) {
        if (topicCategories instanceof ArrayList) {
            this.topicCategories = (ArrayList<WikipediaTopic>) topicCategories;
        } else {
            this.topicCategories.clear();
            this.topicCategories.addAll(topicCategories);
        }
    }

    public List<String> getTopicIds() {
        return topicIds;
    }

    public void setTopicIds(List<String> topicIds) {
        if (topicIds instanceof ArrayList) {
            this.topicIds = (ArrayList<String>) topicIds;
        } else {
            this.topicIds.clear();
            this.topicIds.addAll(topicIds);
        }
    }

    public Boolean getNsfw() {
        return nsfw;
    }

    public void setNsfw(Boolean nsfw) {
        this.nsfw = nsfw;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        if (videos instanceof ArrayList) {
            this.videos = (ArrayList<Video>) videos;
        } else {
            this.videos.clear();
            this.videos.addAll(videos);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final Channel that = (Channel) o;

        return new EqualsBuilder().append(etag, that.etag)
                .append(customUrl, that.customUrl)
                .append(title, that.title)
                .append(description, that.description)
                .append(publishedAt, that.publishedAt)
                .append(uploads, that.uploads)
                .append(tnUrl, that.tnUrl)
                .append(lastChecked, that.lastChecked)
                // these are often null
                .append(hl, that.hl)
                .append(parentEtag, that.parentEtag)
                .append(contentOwner, that.contentOwner)
                .append(lang, that.lang)
                .append(country, that.country)
                .append(username, that.username)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(etag)
                .append(customUrl)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("etag", etag)
                .append("username", username)
                .append("contentOwner", contentOwner)
                .append("country", country)
                .append("customUrl", customUrl)
                .append("lang", lang)
                .append("publishedAt", publishedAt)
                .append("description", description)
                .append("title", title)
                .append("uploads", uploads)
                .append("tnUrl", tnUrl)
                .append("hl", hl)
                .append("nsfw", nsfw)
                .append("lastChecked", lastChecked)
                .append("playlists", playlists)
                .append("topicCategories", topicCategories)
                .append("topicIds", topicIds)
                .append("videos", videos)
                .toString();
    }
}
