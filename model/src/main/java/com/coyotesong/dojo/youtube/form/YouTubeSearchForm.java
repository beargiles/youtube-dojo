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

package com.coyotesong.dojo.youtube.form;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.cache.interceptor.SimpleKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Channel search form.
 *
 * @see "https://developers.google.com/youtube/v3/docs/search/list"
 */
@SuppressWarnings("unused")
public class YouTubeSearchForm {
    private List<String> types = new ArrayList<>(); // channel, playlist, video ?
    private String query;  // can include both OR (|) and NOT (-)  URL-escape for | is '%7C'

    private String channelId;
    private String channelType;  // 'any', 'show'
    private String eventType; // 'complete', 'live', 'upcoming'
    private String order = "relevance"; // date, rating, relevance, title, videoCount, viewCount  (relevance is default)
    private String publishedAfter;  // ISO format (RFC-3339)
    private String publishedBefore;  // ISO format (RFC-3339)
    private String regionCode;  // ISO 3166-1 alpha-2 country code
    private String lang; // ISO 639-1 two-letter language code
    private String safeSearch = "moderate"; // none, moderate, strict  (moderate is default)
    private String topicId;  // freebase id

    // private Boolean forContentOwner;
    // private Boolean forDeveloper;
    // private Boolean forMine;
    // private Boolean onBehalfOfContentOwner;

    // private String location;  // lat/long
    // private String locationRadius;  // xm, xkm, xft, xmi
    // private Integer maxResults; // default 5

    protected YouTubeSearchForm(String type) {
        this.types.add(type);
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getPublishedAfter() {
        return publishedAfter;
    }

    public void setPublishedAfter(String publishedAfter) {
        this.publishedAfter = publishedAfter;
    }

    public String getPublishedBefore() {
        return publishedBefore;
    }

    public void setPublishedBefore(String publishedBefore) {
        this.publishedBefore = publishedBefore;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getSafeSearch() {
        return safeSearch;
    }

    public void setSafeSearch(String safeSearch) {
        this.safeSearch = safeSearch;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public SimpleKey generateKey() {
        // does not (yet) include eventType, region, etc.
        return new SimpleKey("search", channelId, lang, order, safeSearch, topicId, query);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        YouTubeSearchForm that = (YouTubeSearchForm) o;

        return new EqualsBuilder().append(types, that.types).append(query, that.query).append(channelId, that.channelId).append(channelType, that.channelType).append(eventType, that.eventType).append(order, that.order).append(publishedAfter, that.publishedAfter).append(publishedBefore, that.publishedBefore).append(regionCode, that.regionCode).append(lang, that.lang).append(safeSearch, that.safeSearch).append(topicId, that.topicId).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(types).append(query).append(channelId).append(channelType).append(eventType).append(order).append(publishedAfter).append(publishedBefore).append(regionCode).append(lang).append(safeSearch).append(topicId).toHashCode();
    }
}
