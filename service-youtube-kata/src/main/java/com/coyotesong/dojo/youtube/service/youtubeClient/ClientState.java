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

package com.coyotesong.tabs.service.service.youtubeClient;

import com.google.api.client.json.GenericJson;
import com.google.api.services.youtube.model.PageInfo;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public interface ClientState<R, S extends GenericJson, T extends GenericJson> extends Iterator<List<R>> {
    boolean hasNext();

    List<R> next();

    String getEtag();
    String getEventId();
    String getNextPageToken();
    PageInfo getPageInfo();
    String getVisitorId();

    // normal exit
    boolean isFinished();
    void setFinished(boolean finished);

    // abnormal exit (often GoogleJsonResponseException, code 403, errors[0].reason 'quotaExceeded'
    boolean isFailed();
    void setFailed(boolean failed);

    int getStart();
    int getCount();

    void update() throws IOException;
}
