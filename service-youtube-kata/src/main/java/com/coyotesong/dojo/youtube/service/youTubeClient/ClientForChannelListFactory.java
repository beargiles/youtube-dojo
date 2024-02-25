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

package com.coyotesong.dojo.youtube.service.youTubeClient;

import com.coyotesong.dojo.youtube.model.Channel;
import com.coyotesong.dojo.youtube.model.WikipediaTopic;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import org.jetbrains.annotations.NotNull;
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

    public ClientForChannelListFactory(@NotNull YouTube.Builder ytBuilder) {
        this.ytBuilder = ytBuilder;
    }

    @NotNull
    public Builder newBuilder() {
        return new Builder(ytBuilder);
    }

    public static class Builder {
        private static final List<String> CHANNEL_PARTS = Arrays.asList(
                Constants.Part.CONTENT_DETAILS.toString(),
                Constants.Part.CONTENT_OWNER_DETAILS.toString(),
                Constants.Part.ID.toString(),
                Constants.Part.SNIPPET.toString(),
                Constants.Part.TOPIC_DETAILS.toString());

        private final YouTube.Builder ytBuilder;

        private List<String> ids = Collections.emptyList();
        private String forHandle;
        private String forUsername;
        private String hl;
        private String quotaUser;

        private Builder(@NotNull YouTube.Builder ytBuilder) {
            this.ytBuilder = ytBuilder;
        }

        public Builder withId(@NotNull String id) {
            this.ids = Collections.singletonList(id);
            return this;
        }

        public Builder withIds(@NotNull List<String> ids) {
            this.ids = ids;
            return this;
        }

        public Builder withForHandle(@NotNull String forHandle) {
            this.forHandle = forHandle;
            return this;
        }

        public Builder withForUsername(@NotNull String forUsername) {
            this.forUsername = forUsername;
            return this;
        }

        public Builder withHl(@NotNull String hl) {
            this.hl = hl;
            return this;
        }

        public Builder withQuotaUser(@NotNull String quotaUser) {
            this.quotaUser = quotaUser;
            return this;
        }

        /**
         * Build new instance.
         *
         * @return
         * @throws IOException
         */
        @NotNull
        public YouTubeClient.ListChannels build() throws IOException {
            final YouTube.Channels.List request = ytBuilder.build().channels().list(CHANNEL_PARTS);

            boolean valid = false;
            if (!ids.isEmpty()) {
                request.setId(ids);
                valid = true;
            }
            if (isNotBlank(forHandle)) {
                request.setForUsername(forHandle);
                valid = true;
            } else if (isNotBlank(forUsername)) {
                request.setForUsername(forUsername);
                valid = true;
            }

            if (!valid) {
                throw new IllegalStateException("'id', 'handle', or 'username' must be specified");
            }

            if (isNotBlank(hl)) {
                request.setHl(hl);
            }

            if (isNotBlank(quotaUser)) {
                request.setQuotaUser(quotaUser);
            }

            final YouTubeClientState<Channel, YouTube.Channels.List, ChannelListResponse, com.google.api.services.youtube.model.Channel> state =
                    new YouTubeClientState<>(request, ClientForChannelListFactory::convert);
            return new YouTubeClient.ListChannels(state);
        }
    }

    /**
     * Convert YouTube API object to ours.
     *
     * @param channel
     * @return
     */
    @NotNull
    public static Channel convert(@NotNull com.google.api.services.youtube.model.Channel channel) {
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
