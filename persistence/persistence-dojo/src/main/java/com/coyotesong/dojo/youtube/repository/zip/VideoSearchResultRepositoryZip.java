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

package com.coyotesong.dojo.youtube.repository.zip;

import com.coyotesong.dojo.youtube.form.VideoSearchForm;
import com.coyotesong.dojo.youtube.model.SearchResult;
import com.coyotesong.dojo.youtube.repository.VideoSearchResultRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class VideoSearchResultRepositoryZip implements VideoSearchResultRepository {
    private static final Logger LOG = LoggerFactory.getLogger(VideoSearchResultRepositoryZip.class);
    private static final ObjectMapper MAPPER;

    static {
        MAPPER = JsonMapper
                .builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    private final File file ;

    public VideoSearchResultRepositoryZip(File file) {
        this.file = file;
    }

    @Override
    public void delete() {
        // LOCK
        try (OutputStream os = new FileOutputStream(file);
             ZipOutputStream zos = new ZipOutputStream(os)) {
            // do nothing...
            zos.finish();
        } catch (IOException e) {
            // FiXME - define appropriate exception
            throw new RuntimeException(String.format("%s: %s", e.getClass(), e.getMessage()));
        }
        // UNLOCK
    }

    /*
    @Override
    public @NotNull List<SearchResult> findAll() {
        final List<SearchResult> results = new ArrayList<>();

        // LOCK
        try (ZipFile zf = new ZipFile(file)) {
            return zf.stream().map(ze -> readSearchResult(zf, ze)).toList();
        } catch (IOException e) {
            // FiXME - define appropriate exception
            throw new RuntimeException(String.format("%s: %s", e.getClass(), e.getMessage()));
        }
        // UNLOCK
    }

    @Override
    public @Nullable SearchResult findById(String resultId) {
        // LOCK
        try (ZipFile zf = new ZipFile(file)) {
            final ZipEntry ze = zf.getEntry(resultId);
            if (ze != null) {
                try (InputStream is = zf.getInputStream(ze);
                     BufferedInputStream bis = new BufferedInputStream(is)) {
                    return MAPPER.readValue(bis, SearchResult.class);
                }
            }
        } catch (IOException e) {
            // FiXME - define appropriate exception
            throw new RuntimeException(String.format("%s: %s", e.getClass(), e.getMessage()));
        }
        // UNLOCK

        return null;
    }
     */

    public void insert(SearchResult result) {
    }

    /**
     * Save result details
     *
     * @param results
     */
    @Override
    public void insert(Collection<SearchResult> results) {
        // LOCK
        try (ZipFile zf = new ZipFile(file)) {
            for (SearchResult result : results) {
                ZipEntry ze = zf.getEntry(result.getVideoId());
                if (ze != null) {
                    LOG.warn("duplicate entry!");
                    // FiXME - define appropriate exception
                    throw new RuntimeException("Duplicate entry: " + result.getId());
                }
            }
        } catch (IOException e) {
            // FiXME - define appropriate exception
            throw new RuntimeException(String.format("%s: %s", e.getClass(), e.getMessage()));
        }

        // TODO - copy existing entries
        try (OutputStream os = new FileOutputStream(file);
             ZipOutputStream zos = new ZipOutputStream(os)) {
            for (SearchResult result : results) {
                addEntry(zos, result);
            }
        } catch (IOException e) {
            // FiXME - define appropriate exception
            throw new RuntimeException(String.format("%s: %s", e.getClass(), e.getMessage()));
        }

        // UNLOCK
    }

    SearchResult readSearchResult(ZipFile zf, ZipEntry ze) {
        try (InputStream is = zf.getInputStream(ze);
             BufferedInputStream bis = new BufferedInputStream(is)) {
            return MAPPER.readValue(bis, SearchResult.class);
        } catch (IOException e) {
            // FiXME - define appropriate exception
            throw new RuntimeException(String.format("%s: %s", e.getClass(), e.getMessage()));
        }
    }

    ZipEntry createEntry(String filename, byte[] content, String comment, Instant creationTime, Instant lastAccessTime) {

        final ZipEntry ze = new ZipEntry(filename);
        ze.setSize(content.length);

        if (isNotBlank(comment)) {
            ze.setComment(comment);
        }

        if (creationTime != null) {
            final FileTime fileTime = FileTime.from(creationTime);
            ze.setCreationTime(fileTime);
            ze.setLastModifiedTime(fileTime);
            ze.setLastAccessTime(fileTime);
        }

        if (lastAccessTime != null) {
            final FileTime fileTime = FileTime.from(lastAccessTime);
            ze.setLastAccessTime(fileTime);

        }

        return ze;
    }

    void addEntry(ZipOutputStream zos, VideoSearchForm form) throws IOException {
        // create zip entry
        final byte[] content = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(form).getBytes(StandardCharsets.UTF_8);
        ZipEntry ze = createEntry("request", content, null, form.getLastChecked(), null);

        // add it to the file
        zos.putNextEntry(ze);
        zos.write(content);
        zos.closeEntry();
    }

    void addEntry(ZipOutputStream zos, SearchResult result) throws IOException {
        // create zip entry
        final byte[] content = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(result).getBytes(StandardCharsets.UTF_8);
        ZipEntry ze = createEntry(result.getVideoId(), content, result.getTitle(), result.getPublishedAt(), result.getLastChecked());

        // add it to the file
        zos.putNextEntry(ze);
        zos.write(content);
        zos.closeEntry();
    }
}
