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

import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.google.api.services.youtube.model.ThumbnailDetails;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

public class SearchResult {
    private Boolean subscribed;
    private String channelId;
    private String playlistId;
    private String videoId;
    private String etag;
    private String description;
    private String title;
    private String liveBroadcastContent;
    private Instant publishedAt;
    private String tnUrl;
    private String channelTitle;

    public SearchResult() {
    }

    public SearchResult(com.google.api.services.youtube.model.SearchResult result) {
        if (result.getId().getChannelId() != null) {
            this.channelId = result.getId().getChannelId();
        }
        if (result.getId().getPlaylistId() != null) {
            this.playlistId = result.getId().getPlaylistId();
        }
        if (result.getId().getVideoId() != null) {
            this.videoId = result.getId().getVideoId();
        }

        this.etag = result.getEtag();
        if (result.getSnippet() != null) {
            final SearchResultSnippet snippet = result.getSnippet();
            this.channelId = snippet.getChannelId();
            this.title = snippet.getTitle();
            this.description = snippet.getDescription();
            this.liveBroadcastContent = snippet.getLiveBroadcastContent();
            if (snippet.getPublishedAt() != null) {
                this.publishedAt = Instant.ofEpochMilli(snippet.getPublishedAt().getValue());;
            }
            final ThumbnailDetails thumbnailDetails = snippet.getThumbnails();
            if ((thumbnailDetails != null) && (thumbnailDetails.getDefault() != null)) {
                this.tnUrl = thumbnailDetails.getDefault().getUrl();
            }
            this.channelTitle = snippet.getChannelTitle();
        }
    }

    public SearchResult(Channel channel) {
        this.channelId = channel.getId();
        this.title = channel.getTitle();
        this.description = channel.getDescription();
        this.publishedAt = channel.getPublishedAt();
        this.tnUrl = channel.getTnUrl();
        this.channelTitle = channel.getTitle();
    }

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
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

    public String getLiveBroadcastContent() {
        return liveBroadcastContent;
    }

    public void setLiveBroadcastContent(String liveBroadcastContent) {
        this.liveBroadcastContent = liveBroadcastContent;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTnUrl() {
        return tnUrl;
    }

    public void setTnUrl(String tnUrl) {
        this.tnUrl = tnUrl;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String toString() {
        return String.format("%s  '%-20.20s'  '%-40.40s'", channelId, title, description);
    }
}
