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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * YouTube Channel
 *
 * See [Channels:List](https://developers.google.com/youtube/v3/docs/channels/list)
 *
 * See [Channel API](https://googleapis.dev/java/google-api-services-youtube/latest/com/google/api/services/youtube/model/Channel.html)
 */
@SuppressWarnings("unused")
public class Channel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(Channel.class);

    private Integer key;
    private String channelId;
    private Boolean summary; // true if search results

    private String handle;
    private String contentOwner;
    private String country;
    private String lang;
    private Instant publishedAt;
    private String description;
    private String title;
    private Long videoCount;
    private Long subscriberCount;
    private Long viewCount;
    private String uploads;
    private String tnUrl;
    private String hl;

    private String category;
    private String etag;
    private String parentEtag;
    private Boolean nsfw;
    private Instant lastChecked;

    private ArrayList<Playlist> playlists = new ArrayList<>();
    private ArrayList<ChannelSection> sections = new ArrayList<>();

    // wikipedia
    private ArrayList<WikipediaTopic> topicCategories = new ArrayList<>();
    // topicIds = freebase topic ids
    private ArrayList<String> topicIds = new ArrayList<>();

    // statistics = commentCount, hiddenSubscriberCount(boolean), subscriberCount, videoCount, viewCount),
    // status = isLinked, longUploadStatus, madeForKids, privacyStatus, selfDeclaredMadeForKids

    // values that seem to be null in the channels I care: view_count, favorites, likes, watch_history, watch_later

    private ArrayList<Video> videos = new ArrayList<>();

    public Channel() {
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
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
        this.publishedAt = ZonedDateTime.ofInstant(publishedAt, ZoneOffset.UTC).toInstant().truncatedTo(ChronoUnit.SECONDS);
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

    public Long getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(Long videoCount) {
        this.videoCount = videoCount;
    }

    public Long getSubscriberCount() {
        return subscriberCount;
    }

    public void setSubscriberCount(Long subscriberCount) {
        this.subscriberCount = subscriberCount;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
            if (this.playlists == null) {
                // some database frameworks don't properly initialize the object
                this.playlists = new ArrayList<>();
            }

            this.playlists.clear();
            if (playlists != null) {
                this.playlists.addAll(playlists);
            }
        }
    }

    public List<WikipediaTopic> getTopicCategories() {
        return topicCategories;
    }

    public void setTopicCategories(List<WikipediaTopic> topicCategories) {
        if (topicCategories instanceof ArrayList) {
            this.topicCategories = (ArrayList<WikipediaTopic>) topicCategories;
        } else {
            if (this.topicCategories == null) {
                // some database frameworks don't properly initialize the object
                this.topicCategories = new ArrayList<>();
            }

            this.topicCategories.clear();
            if (topicCategories != null) {
                this.topicCategories.addAll(topicCategories);
            }
        }
    }

    public List<String> getTopicIds() {
        return topicIds;
    }

    public void setTopicIds(List<String> topicIds) {
        if (topicIds instanceof ArrayList) {
            this.topicIds = (ArrayList<String>) topicIds;
        } else {
            if (this.topicIds == null) {
                // some database frameworks don't properly initialize the object
                this.topicIds = new ArrayList<>();
            }

            this.topicIds.clear();
            if (topicIds != null) {
                this.topicIds.addAll(topicIds);
            }
        }
    }

    public Boolean getSummary() {
        return summary;
    }

    public void setSummary(Boolean summary) {
        this.summary = summary;
    }

    public Boolean getNsfw() {
        return nsfw;
    }

    public void setNsfw(Boolean nsfw) {
        this.nsfw = nsfw;
    }

    public ArrayList<ChannelSection> getSections() {
        return sections;
    }

    public void setSections(List<ChannelSection> sections) {
        if (sections instanceof ArrayList) {
            this.sections = (ArrayList<ChannelSection>) sections;
        } else {
            if (this.sections == null) {
                // some database frameworks don't properly initialize the object
                this.sections = new ArrayList<>();
            }

            this.sections.clear();
            if (sections != null) {
                this.sections.addAll(sections);
            }
        }
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        if (videos instanceof ArrayList) {
            this.videos = (ArrayList<Video>) videos;
        } else {
            if (this.videos == null) {
                // some database frameworks don't properly initialize the object
                this.videos = new ArrayList<>();
            }

            this.videos.clear();
            if (videos != null) {
                this.videos.addAll(videos);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final Channel that = (Channel) o;

        return new EqualsBuilder()
                // DO NOT INCLUDE 'KEY'
                .append(channelId, that.channelId)
                .append(handle, that.handle)
                .append(title, that.title)
                .append(description, that.description)
                .append(publishedAt, that.publishedAt)
                .append(videoCount, that.videoCount)
                .append(subscriberCount, that.subscriberCount)
                .append(viewCount, that.viewCount)
                .append(uploads, that.uploads)
                .append(tnUrl, that.tnUrl)
                // .append(lastChecked, that.lastChecked)
                .append(summary, that.summary)
                // these are often null
                .append(hl, that.hl)
                // .append(parentEtag, that.parentEtag)
                .append(contentOwner, that.contentOwner)
                .append(lang, that.lang)
                .append(country, that.country)
                .append(nsfw, that.nsfw)
                // collections...
                .append(sections, that.sections)
                .append(playlists, that.playlists)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(channelId)
                .append(handle)
                .toHashCode();
    }

    @Override
    public String toString() {
        // return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
        return new ToStringBuilder(this, MyToStringStyle.DEFAULT_STYLE)
                .append("key", key)
                .append("channelId", channelId)
                .append("handle", handle)
                .append("summary", summary)
                .append("title", title)
                .append("description", description)
                .append("country", country)
                .append("publishedAt", publishedAt)
                .append("videoCount", videoCount)
                .append("subscriberCount", subscriberCount)
                .append("viewCount", viewCount)
                .append("uploads", uploads)
                .append("contentOwner", contentOwner)
                .append("tnUrl", tnUrl)
                .append("lang", lang)
                .append("hl", hl)
                .append("nsfw", nsfw)
                // .append("lastChecked", lastChecked)
                .append("sections", sections)
                .append("playlists", playlists)
                .append("topicCategories", topicCategories)
                .append("topicIds", topicIds)
                .append("videos", videos)
                // .append("etag", etag)
                // .append("parent_etag", parentEtag)
                .toString();
    }
}
