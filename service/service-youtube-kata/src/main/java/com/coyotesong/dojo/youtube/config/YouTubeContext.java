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

import com.coyotesong.dojo.youtube.repository.YouTubeApiCacheRepository;
import com.coyotesong.dojo.youtube.repository.jooq.YouTubeApiCacheRepositoryJooq;
import com.coyotesong.dojo.youtube.service.YouTubeApiCacheService;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.*;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Prepare YouTube client builder here - this lets the katas focus
 * on using the client library instead of handling the authentication.
 * <p>
 *     See https://googleapis.github.io/google-http-java-client/unit-testing.html
 * </p>
 */
@Configuration
@PropertySource("file:///${user.home}/.config/youtube.properties")
@ConfigurationPropertiesScan
@ComponentScan({
        "com.coyotesong.dojo.youtube.config", // for jooq config
        "com.coyotesong.dojo.youtube.repository",
        "com.coyotesong.dojo.youtube.security",
        "com.coyotesong.dojo.youtube.service"
})
@Profile({"test"})
public class YouTubeContext {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(YouTubeContext.class);

    @Value("${youtube.api.key}")
    private String key;

    @Value("${youtube.api.applicationName}")
    private String applicationName;

    @Value("${youtube.api.testChannelId}")
    private String testChannelId;

    /**
     * Create YouTube client API builder
     *
     * @return YouTube builder
     * @throws IOException              may be thrown by newTrustedTransport()
     * @throws GeneralSecurityException may be thrown by newTrustedTransport()
     */
    @Bean
    public YouTube.Builder builder() throws IOException, GeneralSecurityException {

        // final MtlsProvider mtlsProvider = null;
        // final HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport(mtlsProvider);

        // INITIALIZER = new YouTubeRequestInitializer(key, USER_IP);
        final GoogleClientRequestInitializer initializer = new YouTubeRequestInitializer(key);

        final JsonFactory jsonFactory = new GsonFactory();
        final HttpRequestInitializer httpRequestInitializer = null;

        // this method - alone - may trigger IOException or GeneralSecurityException
        final HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        // final HttpTransport transport = new CapturingHttpTransport();

        final YouTube.Builder builder = new YouTube.Builder(transport, jsonFactory, httpRequestInitializer);
        builder.setGoogleClientRequestInitializer(initializer);
        builder.setApplicationName(applicationName);

        // builder.setRootUrl()
        // builder.setServicePath()

        return builder;
    }

    // @Bean
    // @Autowired
    // public YouTubeApiCacheRepository youTubeApiCacheRepository(org.jooq.Configuration configuration) {
    //     return new YouTubeApiCacheRepositoryJooq(configuration);
    // }
}
