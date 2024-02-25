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

package com.coyotesong.dojo.youtube.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * Handle YouTube Client API configuration.
 */
@Configuration
public class YouTubeContext {

    /**
     * Create YouTube client API builder
     *
     * @return YouTube builder
     * @throws IOException              may be thrown by newTrustedTransport()
     * @throws GeneralSecurityException may be thrown by newTrustedTransport()
     */
    @Bean
    @Autowired
    public YouTube.Builder builder(@NotNull Properties youtubeProperties) throws IOException, GeneralSecurityException {

        // this method - alone - may trigger IOException or GeneralSecurityException
        final HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();

        final String key = youtubeProperties.getProperty("youtube.api.key");

        // INITIALIZER = new YouTubeRequestInitializer(key, USER_IP);
        final GoogleClientRequestInitializer initializer = new YouTubeRequestInitializer(key);

        final JsonFactory jsonFactory = new GsonFactory();
        final HttpRequestInitializer httpRequestInitializer = null;

        final YouTube.Builder builder = new YouTube.Builder(transport, jsonFactory, httpRequestInitializer);
        builder.setGoogleClientRequestInitializer(initializer);
        builder.setApplicationName(youtubeProperties.getProperty("youtube.api.name"));

        // builder.setRootUrl()
        // builder.setServicePath()

        return builder;
    }
}
