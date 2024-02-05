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

import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
abstract class AbstractClientState<R, S extends GenericJson, T extends GenericJson> implements ClientState<R, S, T> {
    private String etag;
    private String eventId;
    private String nextPageToken;
    private PageInfo pageInfo;
    private String visitorId;
    private List<R> items = Collections.emptyList();
    private int start;
    private int count;
    private boolean finished;
    private boolean failed;

    public String getEtag() {
        return etag;
    }

    protected void setEtag(String etag) {
        this.etag = etag;
    }

    public String getEventId() {
        return eventId;
    }

    protected void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    protected void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    public List<R> getItems() {
        return items;
    }

    protected void setItems(List<R> items) {
        this.items = items;
    }

    public int getStart() {
        return start;
    }

    protected void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    protected void setCount(int count) {
        this.count = count;
    }

    public boolean hasNext() {
        return !failed && !finished;
    }

    public List<R> next() {
        return items;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }
}
