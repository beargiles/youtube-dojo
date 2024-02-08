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

/**
 * Search results
 */
@SuppressWarnings("unused")
public class SearchResult {
    private String channelId;
    private String videoId;
    private String etag;
    private String description;
    private String title;
    private String liveBroadcastContent;
    private Instant publishedAt;
    private String tnUrl;
    private String channelTitle;
    private Instant lastChecked;

    public SearchResult() {
    }

    public SearchResult(Channel channel) {
        this.channelId = channel.getId();
        this.title = channel.getTitle();
        this.description = channel.getDescription();
        this.publishedAt = channel.getPublishedAt();
        this.tnUrl = channel.getTnUrl();
        this.channelTitle = channel.getTitle();
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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

        SearchResult that = (SearchResult) o;

        return new EqualsBuilder().append(channelId, that.channelId).append(videoId, that.videoId).append(etag, that.etag).append(description, that.description).append(title, that.title).append(liveBroadcastContent, that.liveBroadcastContent).append(publishedAt, that.publishedAt).append(tnUrl, that.tnUrl).append(channelTitle, that.channelTitle).append(lastChecked, that.lastChecked).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(channelId).append(videoId).append(etag).append(description).append(title).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("channelId", channelId)
                .append("videoId", videoId)
                .append("etag", etag)
                .append("description", description)
                .append("title", title)
                .append("liveBroadcastContent", liveBroadcastContent)
                .append("publishedAt", publishedAt)
                .append("tnUrl", tnUrl)
                .append("channelTitle", channelTitle)
                .append("lastChecked", lastChecked)
                .toString();
    }
}
