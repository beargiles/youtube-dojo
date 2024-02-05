/*
 * Copyright (c) 2023 Bear Giles <bgiles@coyotesong.com>.
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

import com.coyotesong.tabs.model.Channel;
import org.springframework.stereotype.Service;

/**
 * Implementation of Sanitizer
 */
@Service
public class LogSanitizerImpl implements LogSanitizer {
    /**
     * Sanitize arbitrary string
     *
     * @param s arbitrary string
     * @return sanitized string
     */
    @Override
    public String forString(String s) {
        return s;
    }

    /**
     * Sanitize 'channel' object
     *
     * @param channel channel object
     * @return sanitized string
     */
    @Override
    public String forChannel(Channel channel) {
        // do nothing special... yet
        return forString(String.valueOf(channel));
    }

    @Override
    public String forUsername(String username) {
        return forString(username);
    }

    @Override
    public String forChannelId(String channelId) {
        return forString(channelId);
    }
}
