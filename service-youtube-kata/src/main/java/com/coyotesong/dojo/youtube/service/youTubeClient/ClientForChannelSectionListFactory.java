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

import com.coyotesong.dojo.youtube.model.ChannelSection;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelSectionContentDetails;
import com.google.api.services.youtube.model.ChannelSectionListResponse;
import com.google.api.services.youtube.model.ChannelSectionSnippet;
import com.google.api.services.youtube.model.ChannelSectionTargeting;
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
 * ChannelSection list client factory
 */
@Component
@SuppressWarnings("unused")
public class ClientForChannelSectionListFactory {
    private final YouTube.Builder ytBuilder;

    public ClientForChannelSectionListFactory(@NotNull YouTube.Builder ytBuilder) {
        this.ytBuilder = ytBuilder;
    }

    @NotNull
    public Builder newBuilder() {
        return new Builder(ytBuilder);
    }

    public static class Builder {
        private static final List<String> CHANNEL_SECTION_PARTS = Arrays.asList(
                Constants.Part.CONTENT_DETAILS.toString(),
                Constants.Part.ID.toString(),
                Constants.Part.SNIPPET.toString());
        // TODO - TARGETING, LOCALALIZTION

        private final YouTube.Builder ytBuilder;

        private String channelId;
        private List<String> ids = Collections.emptyList();
        private String hl;
        private String quotaUser;

        private Builder(@NotNull YouTube.Builder ytBuilder) {
            this.ytBuilder = ytBuilder;
        }

        public Builder withChannelId(@NotNull String channelId) {
            this.channelId = channelId;
            return this;
        }

        public Builder withId(@NotNull String id) {
            this.ids = Collections.singletonList(id);
            return this;
        }

        public Builder withIds(@NotNull List<String> ids) {
            this.ids = ids;
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
        public YouTubeClient.ListChannelSections build() throws IOException {
            final YouTube.ChannelSections.List request = ytBuilder.build().channelSections().list(CHANNEL_SECTION_PARTS);

            // requires authenticated user: request.setMine(true)
            // reserved for YouTube content partners: request.setOnBehalfOfContentOwner(String);

            boolean valid = false;
            if (isNotBlank(channelId)) {
                request.setChannelId(channelId);
                valid = true;
            }
            if (!ids.isEmpty()) {
                request.setId(ids);
                valid = true;
            }

            if (!valid) {
                throw new IllegalStateException("'channelId' or 'id' must be specified");
            }

            if (isNotBlank(hl)) {
                request.setHl(hl);
            }

            if (isNotBlank(quotaUser)) {
                request.setQuotaUser(quotaUser);
            }

            final YouTubeClientState<ChannelSection, YouTube.ChannelSections.List, ChannelSectionListResponse, com.google.api.services.youtube.model.ChannelSection> state =
                    new YouTubeClientState<>(request, ClientForChannelSectionListFactory::convert);
            return new YouTubeClient.ListChannelSections(state);
        }
    }

    /**
     * Convert YouTube API object to ours.
     *
     * @param channelSection
     * @return
     */
    @NotNull
    public static ChannelSection convert(@NotNull com.google.api.services.youtube.model.ChannelSection channelSection) {
        final ChannelSection value = new ChannelSection();
        value.setId(channelSection.getId());
        value.setEtag(channelSection.getEtag());

        final ChannelSectionContentDetails details = channelSection.getContentDetails();
        if (details != null) {
            final List<String> channelIds = details.getChannels();
            if (channelIds != null) {
                value.setChannelIds(channelIds);
            }
            final List<String> playlistIds = details.getPlaylists();
            if (playlistIds != null) {
                value.setPlaylistIds(playlistIds);
            }
        }

        final ChannelSectionSnippet snippet = channelSection.getSnippet();
        if (snippet != null) {
            value.setChannelId(snippet.getChannelId());
            value.setLang(snippet.getDefaultLanguage());
            value.setPosition(snippet.getPosition());
            value.setStyle(snippet.getStyle());
            value.setTitle(snippet.getTitle());
            value.setType(snippet.getType());
            // localizations
        }

        final ChannelSectionTargeting targeting = channelSection.getTargeting();
        if (targeting != null) {
            @SuppressWarnings("unused") final List<String> countries = targeting.getCountries();
            @SuppressWarnings("unused") final List<String> languages = targeting.getLanguages();
            @SuppressWarnings("unused") final List<String> regions = targeting.getRegions();
        }

        value.setLastChecked(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        return value;
    }
}
