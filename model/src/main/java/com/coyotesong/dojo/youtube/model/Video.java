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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Video {
    private static final Logger LOG = LoggerFactory.getLogger(Video.class);
    private static final YTUtils utils = new YTUtils();

    private String id;
    private String etag;
    private String embedSrc;
    private String embedHtml;
    private Long embedHeight;
    private Long embedWidth;
    private String recordingDetails;
    private String title;
    private String projectDetails;

    // content details
    private String caption;
    private String contentRating;
    private String mpaaRating;
    private String mpaatRating;
    private String tvpgRating;
    private String ytRating;
    private String countryRestriction;
    private String definition;
    private String dimension;
    private String duration;
    private Boolean hasCustomThumbnail;
    private Boolean licensedContent;
    private String projection;
    private String regionRestrictions;

    // snippets
    private String categoryId;
    private String channelId;
    private String channelTitle;
    //defaultAutoLanguage, defaultLanguage
    private String description;
    private String lang;
    // liveBroadcastContent
    // localized
    private Instant publishedAt;

    private Map<String, Thumbnail> thumbnails = new LinkedHashMap<>();
    private List<Tag> tags = new ArrayList<>();

    // statistics
    private BigInteger commentCount;
    private BigInteger dislikeCount;
    private BigInteger favoriteCount;
    private BigInteger likeCount;
    private BigInteger viewCount;

    // status
    private Boolean embeddable;
    private String license;
    // private Boolean madeForKids;
    private String privacyStatus;
    private Boolean publicStatsViewable;
    // private Boolean selfDeclaredMadeForKids;
    private String uploadStatus;

    private Instant lastChecked;

    private List<String> topicCategories = new ArrayList<>();
    private List<String> topicIds = new ArrayList<>();
    private List<String> relevantTopicIds = new ArrayList<>();

    private Boolean nsfw;

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

    public String getEmbedSrc() {
        return embedSrc;
    }

    public void setEmbedSrc(String embedSrc) {
        this.embedSrc = embedSrc;
    }

    public String getEmbedHtml() {
        return embedHtml;
    }

    public void setEmbedHtml(String embedHtml) {
        this.embedHtml = embedHtml;
    }

    public Long getEmbedHeight() {
        return embedHeight;
    }

    public void setEmbedHeight(Long embedHeight) {
        this.embedHeight = embedHeight;
    }

    public Long getEmbedWidth() {
        return embedWidth;
    }

    public void setEmbedWidth(Long embedWidth) {
        this.embedWidth = embedWidth;
    }

    public String getRecordingDetails() {
        return recordingDetails;
    }

    public void setRecordingDetails(String recordingDetails) {
        this.recordingDetails = recordingDetails;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProjectDetails() {
        return projectDetails;
    }

    public void setProjectDetails(String projectDetails) {
        this.projectDetails = projectDetails;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getContentRating() {
        return contentRating;
    }

    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;
    }

    public String getMpaaRating() {
        return mpaaRating;
    }

    public void setMpaaRating(String mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public String getMpaatRating() {
        return mpaatRating;
    }

    public void setMpaatRating(String mpaatRating) {
        this.mpaatRating = mpaatRating;
    }

    public String getTvpgRating() {
        return tvpgRating;
    }

    public void setTvpgRating(String tvpgRating) {
        this.tvpgRating = tvpgRating;
    }

    public String getYtRating() {
        return ytRating;
    }

    public void setYtRating(String ytRating) {
        this.ytRating = ytRating;
    }

    public String getCountryRestriction() {
        return countryRestriction;
    }

    public void setCountryRestriction(String countryRestriction) {
        this.countryRestriction = countryRestriction;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Boolean getHasCustomThumbnail() {
        return hasCustomThumbnail;
    }

    public void setHasCustomThumbnail(Boolean hasCustomThumbnail) {
        this.hasCustomThumbnail = hasCustomThumbnail;
    }

    public Boolean getLicensedContent() {
        return licensedContent;
    }

    public void setLicensedContent(Boolean licensedContent) {
        this.licensedContent = licensedContent;
    }

    public String getProjection() {
        return projection;
    }

    public void setProjection(String projection) {
        this.projection = projection;
    }

    public String getRegionRestrictions() {
        return regionRestrictions;
    }

    public void setRegionRestrictions(String regionRestrictions) {
        this.regionRestrictions = regionRestrictions;
    }
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Map<String, Thumbnail> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(Map<String, Thumbnail> thumbnails) {
        this.thumbnails = thumbnails;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    // statistics

    public BigInteger getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(BigInteger commentCount) {
        this.commentCount = commentCount;
    }

    public BigInteger getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(BigInteger dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public BigInteger getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(BigInteger favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public BigInteger getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(BigInteger likeCount) {
        this.likeCount = likeCount;
    }

    public BigInteger getViewCount() {
        return viewCount;
    }

    public void setViewCount(BigInteger viewCount) {
        this.viewCount = viewCount;
    }

    // status

    public Boolean getEmbeddable() {
        return embeddable;
    }

    public void setEmbeddable(Boolean embeddable) {
        this.embeddable = embeddable;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getPrivacyStatus() {
        return privacyStatus;
    }

    public void setPrivacyStatus(String privacyStatus) {
        this.privacyStatus = privacyStatus;
    }

    public Boolean getPublicStatsViewable() {
        return publicStatsViewable;
    }

    public void setPublicStatsViewable(Boolean publicStatsViewable) {
        this.publicStatsViewable = publicStatsViewable;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public Instant getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(Instant lastChecked) {
        this.lastChecked = lastChecked;
    }

    public List<String> getTopicCategories() {
        return topicCategories;
    }

    public void setTopicCategories(List<String> topicCategories) {
        this.topicCategories = topicCategories;
    }

    public List<String> getTopicIds() {
        return topicIds;
    }

    public void setTopicIds(List<String> topicIds) {
        this.topicIds = topicIds;
    }

    public List<String> getRelevantTopicIds() {
        return relevantTopicIds;
    }

    public void setRelevantTopicIds(List<String> relevantTopicIds) {
        this.relevantTopicIds = relevantTopicIds;
    }

    public Boolean getNsfw() {
        return nsfw;
    }

    public void setNsfw(Boolean nsfw) {
        this.nsfw = nsfw;
    }

    public Video() {

    }


    public Video(com.google.api.services.youtube.model.Video video) {
        this.id = video.getId();
        this.etag = video.getEtag();
        if (video.getPlayer() != null) {
            final VideoPlayer player = video.getPlayer();
            // full embedded HTML is
            // <iframe width="480" height="270" src="//www.youtube.com/embed/XYZ" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>
            final String s = player.getEmbedHtml();
            this.embedHtml = s;
            if (StringUtils.isNotBlank(s)) {
                final Pattern pattern = Pattern.compile(".*src=\"([^\"]+)\".*");
                final Matcher m = pattern.matcher(s);
                if (m.matches()) {
                    this.embedSrc = "https:" + m.group(1);
                }
            }
            this.embedHeight = player.getEmbedHeight();
            this.embedWidth = player.getEmbedWidth();
        }

        if (video.getContentDetails() != null) {
            final VideoContentDetails details = video.getContentDetails();
            this.caption = details.getCaption();
            this.definition = details.getDefinition();
            this.dimension = details.getDimension();
            this.duration = details.getDuration();
            this.hasCustomThumbnail = details.getHasCustomThumbnail();
            this.licensedContent = details.getLicensedContent();
            this.projection = details.getProjection();

            if (details.getContentRating() != null){
                ContentRating contentRating = details.getContentRating();
                this.ytRating = contentRating.getYtRating();
                this.mpaaRating = contentRating.getMpaaRating();
                // trailers, ads, DVDs
                this.mpaatRating = contentRating.getMpaatRating();
                this.tvpgRating = contentRating.getTvpgRating();
                try {
                    this.contentRating = details.getContentRating().toPrettyString();
                    if ("{}".equals(this.contentRating)) {
                        this.contentRating = null;
                    }
                } catch (IOException e) {
                    LOG.info("error encoding content rating for video {}", id);
                }
            }
            if (details.getCountryRestriction() != null) {
                try {
                    this.countryRestriction = details.getCountryRestriction().toPrettyString();
                } catch (IOException e) {
                    LOG.info("error encoding country restriction for video {}", id);
                }
            }
            if (details.getRegionRestriction() != null) {
                try {
                    this.regionRestrictions = details.getRegionRestriction().toPrettyString();
                } catch (IOException e) {
                    LOG.info("error encoding region restriction for video {}", id);
                }
            }
        }

        if (video.getSnippet() != null) {
            final VideoSnippet snippet = video.getSnippet();
            this.categoryId = snippet.getCategoryId();
            this.channelId = snippet.getChannelId();
            this.channelTitle = snippet.getChannelTitle();
            // defaultAudioLanguage, defaultLanguage
            this.description = snippet.getDescription();
            // liveBroadcastContent, localized
            this.lang = snippet.getDefaultLanguage();
            if (snippet.getPublishedAt() != null) {
                this.publishedAt = Instant.ofEpochMilli(snippet.getPublishedAt().getValue());
            }
            /*
            if ((snippet.getTags() != null) && !snippet.getTags().isEmpty()) {
                for (String tag : snippet.getTags()) {
                    this.tags.add(new Tag(id, tag, tag));
                }
            }
             */
            this.title = snippet.getTitle();

            final ThumbnailDetails td = snippet.getThumbnails();
            if ((td != null) && !td.isEmpty()) {
                this.thumbnails.putAll(utils.convertThumbnails(id, td));
            }
        }

        if (video.getStatistics() != null) {
            final VideoStatistics statistics = video.getStatistics();
            this.dislikeCount = statistics.getDislikeCount();
            this.favoriteCount = statistics.getFavoriteCount();
            this.likeCount = statistics.getLikeCount();
            this.viewCount = statistics.getViewCount();
        }

        if (video.getStatus() != null) {
            final VideoStatus status = video.getStatus();
            this.embeddable = status.getEmbeddable();
            this.license = status.getLicense();
            // this.madeForKids = status.getMadeForKids();
            this.privacyStatus = status.getPrivacyStatus();
            this.publicStatsViewable = status.getPublicStatsViewable();
            if (status.getPublishAt() != null) {
                this.publishedAt = Instant.ofEpochMilli(status.getPublishAt().getValue());
            }
            // this.selfDeclaredMadeForKids = status.getSelfDeclaredMadeForKids();
            this.uploadStatus = status.getUploadStatus();
        }

        if (video.getRecordingDetails() != null) {
            final VideoRecordingDetails details = video.getRecordingDetails();
            details.getLocation();
            details.getLocationDescription();
            details.getRecordingDate();
            // this.recordingDetails = video.getRecordingDetails().toPrettyString();
        }

        if (video.getTopicDetails() != null) {
            final VideoTopicDetails details = video.getTopicDetails();
            if (details.getTopicCategories() != null) {
                for (String category : details.getTopicCategories()) {
                    final int ridx = category.lastIndexOf("/");
                    final String shortCategory = category.substring(ridx + 1).replace("_", " ");
                    LOG.info("category: {} -> {}", category, shortCategory);
                    topicCategories.add(shortCategory);
                }
            }
            if (details.getTopicIds() != null) {
                topicIds.addAll(details.getTopicIds());
            }
            if (details.getRelevantTopicIds() != null) {
                relevantTopicIds.addAll(details.getRelevantTopicIds());
            }
        }

        // there doesn't seem to be anything here... at least nothing standard
        /*
        if (video.getProjectDetails() != null) {
            VideoProjectDetails details = video.getProjectDetails();
            try {
                this.projectDetails = video.getProjectDetails().toPrettyString());
            } catch (IOException e) {
                //
            }
        }
         */
    }
}