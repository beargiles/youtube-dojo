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
import com.google.api.services.youtube.YouTubeRequest;

import java.io.IOException;

/**
 * Persistence mechanism for YouTube API requests.
 *
 * - If the request has been seen before then the persistence mechanism should replay
 *   it.
 *
 * - If the request has not been seen before then the persistence mechanism should
 *   capture the request and responses for future reuse.
 */
public interface YouTubeRequestPersistenceService<C extends YouTubeRequest<S>, S extends GenericJson, T extends GenericJson> extends AutoCloseable {

    void setRequest(C request, Class<S> sClass);

    S update(String nextPageToken, S response) throws IOException;

    void close() throws IOException;
}
