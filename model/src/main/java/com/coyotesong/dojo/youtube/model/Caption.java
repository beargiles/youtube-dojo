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
 * YouTube Caption
 * <p>
 * see <a href="https://googleapis.dev/java/google-api-services-youtube/latest/com/google/api/services/youtube/model/Caption.html">Caption</a>
 */
@SuppressWarnings("unused")
public class Caption implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String etag;

    private String audioTrackType;
    private String failureReason;
    private Boolean isAutoSynced;
    private Boolean isCc;
    private Boolean isDraft;
    private Boolean isEasyReader;
    private Boolean isLarge;
    private String Language;
    private Instant lastUpdated;
    private String name;
    private String status;
    private String trackKind;
    private String videoId;

    private Instant lastChecked;

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

    public String getAudioTrackType() {
        return audioTrackType;
    }

    public void setAudioTrackType(String audioTrackType) {
        this.audioTrackType = audioTrackType;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public Boolean getAutoSynced() {
        return isAutoSynced;
    }

    public void setAutoSynced(Boolean autoSynced) {
        isAutoSynced = autoSynced;
    }

    public Boolean getCc() {
        return isCc;
    }

    public void setCc(Boolean cc) {
        isCc = cc;
    }

    public Boolean getDraft() {
        return isDraft;
    }

    public void setDraft(Boolean draft) {
        isDraft = draft;
    }

    public Boolean getEasyReader() {
        return isEasyReader;
    }

    public void setEasyReader(Boolean easyReader) {
        isEasyReader = easyReader;
    }

    public Boolean getLarge() {
        return isLarge;
    }

    public void setLarge(Boolean large) {
        isLarge = large;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrackKind() {
        return trackKind;
    }

    public void setTrackKind(String trackKind) {
        this.trackKind = trackKind;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
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

        Caption caption = (Caption) o;

        return new EqualsBuilder().append(etag, caption.etag).append(audioTrackType, caption.audioTrackType).append(failureReason, caption.failureReason).append(isAutoSynced, caption.isAutoSynced).append(isCc, caption.isCc).append(isDraft, caption.isDraft).append(isEasyReader, caption.isEasyReader).append(isLarge, caption.isLarge).append(Language, caption.Language).append(lastUpdated, caption.lastUpdated).append(name, caption.name).append(status, caption.status).append(trackKind, caption.trackKind).append(videoId, caption.videoId).append(lastChecked, caption.lastChecked).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(etag).append(name).append(videoId).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("etag", etag)
                .append("audioTrackType", audioTrackType)
                .append("failureReason", failureReason)
                .append("isAutoSynced", isAutoSynced)
                .append("isCc", isCc)
                .append("isDraft", isDraft)
                .append("isEasyReader", isEasyReader)
                .append("isLarge", isLarge)
                .append("Language", Language)
                .append("lastUpdated", lastUpdated)
                .append("name", name)
                .append("status", status)
                .append("trackKind", trackKind)
                .append("videoId", videoId)
                .append("lastChecked", lastChecked)
                .toString();
    }
}
