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
 * Channel information provided on 'Subscriptions' page on YouTube website.
 *
 * See [Channels](https://youtube.com/feeds/channels)
 */
public class ChannelSummary implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;  // FIXME: rename to id
    private String username;
    private String baseUrl;  // or customUrl
    private String description;
    private String title;
    private String tn88;
    private String tn176;

    // not present (see Channel)
    // private String etag;
    // private String parentEtag;
    // private String contentOwner;
    // private String country;
    // private String lang;
    // private Instant publishedAt;
    // private String uploads;
    // private String hl;

    // additional content
    private String shortByline;
    private String longByline;
    private String subscriberCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortByline() {
        return shortByline;
    }

    public void setShortByline(String shortByline) {
        this.shortByline = shortByline;
    }

    public String getLongByline() {
        return longByline;
    }

    public void setLongByline(String longByline) {
        this.longByline = longByline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTn88() {
        return tn88;
    }

    public void setTn88(String tn88) {
        this.tn88 = tn88;
    }

    public String getTn176() {
        return tn176;
    }

    public void setTn176(String tn176) {
        this.tn176 = tn176;
    }

    public String getSubscriberCount() {
        return subscriberCount;
    }

    public void setSubscriberCount(String subscriberCount) {
        this.subscriberCount = subscriberCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final ChannelSummary that = (ChannelSummary) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(baseUrl, that.baseUrl)
                .append(title, that.title)
                .append(description, that.description)
                //.append(tn88Url, that.tn88Url)
                // these are often null
                .append(username, that.username)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(baseUrl)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("username", username)
                .append("baseUrl", baseUrl)
                .append("description", description)
                .append("title", title)
                .toString();
    }
}
