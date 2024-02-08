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

/**
 * YouTube VideoCategory
 * <p>
 * Note: 'channelId' appears to always be 'UCBR8-60-B28hp2BmDPdntcQ'
 * </p>
 * <p>
 * see <a href="https://googleapis.dev/java/google-api-services-youtube/latest/com/google/api/services/youtube/model/VideoCategory.html">VideoCategory</a>
 */
@SuppressWarnings("unused")
public class VideoCategory implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String channelId;
    private String etag;
    private String title;
    private Boolean assignable;
    private String lang;

    private String parentEtag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getAssignable() {
        return assignable;
    }

    public void setAssignable(Boolean assignable) {
        this.assignable = assignable;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getParentEtag() {
        return parentEtag;
    }

    public void setParentEtag(String parentEtag) {
        this.parentEtag = parentEtag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        VideoCategory that = (VideoCategory) o;

        return new EqualsBuilder()
                .append(etag, that.etag)
                .append(lang, that.lang)
                .append(channelId, that.channelId)
                .append(title, that.title)
                .append(assignable, that.assignable)
                .append(parentEtag, that.parentEtag)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(etag).append(lang).append(channelId).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("etag", etag)
                .append("lang", lang)
                .append("title", title)
                .append("assignable", assignable)
                .append("parentEtag", parentEtag)
                .toString();
    }
}
