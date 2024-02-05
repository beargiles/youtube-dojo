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

package com.coyotesong.dojo.youtube.service.youtubeClient;

import com.coyotesong.dojo.youtube.form.YouTubeSearchForm;
import com.google.api.services.youtube.YouTube;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Search client factory
 */
@Component
@SuppressWarnings("unused")
public class ClientForSubscriptionListFactory {
    private final YouTube.Builder ytBuilder;

    public ClientForSubscriptionListFactory(YouTube.Builder ytBuilder) {
        this.ytBuilder = ytBuilder;
    }

    public Builder newBuilder() {
        return new Builder(ytBuilder);
    }

    public static class Builder {
        private static final List<String> SEARCH_LIST_PARTS = Arrays.asList("snippet");

        private final YouTube.Builder ytBuilder;

        private List<String> types = new ArrayList<>();
        private Long maxResults = 50L;
        private String channelId;
        private String query;
        private String publishedBefore;
        private String publishedAfter;
        private String channelType;
        private String eventType;
        private String order;
        private String lang;


        private String hl;

        private Builder(YouTube.Builder ytBuilder) {
            this.ytBuilder = ytBuilder;
        }

        public Builder withForm(YouTubeSearchForm form) {
            this.types = form.getTypes();
            this.maxResults = 50L;

            // concrete values
            if (isNotBlank(form.getChannelId())) {
                this.channelId = form.getChannelId();
            }
            // if (isNotBlank(form.getLocation())) {
            //     request.setLocationform.getLocation());
            // }
            // if (isNotBlank(form.getLocationRadius())) {
            //     request.setLocationRadiusform.getLocationRadius());
            // }
            if (isNotBlank(form.getQuery())) {
                this.query = form.getQuery();
            }

            // temporal. Should check format
            if (isNotBlank(form.getPublishedBefore())) {
                this.publishedBefore = form.getPublishedBefore();
            }

            if (isNotBlank(form.getPublishedAfter())) {
                this.publishedAfter = form.getPublishedAfter();
            }

            // enums
            if (isNotBlank(form.getChannelType())) {
                this.channelType = form.getChannelType();
            }
            if (isNotBlank(form.getEventType())) {
                this.eventType = form.getEventType();
            }
            if (isNotBlank(form.getOrder())) {
                this.order = form.getOrder();
            }
            if (isNotBlank(form.getLang())) {
                this.lang = form.getLang();
            }

            return this;
        }

        public Builder withChannelId(String channelId) {
            this.channelId = channelId;
            return this;
        }

        public Builder withQuery(String query) {
            this.query = query;
            return this;
        }

        public Builder withPublishedBefore(String publishedBefore) {
            this.publishedBefore = publishedBefore;
            return this;
        }

        public Builder withPublishedAfter(String publishedAfter) {
            this.publishedAfter = publishedAfter;
            return this;
        }

        public Builder withChannelType(String channelType) {
            this.channelType = channelType;
            return this;
        }

        public Builder withEventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder withOrder(String order) {
            this.order = order;
            return this;
        }


        public Builder withLang(String lang) {
            this.lang = lang;
            return this;
        }

        /**
         * Build new instance.
         *
         * @return
         * @throws IOException
         */
        public ClientForSearchList build() throws IOException {
            final YouTube.Search.List request = ytBuilder.build().search().list(SEARCH_LIST_PARTS);

            request.setType(types);
            request.setMaxResults(maxResults);

            // concrete values
            if (isNotBlank(channelId)) {
                request.setChannelId(channelId);
            }
            // if (isNotBlank(location)) {
            //     request.setLocation(location);
            // }
            // if (isNotBlank(locationRadius)) {
            //     request.setLocationRadius(locationRadius);
            // }
            if (isNotBlank(query)) {
                request.setQ(query);
            }

            // temporaral
            if (isNotBlank(publishedBefore)) {
                request.setPublishedBefore(publishedBefore);
            }
            if (isNotBlank(publishedAfter)) {
                request.setPublishedAfter(publishedAfter);
            }

            // enums
            if (isNotBlank(channelType)) {
                request.setChannelType(channelType);
            }
            if (isNotBlank(eventType)) {
                request.setEventType(eventType);
            }
            if (isNotBlank(order)) {
                request.setOrder(order);
            }
            if (isNotBlank(lang)) {
                request.setRelevanceLanguage(lang);
            }

            // .setRegionCode()
            // .setSafeSearch()
            // .setTopicId();

            // if (instanceof VideoSearchForm) {
            //   .setVideoCaption();
            //   .setVideoCategoryId()
            //   .setVideoDefinition()
            //   .setVideoDimension()
            //   .setVideoDuration()
            //   .setVideoEmbeddable()
            //   .setVideoLicense()
            //   .setVideoSyndicated()
            //   .setVideoType()
            // }

            return new ClientForSearchList(request);
        }
    }
}
