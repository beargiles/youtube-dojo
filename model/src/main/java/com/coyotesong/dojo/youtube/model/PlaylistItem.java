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
 * YouTube playlist item
 * <p>
 * see <a href="https://googleapis.dev/java/google-api-services-youtube/latest/com/google/api/services/youtube/model/PlaylistItem.html">PlaylistItem</a>
 */
@SuppressWarnings("unused")
public class PlaylistItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String etag;
    private String playlistId;
    private Instant publishedAt;
    private String channelId;
    private String channelTitle;
    private String title;
    private String description;
    private String videoOwnerChannelId;
    private Long position;
    private String videoId;
    private String note;
    private Instant videoPublishedAt;
    private String privacyStatus;
    private String thumbnailUrl;

    // channelTitle and videoOwnerChannelTitle can be found in 'channel' table

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

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
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

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
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

    public String getVideoOwnerChannelId() {
        return videoOwnerChannelId;
    }

    public void setVideoOwnerChannelId(String videoOwnerChannelId) {
        this.videoOwnerChannelId = videoOwnerChannelId;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Instant getVideoPublishedAt() {
        return videoPublishedAt;
    }

    public void setVideoPublishedAt(Instant videoPublishedAt) {
        this.videoPublishedAt = videoPublishedAt;
    }

    public String getPrivacyStatus() {
        return privacyStatus;
    }

    public void setPrivacyStatus(String privacyStatus) {
        this.privacyStatus = privacyStatus;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PlaylistItem that = (PlaylistItem) o;

        return new EqualsBuilder().append(etag, that.etag).append(playlistId, that.playlistId).append(publishedAt, that.publishedAt).append(channelId, that.channelId).append(channelTitle, that.channelTitle).append(title, that.title).append(description, that.description).append(videoOwnerChannelId, that.videoOwnerChannelId).append(position, that.position).append(videoId, that.videoId).append(note, that.note).append(videoPublishedAt, that.videoPublishedAt).append(privacyStatus, that.privacyStatus).append(thumbnailUrl, that.thumbnailUrl).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(etag).append(playlistId).append(title).append(description).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("etag", etag)
                .append("playlistId", playlistId)
                .append("publishedAt", publishedAt)
                .append("channelId", channelId)
                .append("channelTitle", channelTitle)
                .append("title", title)
                .append("description", description)
                .append("videoOwnerChannelId", videoOwnerChannelId)
                .append("position", position)
                .append("videoId", videoId)
                .append("note", note)
                .append("videoPublishedAt", videoPublishedAt)
                .append("privacyStatus", privacyStatus)
                .append("thumbnailUrl", thumbnailUrl)
                .toString();
    }
}