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

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlaylistItem {
    private static final YTUtils utils = new YTUtils();

    private String playlistItemId;
    private String etag;
    private String playlistId;
    private Instant publishedAt;
    private String channelId;
    private String title;
    private String description;
    private String videoOwnerChannelId;
    private Long position;
    private String videoId;
    private String kind;
    private String note;
    private Instant videoPublishedAt;
    private String privacyStatus;
    private String thumbnailUrl;

    // channelTitle and videoOwnerChannelTitle can be found in 'channel' table

    public PlaylistItem() {

    }

    public PlaylistItem(com.google.api.services.youtube.model.PlaylistItem item) {
        this.playlistItemId = item.getId();
        this.etag = item.getEtag();

        final PlaylistItemSnippet snippet = item.getSnippet();
        if (snippet != null) {
            if (snippet.getPublishedAt() != null) {
                this.publishedAt = Instant.ofEpochMilli(snippet.getPublishedAt().getValue());
            }

            this.channelId = snippet.getChannelId();
            this.title = snippet.getTitle();
            this.description = snippet.getDescription();
            this.videoOwnerChannelId = snippet.getVideoOwnerChannelId();
            this.playlistId = snippet.getPlaylistId();
            this.position = snippet.getPosition();
            if (snippet.getResourceId() != null) {
                this.videoId = snippet.getResourceId().getVideoId();
                this.kind = snippet.getResourceId().getKind();
            }

            final ThumbnailDetails td = snippet.getThumbnails();
            if ((td != null) && !td.isEmpty() && !td.getDefault().isEmpty()) {
                this.thumbnailUrl = td.getDefault().getUrl();
            }
        }

        final PlaylistItemContentDetails contents = item.getContentDetails();
        if (contents != null) {
            if (this.videoId == null) {
                this.videoId = contents.getVideoId();
            }
            this.note = contents.getNote();
            if (contents.getVideoPublishedAt() != null) {
                this.videoPublishedAt = Instant.ofEpochMilli(contents.getVideoPublishedAt().getValue());
            }
        }

        final PlaylistItemStatus status = item.getStatus();
        if (status != null) {
            this.privacyStatus = status.getPrivacyStatus();
        }
    }

    public String getPlaylistItemId() {
        return playlistItemId;
    }

    public void setPlaylistItemId(String playlistItemId) {
        this.playlistItemId = playlistItemId;
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

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
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
}