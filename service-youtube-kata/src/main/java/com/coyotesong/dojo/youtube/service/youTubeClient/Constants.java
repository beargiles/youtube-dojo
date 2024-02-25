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

public interface Constants {

    /**
     * Valid request parts
     * <p>
     * (Mostly to avoid typos, for now...)
     * </p>
     */
    enum Part {
        CONTENT_DETAILS("contentDetails"),
        CONTENT_OWNER_DETAILS("contentOwnerDetails"),
        ID("id"),
        LOCALIZATION("localization"),
        PLAYER("player"),
        SNIPPET("snippet"),
        STATUS("status"),
        TARGETING("targeting"),
        TOPIC_DETAILS("topicDetails");

        private final String ytValue;

        Part(String ytValue) {
            this.ytValue = ytValue;
        }

        public String toString() {
            return ytValue;
        }
    }
}
