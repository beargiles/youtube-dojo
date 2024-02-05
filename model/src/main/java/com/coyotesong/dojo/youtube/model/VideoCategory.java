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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class VideoCategory {
    private Integer id;
    private Integer categoryId;
    private String etag;
    private String title;
    private Boolean assignable;
    private String lang;

    private String parentEtag;

    /**
     * Default constructor
     */
    public VideoCategory() {

    }

    /**
     * Copy constructor
     *
     * @param parentEtag
     * @param category
     * @param lang
     */
    public VideoCategory(String parentEtag, com.google.api.services.youtube.model.VideoCategory category, String lang) {
        this.parentEtag = parentEtag;
        this.etag = category.getEtag();
        this.categoryId = Integer.valueOf(category.getId(), 10);
        this.title = category.getSnippet().getTitle();
        this.assignable = category.getSnippet().getAssignable();
        this.lang = lang;

        // category.kind is always "youtube@videoCategory"
        // category.snippet.channelId is always '"UCBR8-60-B28hp2BmDPdntcQ"
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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
                .append(categoryId, that.categoryId)
                .append(title, that.title)
                .append(assignable, that.assignable)
                .append(parentEtag, that.parentEtag)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(etag).append(lang).append(categoryId).toHashCode();
    }
}
