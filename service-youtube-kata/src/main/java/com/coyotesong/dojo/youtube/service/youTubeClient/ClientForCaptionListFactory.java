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

import com.coyotesong.dojo.youtube.model.Caption;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CaptionListResponse;
import com.google.api.services.youtube.model.CaptionSnippet;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Caption list client factory
 */
@Component
@SuppressWarnings("unused")
public class ClientForCaptionListFactory {
    private final YouTube.Builder ytBuilder;

    public ClientForCaptionListFactory(@NotNull YouTube.Builder ytBuilder) {
        this.ytBuilder = ytBuilder;
    }

    @NotNull
    public Builder newBuilder() {
        return new Builder(ytBuilder);
    }

    public static class Builder {
        private static final List<String> CAPTION_PARTS = Arrays.asList(
                Constants.Part.ID.toString(),
                Constants.Part.SNIPPET.toString());

        private final YouTube.Builder ytBuilder;

        private String videoId;
        private String quotaUser;

        private Builder(@NotNull YouTube.Builder ytBuilder) {
            this.ytBuilder = ytBuilder;
        }

        public Builder withVideoId(@NotNull String videoId) {
            this.videoId = videoId;
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
        public YouTubeClient.ListCaptions build() throws IOException {
            if (isBlank(videoId)) {
                throw new IllegalStateException("'videoId' must be specified");
            }

            final YouTube.Captions.List request = ytBuilder.build().captions().list(CAPTION_PARTS, videoId);
            request.setVideoId(videoId);

            // requires oauth: setOnBehalfOf(), setOnBehalfOfContentOwner()

            // setIds() - Stubby or Apiary

            if (isNotBlank(quotaUser)) {
                request.setQuotaUser(quotaUser);
            }

            final YouTubeClientState<Caption, YouTube.Captions.List, CaptionListResponse, com.google.api.services.youtube.model.Caption> state =
                    new YouTubeClientState<>(request, ClientForCaptionListFactory::convert);
            return new YouTubeClient.ListCaptions(state);
        }
    }

    /**
     * Convert YouTube API object to ours.
     *
     * @param caption
     * @return
     */
    @NotNull
    public static Caption convert(@NotNull com.google.api.services.youtube.model.Caption caption) {
        final Caption value = new Caption();
        value.setId(caption.getId());
        value.setEtag(caption.getEtag());

        if (caption.getSnippet() != null) {
            final CaptionSnippet snippet = caption.getSnippet();
            value.setAudioTrackType(snippet.getAudioTrackType());
            value.setFailureReason(snippet.getFailureReason());
            value.setAutoSynced(snippet.getIsAutoSynced());
            value.setCc(snippet.getIsCC());
            value.setDraft(snippet.getIsDraft());
            value.setEasyReader(snippet.getIsEasyReader());
            value.setLarge(snippet.getIsLarge());
            value.setLanguage(snippet.getLanguage());
            if (snippet.getLastUpdated() != null) {
                value.setLastUpdated(Instant.ofEpochMilli(snippet.getLastUpdated().getValue()));
            }
            value.setName(snippet.getName());
            value.setStatus(snippet.getStatus());
            value.setTrackKind(snippet.getTrackKind());
            value.setVideoId(snippet.getVideoId());
        }

        value.setLastChecked(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        return value;
    }
}
