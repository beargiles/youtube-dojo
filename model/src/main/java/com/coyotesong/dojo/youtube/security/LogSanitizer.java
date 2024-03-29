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

package com.coyotesong.dojo.youtube.security;

/**
 * Sanitize potentially unsafe information before writing it to the log.
 */
@SuppressWarnings("unused")
public interface LogSanitizer {
    /**
     * Sanitize arbitrary string
     *
     * @param s arbitrary string
     * @return sanitized string
     */
    String forString(String s);

    String forUsername(String username);

    default String forHandle(String handle) {
        return forString(handle);
    }

    default String forChannelId(String channelId) {
        return forString(channelId);
    }

    default String forPlaylistId(String playlistId) {
        return forString(playlistId);
    }

    default String forPlaylistItemId(String playlistItemId) {
        return forString(playlistItemId);
    }

    default String forVideoId(String videoId) {
        return forString(videoId);
    }
}
