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

import com.coyotesong.dojo.youtube.model.Channel;
import com.coyotesong.dojo.youtube.model.WikipediaTopic;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Channel list client factory
 */
@Component
@SuppressWarnings("unused")
public class ClientForChannelListFactory {
    private final YouTube.Builder ytBuilder;

    public ClientForChannelListFactory(YouTube.Builder ytBuilder) {
        this.ytBuilder = ytBuilder;
    }

    public Builder newBuilder() {
        return new Builder(ytBuilder);
    }

    public static class Builder {
        private static final List<String> CHANNEL_PARTS = Arrays.asList("contentDetails", "contentOwnerDetails", "id", "snippet", "topicDetails");

        private final YouTube.Builder ytBuilder;

        private List<String> ids = Collections.emptyList();
        private String forHandle;
        private String forUsername;
        private String hl;
        private String quotaUser;

        private Builder(YouTube.Builder ytBuilder) {
            this.ytBuilder = ytBuilder;
        }

        public Builder withId(String id) {
            this.ids = Collections.singletonList(id);
            return this;
        }

        public Builder withIds(List<String> ids) {
            this.ids = ids;
            return this;
        }

        public Builder withForHandle(String forHandle) {
            this.forHandle = forHandle;
            return this;
        }

        public Builder withForUsername(String forUsername) {
            this.forUsername = forUsername;
            return this;
        }

        public Builder withHl(String hl) {
            this.hl = hl;
            return this;
        }

        public Builder withQuotaUser(String quotaUser) {
            this.quotaUser = quotaUser;
            return this;
        }

        /**
         * Build new instance.
         *
         * @return
         * @throws IOException
         */
        public ClientForChannelList build() throws IOException {
            final YouTube.Channels.List request = ytBuilder.build().channels().list(CHANNEL_PARTS);

            if (!ids.isEmpty()) {
                request.setId(ids);
            } else if (isNotBlank(forHandle)) {
                request.setForUsername(forHandle);
            } else if (isNotBlank(forUsername)) {
                request.setForUsername(forUsername);
            } else {
                throw new IllegalStateException("'id', 'handle', or 'username' must be specified");
            }

            if (isNotBlank(hl)) {
                request.setHl(hl);
            }

            if (isNotBlank(quotaUser)) {
                request.setQuotaUser(quotaUser);
            }

            return new ClientForChannelList(request);
        }
    }

    public static class ClientForChannelList extends ClientForYouTubeRequest<Channel, ChannelListResponse, com.google.api.services.youtube.model.Channel> {
        ClientForChannelList(YouTube.Channels.List request) {
            super(new ChannelState(request));
        }
    }

    public static class ChannelState extends AbstractClientState<Channel, ChannelListResponse, com.google.api.services.youtube.model.Channel> {
        private static final Logger LOG = LoggerFactory.getLogger(ChannelState.class);
        private final YouTube.Channels.List request;

        public ChannelState(YouTube.Channels.List request) {
            this.request = request;
        }

        /**
         * Perform YouTube API call.
         */
        public void update() throws IOException {
            request.setMaxResults(50L);
            request.setPageToken(getNextPageToken());
            final ChannelListResponse response = request.execute();

            setEtag(response.getEtag());
            setEventId(response.getEventId());
            setPageInfo(response.getPageInfo());
            setVisitorId(response.getVisitorId());

            // 'setNextPageToken()' is handled here in order to ensure that it's a null value
            // if there's an empty response.
            if (response.isEmpty() || (response.getItems() == null) || response.getItems().isEmpty()) {
                setItems(Collections.emptyList());
                setNextPageToken(null);
            } else {
                setItems(response.getItems().stream().map(this::convert).toList());
                setNextPageToken(response.getNextPageToken());
            }
        }

        /**
         * Convert YouTube API object to ours.
         *
         * @param channel
         * @return
         */
        public Channel convert(com.google.api.services.youtube.model.Channel channel) {
            final Channel value = new Channel();
            value.setId(channel.getId());
            value.setEtag(channel.getEtag());

            if (channel.getContentOwnerDetails() != null) {
                value.setContentOwner(channel.getContentOwnerDetails().getContentOwner());
                // skip 'timeLinked'
            }

            // skip localizations
            if (channel.getSnippet() != null) {
                final ChannelSnippet snippet = channel.getSnippet();
                value.setCountry(snippet.getCountry());
                value.setCustomUrl(snippet.getCustomUrl());
                value.setLang(snippet.getDefaultLanguage());
                value.setDescription(snippet.getDescription());
                if (snippet.getPublishedAt() != null) {
                    value.setPublishedAt(Instant.ofEpochMilli(snippet.getPublishedAt().getValue()));
                }
                value.setTitle(snippet.getTitle());
                final ThumbnailDetails td = snippet.getThumbnails();
                if ((td != null) && !td.isEmpty() && !td.getDefault().isEmpty()) {
                    value.setTnUrl(td.getDefault().getUrl());
                }
            }

            if (channel.getContentDetails() != null) {
                final ChannelContentDetails details = channel.getContentDetails();
                value.setUploads(details.getRelatedPlaylists().getUploads());
                // value.set(favorites = details.getRelatedPlaylists().getFavorites();
                // value.set(likes = details.getRelatedPlaylists().getLikes();
                // value.set(watchHistory = details.getRelatedPlaylists().getWatchHistory();
                // value.set(watchLater = details.getRelatedPlaylists().getWatchLater();
            }

            if (channel.getTopicDetails() != null) {
                ChannelTopicDetails details = channel.getTopicDetails();
                if (details.getTopicCategories() != null) {
                    for (String category : details.getTopicCategories()) {
                        value.getTopicCategories().add(new WikipediaTopic(category));
                    }
                }
                if (details.getTopicIds() != null) {
                    value.getTopicIds().addAll(details.getTopicIds());
                }
            }

            value.setLastChecked(Instant.now().truncatedTo(ChronoUnit.SECONDS));

            // statistics
            //    comments, hidden subscriber count, video count, view count
            // status
            //    isLinked, longUploadStatus, madeForKids, privacyStatus, selfDeclaredMadeForKids

            return value;
        }
    }
}
