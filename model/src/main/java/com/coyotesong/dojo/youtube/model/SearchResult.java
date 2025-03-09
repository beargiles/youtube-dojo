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
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Search results
 */
@SuppressWarnings("unused")
public class SearchResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String requestId = UUID.randomUUID().toString(); // artificial primary key
    private Integer position;

    private String channelId;
    private String playlistId;
    private String videoId;
    private String etag;
    private String parentEtag;
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
        this.channelId = channel.getChannelId();
        this.title = channel.getTitle();
        this.description = channel.getDescription();
        this.publishedAt = channel.getPublishedAt();
        this.tnUrl = channel.getTnUrl();
        this.channelTitle = channel.getTitle();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
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

    public String getParentEtag() {
        return parentEtag;
    }

    public void setParentEtag(String parentEtag) {
        this.parentEtag = parentEtag;
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

    public Optional<Channel> asChannel() {
        if (isBlank(channelId)) {
            return Optional.empty();
        }

        final Channel channel = new Channel();
        channel.setSummary(true);
        channel.setChannelId(channelId);
        channel.setTitle(channelTitle);
        channel.setDescription(description);
        channel.setPublishedAt(publishedAt);

        channel.setTnUrl(tnUrl);

        channel.setEtag(etag);
        channel.setLastChecked(lastChecked);

        return Optional.of(channel);
    }

    public Optional<Video> asVideo() {
        if (isBlank(videoId)) {
            return Optional.empty();
        }

        final Video video = new Video();
        video.setSummary(true);
        video.setId(videoId);
        video.setChannelId(channelId);
        video.setChannelTitle(channelTitle);
        video.setTitle(title);
        video.setDescription(description);
        video.setPublishedAt(publishedAt);

        // FIXME: verify dimensions - these may be slightly different
        final Thumbnail tn = new Thumbnail();
        tn.setVideoId(videoId);
        tn.setUrl(tnUrl);
        tn.setHeight(ThumbnailSize.DEFAULT.getHeight());
        tn.setWidth(ThumbnailSize.DEFAULT.getWidth());
        tn.setName(ThumbnailSize.DEFAULT.name());
        video.setThumbnails(Collections.singletonMap(tn.getName(), tn));

        video.setEtag(etag);
        video.setLastChecked(lastChecked);
        // video.setRequestId(requestId); --?

        return Optional.of(video);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final SearchResult that = (SearchResult) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(requestId, that.requestId)
                .append(position, that.position)
                .append(etag, that.etag)
                .append(channelId, that.channelId)
                .append(playlistId, that.playlistId)
                .append(videoId, that.videoId)
                .append(parentEtag, that.parentEtag)
                .append(description, that.description)
                .append(title, that.title)
                .append(liveBroadcastContent, that.liveBroadcastContent)
                .append(publishedAt, that.publishedAt)
                .append(tnUrl, that.tnUrl)
                .append(channelTitle, that.channelTitle)
                .append(lastChecked, that.lastChecked)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, MyToStringStyle.DEFAULT_STYLE)
                .append("id", id)
                .append("requestId", requestId)
                .append("position", position)
                .append("channelId", channelId)
                .append("playlistId", playlistId)
                .append("videoId", videoId)
                .append("title", title)
                .append("description", description)
                .append("liveBroadcastContent", liveBroadcastContent)
                .append("publishedAt", publishedAt)
                .append("tnUrl", tnUrl)
                .append("channelTitle", channelTitle)
                .append("lastChecked", lastChecked)
                // .append("etag", etag)
                .toString();
    }
}
