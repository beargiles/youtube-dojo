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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * YouTube video
 * <p>
 * This class has been extended with a 'NSFW' flag.
 * </p>
 * <p>
 * see <a href="https://googleapis.dev/java/google-api-services-youtube/latest/com/google/api/services/youtube/model/Video.html">Video</a>
 */
@SuppressWarnings("unused")
public class Video implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(Video.class);

    private String id;
    private String etag;
    private String parentEtag;
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

    private LinkedHashMap<String, Thumbnail> thumbnails;
    private ArrayList<Tag> tags;

    // statistics
    private BigInteger commentCount;
    private BigInteger dislikeCount;
    private BigInteger favoriteCount;
    private BigInteger likeCount;
    private BigInteger viewCount;

    // status
    private Boolean embeddable;
    private String license;
    private Boolean madeForKids;
    private String privacyStatus;
    private Boolean publicStatsViewable;
    private Boolean selfDeclaredMadeForKids;
    private String uploadStatus;

    private String hl;
    private Instant lastChecked;

    private ArrayList<String> topicCategories;
    private ArrayList<String> topicIds;
    private ArrayList<String> relevantTopicIds;

    private Boolean nsfw;

    public Video() {
        // fields are explicitly ArrayList<> due to serialization warnings
        this.thumbnails = new LinkedHashMap<>();
        this.tags = new ArrayList<>();
        this.topicCategories = new ArrayList<>();
        this.topicIds = new ArrayList<>();
        this.relevantTopicIds = new ArrayList<>();
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
        if (thumbnails instanceof LinkedHashMap<String, Thumbnail>) {
            this.thumbnails = (LinkedHashMap<String, Thumbnail>) thumbnails;
        } else {
            this.thumbnails.clear();
            this.thumbnails.putAll(thumbnails);
        }
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        if (tags instanceof ArrayList) {
            this.tags = (ArrayList<Tag>) tags;
        } else {
            this.tags.clear();
            this.tags.addAll(tags);
        }
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

    public Boolean getMadeForKids() {
        return madeForKids;
    }

    public void setMadeForKids(Boolean madeForKids) {
        this.madeForKids = madeForKids;
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

    public Boolean getSelfDeclaredMadeForKids() {
        return selfDeclaredMadeForKids;
    }

    public void setSelfDeclaredMadeForKids(Boolean selfDeclaredMadeForKids) {
        this.selfDeclaredMadeForKids = selfDeclaredMadeForKids;
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
        if (topicCategories instanceof ArrayList) {
            this.topicCategories = (ArrayList<String>) topicCategories;
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

    public List<String> getRelevantTopicIds() {
        return relevantTopicIds;
    }

    public void setRelevantTopicIds(List<String> relevantTopicIds) {
        if (relevantTopicIds instanceof ArrayList) {
            this.relevantTopicIds = (ArrayList<String>) relevantTopicIds;
        } else {
            this.relevantTopicIds.clear();
            this.relevantTopicIds.addAll(relevantTopicIds);
        }
    }

    public String getHl() {
        return hl;
    }

    public void setHl(String hl) {
        this.hl = hl;
    }

    public Boolean getNsfw() {
        return nsfw;
    }

    public void setNsfw(Boolean nsfw) {
        this.nsfw = nsfw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final Video that = (Video) o;

        return new EqualsBuilder()
                .append(etag, that.etag)
                .append(embedSrc, that.embedSrc)
                .append(embedHtml, that.embedHtml)
                .append(embedHeight, that.embedHeight)
                .append(embedWidth, that.embedWidth)
                .append(recordingDetails, that.recordingDetails)
                .append(title, that.title)
                .append(projectDetails, that.projectDetails)
                .append(caption, that.caption)
                .append(contentRating, that.contentRating)
                .append(mpaaRating, that.mpaaRating)
                .append(mpaatRating, that.mpaatRating)
                .append(tvpgRating, that.tvpgRating)
                .append(ytRating, that.ytRating)
                .append(countryRestriction, that.countryRestriction)
                .append(definition, that.definition)
                .append(dimension, that.dimension)
                .append(duration, that.duration)
                .append(hasCustomThumbnail, that.hasCustomThumbnail)
                .append(licensedContent, that.licensedContent)
                .append(projection, that.projection)
                .append(regionRestrictions, that.regionRestrictions)
                .append(categoryId, that.categoryId)
                .append(channelId, that.channelId)
                .append(channelTitle, that.channelTitle)
                .append(description, that.description)
                .append(lang, that.lang)
                .append(publishedAt, that.publishedAt)
                .append(thumbnails, that.thumbnails)
                .append(tags, that.tags)
                .append(commentCount, that.commentCount)
                .append(dislikeCount, that.dislikeCount)
                .append(favoriteCount, that.favoriteCount)
                .append(likeCount, that.likeCount)
                .append(viewCount, that.viewCount)
                .append(embeddable, that.embeddable)
                .append(license, that.license)
                .append(privacyStatus, that.privacyStatus)
                .append(publicStatsViewable, that.publicStatsViewable)
                .append(uploadStatus, that.uploadStatus)
                .append(lastChecked, that.lastChecked)
                .append(topicCategories, that.topicCategories)
                .append(topicIds, that.topicIds)
                .append(relevantTopicIds, that.relevantTopicIds)
                .append(hl, that.hl)
                .append(nsfw, that.nsfw)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(etag)
                .append(title)
                .append(channelId)
                .append(description)
                .append(lang)
                .append(publishedAt)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("etag", etag)
                .append("parentEtag", parentEtag)
                .append("embedSrc", embedSrc)
                .append("embedHtml", embedHtml)
                .append("embedHeight", embedHeight)
                .append("embedWidth", embedWidth)
                .append("recordingDetails", recordingDetails)
                .append("title", title)
                .append("projectDetails", projectDetails)
                .append("caption", caption)
                .append("contentRating", contentRating)
                .append("mpaaRating", mpaaRating)
                .append("mpaatRating", mpaatRating)
                .append("tvpgRating", tvpgRating)
                .append("ytRating", ytRating)
                .append("countryRestriction", countryRestriction)
                .append("definition", definition)
                .append("dimension", dimension)
                .append("duration", duration)
                .append("hasCustomThumbnail", hasCustomThumbnail)
                .append("licensedContent", licensedContent)
                .append("projection", projection)
                .append("regionRestrictions", regionRestrictions)
                .append("categoryId", categoryId)
                .append("channelId", channelId)
                .append("channelTitle", channelTitle)
                .append("description", description)
                .append("lang", lang)
                .append("publishedAt", publishedAt)
                .append("thumbnails", thumbnails)
                .append("tags", tags)
                .append("commentCount", commentCount)
                .append("dislikeCount", dislikeCount)
                .append("favoriteCount", favoriteCount)
                .append("likeCount", likeCount)
                .append("viewCount", viewCount)
                .append("embeddable", embeddable)
                .append("license", license)
                .append("privacyStatus", privacyStatus)
                .append("publicStatsViewable", publicStatsViewable)
                .append("uploadStatus", uploadStatus)
                .append("lastChecked", lastChecked)
                .append("topicCategories", topicCategories)
                .append("topicIds", topicIds)
                .append("relevantTopicIds", relevantTopicIds)
                .append("nsfw", nsfw)
                .toString();
    }
}