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

package com.coyotesong.tabs.service.service.youtubeClient;

import com.google.api.services.youtube.YouTube;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Video list client factory
 */
@Component
@SuppressWarnings("unused")
public class ClientForVideoListFactory {
    private final YouTube.Builder ytBuilder;

    public ClientForVideoListFactory(YouTube.Builder ytBuilder) {
        this.ytBuilder = ytBuilder;
    }

    public Builder newBuilder() {
        return new Builder(ytBuilder);
    }

    public static class Builder {
        private static final List<String> VIDEO_PARTS = Arrays.asList("contentDetails", "contentOwnerDetails", "id", "snippet", "topicDetails");

        private final YouTube.Builder ytBuilder;

        private List<String> ids = Collections.emptyList();
        private String forUsername;
        private String hl;

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

        public Builder withForUsername(String forUsername) {
            this.forUsername = forUsername;
            return this;
        }

        public Builder withHl(String hl) {
            this.hl = hl;
            return this;
        }

        /**
         * Build new instance.
         *
         * @return
         * @throws IOException
         */
        public ClientForVideoList build() throws IOException {
            final YouTube.Videos.List request = ytBuilder.build().videos().list(VIDEO_PARTS);

            if (!ids.isEmpty()) {
                request.setId(ids);
            } else {
                throw new IllegalStateException("either 'id' or 'username' must be specified");
            }

            if (isNotBlank(hl)) {
                request.setHl(hl);
            }

            return new ClientForVideoList(request);
        }
    }
}
