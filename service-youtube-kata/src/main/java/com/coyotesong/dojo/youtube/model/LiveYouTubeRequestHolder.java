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

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.youtube.YouTubeRequest;

import java.io.IOException;

/**
 * Default implementation of YouTubeRequestHolder
 *
 * @param <C> YouTube API request
 * @param <S> contents of response
 * @param <T> contents of request
 */
@SuppressWarnings("unused")
public class LiveYouTubeRequestHolder<C extends YouTubeRequest<S>, S extends GenericJson, T extends GenericJson> implements YouTubeRequestHolder<C, S, T> {
    private final C request;
    private JsonFactory jsonFactory;

    public LiveYouTubeRequestHolder(C request) {
        this.request = request;
    }

    public LiveYouTubeRequestHolder(C request, JsonFactory jsonFactory) {
        this(request);
        this.jsonFactory = jsonFactory;
    }

    /**
     * Perform request
     *
     * @return
     * @throws IOException
     */
    @Override
    public S execute(String nextPageToken) throws IOException {
        S response = request.execute();
        if (response.getFactory() == null) {
            response.setFactory(jsonFactory);
        }
        return response;
    }
}
