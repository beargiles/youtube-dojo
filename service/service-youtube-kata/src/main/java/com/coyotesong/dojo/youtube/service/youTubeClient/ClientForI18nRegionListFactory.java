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

import com.coyotesong.dojo.youtube.service.YouTubeApiCacheService;
import com.coyotesong.dojo.youtube.model.I18nRegion;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.I18nRegionListResponse;
import com.google.api.services.youtube.model.I18nRegionSnippet;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * I18nRegion list client factory
 */
@Component
@SuppressWarnings("unused")
public class ClientForI18nRegionListFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ClientForI18nRegionListFactory.class);

    private final YouTube.Builder ytBuilder;
    private final YouTubeApiCacheService cache;

    public ClientForI18nRegionListFactory(@NotNull YouTube.Builder ytBuilder, @NotNull YouTubeApiCacheService cache) {
        this.ytBuilder = ytBuilder;
        this.cache = cache;
    }

    @NotNull
    public Builder newBuilder() {
        return new Builder(ytBuilder, cache);
    }

    public static class Builder {
        private static final List<String> I18N_LANGUAGE_PARTS = Collections.singletonList(Constants.Part.SNIPPET.toString());

        private final YouTube.Builder ytBuilder;
        private final YouTubeApiCacheService cache;

        private String hl;
        private String quotaUser;

        private Builder(@NotNull YouTube.Builder ytBuilder, @NotNull YouTubeApiCacheService cache) {
            this.ytBuilder = ytBuilder;
            this.cache = cache;
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
        public YouTubeClient.ListI18nRegions build() throws IOException {
            final YouTube.I18nRegions.List request = ytBuilder.build().i18nRegions().list(I18N_LANGUAGE_PARTS);

            if (isBlank(hl)) {
                throw new IllegalStateException("'hl' must be specified");
            }

            request.setHl(hl);

            if (isNotBlank(quotaUser)) {
                request.setQuotaUser(quotaUser);
            }

            final YouTubeClientState<I18nRegion, YouTube.I18nRegions.List, I18nRegionListResponse, com.google.api.services.youtube.model.I18nRegion> state =
                    new YouTubeClientState<>(request, ClientForI18nRegionListFactory::convert, cache);

            return new YouTubeClient.ListI18nRegions(state);
        }
    }

    /**
     * Convert YouTube API object to ours.
     *
     * @param language
     * @return
     */
    @NotNull
    public static I18nRegion convert(@NotNull com.google.api.services.youtube.model.I18nRegion language) {
        final I18nRegion value = new I18nRegion();

        if (LOG.isTraceEnabled()) {
            try {
                LOG.trace(language.toPrettyString());
            } catch (IOException e) {
            }
        }

        value.setCode(language.getId());

        if (language.getSnippet() != null) {
            final I18nRegionSnippet snippet = language.getSnippet();
            value.setGl(snippet.getGl());
            value.setName(snippet.getName());
        }

        // value.setEtag(language.getEtag());
        // value.setLastChecked(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        return value;
    }
}
