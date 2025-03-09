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

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.LowLevelHttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@SuppressWarnings("unchecked")
class CapturingLowLevelHttpResponse extends LowLevelHttpResponse {
    private final HttpRequest request;
    private final HttpResponse response;
    private final Map<String, Object> headers = new LinkedHashMap<>();
    private final List<String> headerNames;

    public CapturingLowLevelHttpResponse(HttpResponse response) {
        this.request = response.getRequest();
        this.response = response;
        this.response.getHeaders().putAll(headers);
        this.headerNames = headers.keySet().stream().toList();
    }

    @Override
    public InputStream getContent() throws IOException {
        return response.getContent();
    }

    @Override
    public String getContentEncoding() throws IOException {
        return response.getContentEncoding();
    }

    @Override
    public long getContentLength() throws IOException {
        return 0;
    }

    @Override
    public String getContentType() throws IOException {
        return response.getContentType();
    }

    @Override
    public String getStatusLine() throws IOException {
        return response.getStatusMessage();
    }

    @Override
    public int getStatusCode() throws IOException {
        return response.getStatusCode();
    }

    @Override
    public String getReasonPhrase() throws IOException {
        return "";
    }

    @Override
    public int getHeaderCount() throws IOException {
        return headers.size();
    }

    @Override
    public String getHeaderName(int i) throws IOException {
        return headerNames.get(i);
    }

    @Override
    public String getHeaderValue(int i) throws IOException {
        final Object obj = headers.get(headerNames.get(i));
        if (obj == null) {
            return null;
        } else if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof String[]) {
            return Arrays.toString((String[]) obj);
        } else {
            return Objects.toString(obj);
        }
    }
}
