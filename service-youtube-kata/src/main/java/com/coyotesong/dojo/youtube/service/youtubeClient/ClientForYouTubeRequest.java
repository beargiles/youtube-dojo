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

package com.coyotesong.dojo.youtube.service.youtubeClient;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.GenericJson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


@SuppressWarnings("unused")
public class ClientForYouTubeRequest<R, S extends GenericJson, T extends GenericJson> implements Iterator<List<R>> {
    private static final Logger LOG = LoggerFactory.getLogger(ClientForYouTubeRequest.class);

    private final ClientState<R, S, T> state;

    public ClientForYouTubeRequest(ClientState<R, S, T> state) {
        this.state = state;
    }

    public String getEtag() {
        return state.getEtag();
    }

    public String getEventId() {
        return state.getEventId();
    }

    public String getVisitorId() {
        return state.getVisitorId();
    }

    public boolean hasNext() {
        return state.hasNext();
    }

    @Override
    public List<R> next() {

        if (!hasNext()) {
            LOG.info("next() called without checking 'hasNext()' first");
            return Collections.emptyList();
        }

        try {
            state.update();
            state.setFinished(StringUtils.isBlank(state.getNextPageToken()));
        } catch (GoogleJsonResponseException e) {
            final GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                if (!error.getErrors().isEmpty() && "youtube.quota".equals(error.getErrors().get(0).getDomain())) {
                    LOG.warn("Quota issue: {}", error.getErrors().get(0).getReason());
                } else {
                    LOG.warn("Authentication issue: {}", error.getMessage());
                }
            } else if (error.getCode() == 404) {
                // this is not consistently seen with unknown channel, playlist, video, etc.
                LOG.info("Not found: {}", error.getMessage());
            } else {
                LOG.warn("GoogleJsonError: {}", error);
            }
            state.setFailed(true);
        } catch (IOException e) {
            LOG.info("{}: {}", e.getClass().getName(), e.getMessage());
            state.setFailed(true);
        }

        return state.next();
    }
}
