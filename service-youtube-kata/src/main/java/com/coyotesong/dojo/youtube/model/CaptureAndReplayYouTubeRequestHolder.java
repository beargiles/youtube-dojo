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

import com.coyotesong.dojo.youtube.service.YouTubeRequestHolder;
import com.coyotesong.dojo.youtube.service.YouTubeRequestPersistenceService;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.youtube.YouTubeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Capture and replay implementation of YouTubeRequestHolder
 *
 * Capture-and-replay mechanism for YouTube API Requests. The persistence mechanism
 * should determine if this request has been seen before.
 *
 * - If the request has been seen before then the persistence mechanism should replay
 *   it.
 *
 * - If the request has not been seen before then the persistence mechanism should
 *   capture the request and responses for future reuse.
 *
 * This class should be unaware whether this is a live or captured exchange.
 *
 * @param <C> YouTube API request
 * @param <S> contents of response
 * @param <T> contents of request
 */
@SuppressWarnings("unused")
public class CaptureAndReplayYouTubeRequestHolder<C extends YouTubeRequest<S>, S extends GenericJson, T extends GenericJson> implements YouTubeRequestHolder<C, S, T> {
    private static final Logger LOG = LoggerFactory.getLogger(CaptureAndReplayYouTubeRequestHolder.class);

    private final YouTubeRequestPersistenceService<C, S, T> youTubeRequestPersistenceService;
    private final C request;
    private JsonFactory jsonFactory;

    public CaptureAndReplayYouTubeRequestHolder(C request, Class<S> sClass) {
        this.request = request;
        if (!"List".equals(request.getClass().getSimpleName())) {
            LOG.info("Only 'List' is supported");
            this.youTubeRequestPersistenceService = null;
        } else {
            this.youTubeRequestPersistenceService = new YouTubeRequestPersistenceServiceZipFile();
            this.youTubeRequestPersistenceService.setRequest(request, sClass);
        }
    }

    public CaptureAndReplayYouTubeRequestHolder(C request, Class<S> sClass, JsonFactory jsonFactory) {
        this.request = request;
        this.jsonFactory = jsonFactory;
        if (!"List".equals(request.getClass().getSimpleName())) {
            LOG.info("Only 'List' is supported");
            this.youTubeRequestPersistenceService = null;
        } else {
            this.youTubeRequestPersistenceService = new YouTubeRequestPersistenceServiceZipFile();
            this.youTubeRequestPersistenceService.setRequest(request, sClass);
        }
    }

    /**
     * Perform request
     *
     * @return
     * @throws IOException
     */
    @Override
    public S execute(String nextPageToken) throws IOException {
        final S response = request.execute();
        if (response.getFactory() == null) {
            response.setFactory(jsonFactory);
        }
        if (youTubeRequestPersistenceService != null) {
            return youTubeRequestPersistenceService.update(nextPageToken, response);
        } else {
            return response;
        }
    }

    public void close() throws IOException {
        if (youTubeRequestPersistenceService != null) {
            youTubeRequestPersistenceService.close();
        }
    }
}
