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

package com.coyotesong.dojo.youtube.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.util.Properties;

/**
 * Invalid YouTube credentials
 */
@Configuration
@Profile("BadYouTubeAccount")
public class BadYouTubeProperties {

    @Bean
    public Properties youtubeProperties() throws IOException {
        // could also check environment variables, etc.
        final Properties properties = new Properties();
        properties.put("youtube.api.key", TestConstants.BAD_API_KEY);
        properties.put("youtube.api.name", TestConstants.BAD_APPLICATION_NAME);
        return properties;
    }
}
