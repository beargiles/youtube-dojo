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

import com.coyotesong.dojo.youtube.model.I18nLanguage;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.I18nLanguageListResponse;
import com.google.api.services.youtube.model.I18nLanguageSnippet;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * I18nLanguage list client factory
 */
@Component
@SuppressWarnings("unused")
public class ClientForI18nLanguageListFactory {
    private final YouTube.Builder ytBuilder;

    public ClientForI18nLanguageListFactory(@NotNull YouTube.Builder ytBuilder) {
        this.ytBuilder = ytBuilder;
    }

    @NotNull
    public Builder newBuilder() {
        return new Builder(ytBuilder);
    }

    public static class Builder {
        private static final List<String> I18N_LANGUAGE_PARTS = Arrays.asList(Constants.Part.SNIPPET.toString());

        private final YouTube.Builder ytBuilder;

        private String hl;
        private String quotaUser;

        private Builder(@NotNull YouTube.Builder ytBuilder) {
            this.ytBuilder = ytBuilder;
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
        public YouTubeClient.ListI18nLanguages build() throws IOException {
            final YouTube.I18nLanguages.List request = ytBuilder.build().i18nLanguages().list(I18N_LANGUAGE_PARTS);

            if (isBlank(hl)) {
                throw new IllegalStateException("'hl' must be specified");
            }

            request.setHl(hl);

            if (isNotBlank(quotaUser)) {
                request.setQuotaUser(quotaUser);
            }

            final YouTubeClientState<I18nLanguage, YouTube.I18nLanguages.List, I18nLanguageListResponse, com.google.api.services.youtube.model.I18nLanguage> state =
                    new YouTubeClientState<>(request, ClientForI18nLanguageListFactory::convert);

            return new YouTubeClient.ListI18nLanguages(state);
        }
    }

    /**
     * Convert YouTube API object to ours.
     *
     * @param language
     * @return
     */
    @NotNull
    public static I18nLanguage convert(@NotNull com.google.api.services.youtube.model.I18nLanguage language) {
        final I18nLanguage value = new I18nLanguage();

        value.setId(language.getId());
        value.setEtag(language.getEtag());

        if (language.getSnippet() != null) {
            final I18nLanguageSnippet snippet = language.getSnippet();
            value.setHl(snippet.getHl());
            value.setName(snippet.getName());
        }

        // value.setLastChecked(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        return value;
    }
}
