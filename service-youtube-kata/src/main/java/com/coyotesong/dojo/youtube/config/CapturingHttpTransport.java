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
import com.google.api.client.http.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Mock HttpTransport
 *
 * This code can be used as the starting point for a testing resource that captures and
 * replays messages exchanges with a live server. This can be used to eliminate the need
 * to burn our YouTube dev quota during routine builds.
 *
 * (It could also be used to build a custom TestContainer.)
 *
 * Alternate approach: capture and/or replay exchanges using proxy server.
 *
 * See [HTTP Unit Testing](https://googleapis.github.io/google-http-java-client/unit-testing.html)
 */
public class CaptureHttpTransport extends HttpTransport {
    private final HttpTransport parent;
    private final HttpRequestFactory requestFactory;

    public CaptureHttpTransport() throws IOException, GeneralSecurityException {
        this.parent = GoogleNetHttpTransport.newTrustedTransport();
        this.requestFactory = parent.createRequestFactory();
    }

    @Override
    public boolean supportsMethod(String method) throws IOException {
        return parent.supportsMethod(method);
    }

    @Override
    public boolean isMtls() {
        return parent.isMtls();
    }

    @Override
    public void shutdown() throws IOException {
        parent.shutdown();
    }

    @Override
    public boolean isShutdown() {
        return parent.isShutdown();
    }

    public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
        final GenericUrl genericUrl = new GenericUrl(url);
        final HttpContent httpContent = null;
        switch (method.toUpperCase()) {
            case "DELETE":
                return new CaptureLowLevelHttpRequest(requestFactory.buildDeleteRequest(genericUrl));

            case "GET":
                return new CaptureLowLevelHttpRequest(requestFactory.buildGetRequest(genericUrl));

            case "HEAD":
                return new CaptureLowLevelHttpRequest(requestFactory.buildHeadRequest(genericUrl));

            case "PATCH":
                // return new CaptureLowLevelHttpRequest(requestFactory.buildPatchRequest(genericUrl, httpContent));

            case "POST":
                // return new CaptureLowLevelHttpRequest(requestFactory.buildPostRequest(genericUrl, httpContent));

            case "PUT":
                // return new CaptureLowLevelHttpRequest(requestFactory.buildPutRequest(genericUrl, httpContent));
        }

        throw new RuntimeException("unimplemented");
    }

    static class CaptureLowLevelHttpRequest extends LowLevelHttpRequest {
        final HttpRequest request;

        public CaptureLowLevelHttpRequest(HttpRequest request) {
            this.request = request;
        }

        @Override
        public void addHeader(String name, String value) throws IOException {
            request.getHeaders().put(name, value);
        }

        @Override
        public LowLevelHttpResponse execute() throws IOException {
            return new CapturingLowLevelHttpResponse(request.execute());
        }
    }
}
