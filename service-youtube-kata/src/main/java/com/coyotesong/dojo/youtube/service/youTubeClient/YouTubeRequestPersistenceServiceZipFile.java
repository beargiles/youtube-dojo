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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpContent;
import com.google.api.client.json.GenericJson;
import com.google.api.services.youtube.YouTubeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.IllegalAccessException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Implementation of PersistenceService
 *
 * @param <C>
 * @param <S>
 * @param <T>
 */
public class PersistenceServiceZipFile<C extends YouTubeRequest<S>, S extends GenericJson, T extends GenericJson> implements PersistenceService<C, S, T> {
    private static final Logger LOG = LoggerFactory.getLogger(PersistenceServiceZipFile.class);

    private static final String TMPDIR = System.getProperty("java.io.tmpdir");
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Map<Class<?>, Method> GET_NEXT_TOKEN_METHODS = new LinkedHashMap<>();

    private final Map<String, Object> requestProps = new LinkedHashMap<>();
    private final Map<String, String> responses = new LinkedHashMap<>();

    // how much of this should be saved?
    private String requestJson;
    private HttpContent httpContent;
    private T jsonContent;
    private String fields;

    private File file;
    private boolean isClosed = false;
    private boolean isReplay = false;

    private Class<S> sClass;
    private Method getNextTokenMethod;

    public PersistenceServiceZipFile() {

    }

    /**
     * Set the initial request properties.
     *
     * @param request
     */
    public void setRequest(C request, Class<S> sClass) {
        this.sClass = sClass;
        this.file = determinePath(request);

        if (GET_NEXT_TOKEN_METHODS.containsKey(sClass)) {
            this.getNextTokenMethod = GET_NEXT_TOKEN_METHODS.get(sClass);
        } else {
            try {
                // this shouldn't be required.
                // FIXME - this needs to handle known exceptions better!
                this.getNextTokenMethod = sClass.getDeclaredMethod("getNextToken");
                GET_NEXT_TOKEN_METHODS.put(sClass, this.getNextTokenMethod);
            } catch (NoSuchMethodException e) {
                // this is unusual but does happen with some of the I18N API calls.
                LOG.info("{} does not have a getNextToken() method", sClass.getName());
            }
        }

        request.forEach((s, p) -> requestProps.put(s, p));

        // remove sensitive content
        requestProps.remove("accessToken");
        requestProps.remove("key");
        requestProps.remove("oauthToken");

        // TODO - remove sensitive content? E.g., 'key', 'accessToken', 'oauthToken'
        this.httpContent = request.getHttpContent();

        // TODO - verify this is a 'T'
        this.jsonContent = (T) request.getJsonContent();

        this.fields = request.getFields();
    }

    String getResponseKey(String token) {
        return isNotBlank(token) ? token : "last";
    }

    public S replay(String responseKey) throws IOException {
        if (!isReplay) {
            LOG.warn("Replay should not have been called!");
            throw new IOException("Replay should not have been called!");
        }

        final String json = responses.get(responseKey);
        if (json == null) {
            return null;
        }

        return MAPPER.readValue(json, sClass);
    }

    /**
     * Update capture with current response
     *
     * @param response
     * @return
     * @throws IOException
     */
    public S update(String nextPageToken, S response) throws IOException {
        final String responseKey = getResponseKey(nextPageToken);
        if (isReplay) {
            return replay(responseKey);
        }

        try {
            responses.put(responseKey, response.toPrettyString());
        } finally {
            if (isBlank(nextPageToken)) {
                close();
            }
        }

        return response;
    }

    /**
     * Persist everything that has been captured.
     *
     * @throws IOException
     */
    public void close() throws IOException {
        if (!isClosed) {
            isClosed = true;
        }

        if (isReplay) {
            return;
        }

        if (file == null) {
            return;
        }

        try (OutputStream os = new FileOutputStream(file);
             ZipOutputStream zos = new ZipOutputStream(os)) {

            for (Map.Entry<String, String> entry : responses.entrySet()) {
                final String s = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(entry.getValue());
                final byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
                final ZipEntry ze = new ZipEntry("content/" + entry.getKey() + ".json");
                ze.setSize(bytes.length);
                zos.putNextEntry(ze);
                zos.write(bytes, 0, bytes.length);
                zos.closeEntry();
            }
        }
    }

    /**
     * Determine the correct path for the zipfile.
     *
     * @param request
     * @return
     */
    final File determinePath(C request) {
        final int offset = "com.google.api.services.youtube.YouTube$".length();
        final String className = request.getClass().getName();
        if (className.length() < offset) {
            return null;
        }

        final String name = className.substring(offset).replace("$", FILE_SEPARATOR).toLowerCase();
        final File dir = new File(TMPDIR + FILE_SEPARATOR + name);
        dir.mkdirs();

        String id = null;
        final Object obj = request.get("id");
        if (obj == null) {
            id = "all";
        } else if (obj instanceof List) {
            @SuppressWarnings("unchecked") final List<Object> list = (List<Object>) obj;
            if (list.size() == 1) {
                id = String.valueOf(list.get(0));
            } else {
                LOG.info("Too many request parameters for proper filename: {}", list.size());
                return null;
            }
        } else {
            LOG.info("Unable to determine proper filename for object: {}", obj.getClass().getName());
            return null;
        }

        return new File(dir, id + ".zip");
    }
}
