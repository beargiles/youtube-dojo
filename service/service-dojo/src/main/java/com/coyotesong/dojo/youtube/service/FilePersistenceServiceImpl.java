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

import com.coyotesong.dojo.youtube.form.VideoSearchForm;
import com.coyotesong.dojo.youtube.form.YouTubeSearchForm;
import com.coyotesong.dojo.youtube.model.Channel;
import com.coyotesong.dojo.youtube.model.SearchResult;
import com.coyotesong.dojo.youtube.model.Video;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Implementation of FilePersistenceSService
 * <p>
 * Implementation details: format is zip file containing (pretty-printed) JSON.
 * </p>
 */
@Service
public class FilePersistenceServiceImpl implements FilePersistenceService {
    private static final Logger LOG = LoggerFactory.getLogger(FilePersistenceServiceImpl.class);

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = JsonMapper
                .builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    @Override
    public void writeChannelsToFile(@NotNull String filename, @NotNull Collection<Channel> channels) throws IOException {
        try (OutputStream os = new FileOutputStream(filename);
             ZipOutputStream zos = new ZipOutputStream(os)) {

            final Set<String> keys = new HashSet<>();

            for (Channel channel : channels) {
                if (channel.getHandle().charAt(0) != '@') {
                    channel.setHandle("@" + channel.getHandle());
                }
                if (keys.contains(channel.getChannelId())) {
                    LOG.info("duplicate channel: {}, {}, \"{}\"", channel.getChannelId(), channel.getHandle(), channel.getTitle());
                } else {
                    keys.add(channel.getChannelId());
                    writeIndividualChannel(zos, channel);
                }
            }
        }
    }

    @Override
    public @NotNull List<Channel> readChannelsFromFile(@NotNull String filename) throws IOException {
        final List<Channel> channels = new ArrayList<>();

        try (InputStream is = new FileInputStream(filename);
             ZipInputStream zos = new ZipInputStream(is)) {

            ZipEntry ze = null;
            while ((ze = zos.getNextEntry()) != null) {
                channels.add(MAPPER.convertValue(zos.readAllBytes(), Channel.class));
            }
        }

        return channels;
    }

    @Override
    public void writeVideosToFile(@NotNull String filename, @NotNull Collection<Video> videos) throws IOException {
        try (OutputStream os = new FileOutputStream(filename);
             ZipOutputStream zos = new ZipOutputStream(os)) {

            final Set<String> keys = new HashSet<>();

            for (Video video : videos) {
                if (keys.contains(video.getId())) {
                    LOG.info("duplicate video: {}, {}, \"{}\"", video.getId(), video.getTitle());
                } else {
                    keys.add(video.getId());
                    writeIndividualVideo(zos, video);
                }
            }
        }
    }

    @Override
    public @NotNull List<Video> readVideosFromFile(@NotNull String filename) throws IOException {
        final List<Video> videos = new ArrayList<>();

        try (InputStream is = new FileInputStream(filename);
             ZipInputStream zos = new ZipInputStream(is)) {

            ZipEntry ze = null;
            while ((ze = zos.getNextEntry()) != null) {
                videos.add(MAPPER.convertValue(zos.readAllBytes(), Video.class));
            }
        }

        return videos;
    }

    @Override
    public void writeVideoSearchResultsToFile(String filename, @NotNull VideoSearchForm form, @NotNull Collection<SearchResult> results) throws IOException {
        try (OutputStream os = new FileOutputStream(filename);
             ZipOutputStream zos = new ZipOutputStream(os)) {

            writeIndividualSearchForm(zos, form);

            for (SearchResult result : results) {
                writeIndividualSearchResult(zos, form, result);
            }
        }
    }

    @Override
    public @NotNull VideoSearchForm readVideoSearchFormFromFile(@NotNull String filename) throws IOException {
        try (ZipFile zf = new ZipFile(filename)) {
            final ZipEntry ze = zf.getEntry("request");
            if (ze == null) {
                LOG.warn("{}: file does not contain 'request' entry", filename);
                throw new IOException("file does not contain 'request' entry");
            }
            try (InputStream is = zf.getInputStream(ze)) {
                return MAPPER.readValue(is, VideoSearchForm.class);
            }
        }
    }

    @Override
    public @NotNull List<SearchResult> readSearchResultsFromFile(@NotNull String filename) throws IOException {
        final List<SearchResult> results = new ArrayList<>();

        try (ZipFile zf = new ZipFile(filename)) {
            final Enumeration<? extends ZipEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                final ZipEntry ze = entries.nextElement();
                if (!"request".equals(ze.getName())) {
                    try (InputStream is = zf.getInputStream(ze)) {
                        results.add(MAPPER.readValue(is, SearchResult.class));
                    }
                }
            }
        }

        return results;
    }

    /**
     * Write individual channel
     * @param zos
     * @param channel
     * @throws IOException
     */
    void writeIndividualChannel(ZipOutputStream zos, Channel channel) throws IOException {
        final byte[] content = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(channel).getBytes(StandardCharsets.UTF_8);
        final ZipEntry ze = new ZipEntry(channel.getChannelId());
        ze.setComment(channel.getTitle());
        ze.setSize(content.length);
        final FileTime fileTime = FileTime.from(channel.getLastChecked());
        ze.setCreationTime(fileTime);
        ze.setLastModifiedTime(fileTime);
        ze.setLastAccessTime(fileTime);
        zos.putNextEntry(ze);
        zos.write(content);
        zos.closeEntry();
    }

    /**
     * Write individual video
     * @param zos
     * @param video
     * @throws IOException
     */
    void writeIndividualVideo(ZipOutputStream zos, Video video) throws IOException {
        final byte[] content = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(video).getBytes(StandardCharsets.UTF_8);
        final ZipEntry ze = new ZipEntry(video.getId());
        ze.setComment(video.getTitle());
        ze.setSize(content.length);
        final FileTime fileTime = FileTime.from(video.getLastChecked());
        ze.setCreationTime(fileTime);
        ze.setLastModifiedTime(fileTime);
        ze.setLastAccessTime(fileTime);
        zos.putNextEntry(ze);
        zos.write(content);
        zos.closeEntry();
    }

    /**
     * Write individual search form
     * @param zos
     * @param form
     * @throws IOException
     */
    void writeIndividualSearchForm(ZipOutputStream zos, YouTubeSearchForm form) throws IOException {
        final byte[] content = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(form).getBytes(StandardCharsets.UTF_8);
        final ZipEntry ze = new ZipEntry("request");
        ze.setSize(content.length);
        if (form.getLastChecked() != null) {
            final FileTime fileTime = FileTime.from(form.getLastChecked());
            ze.setCreationTime(fileTime);
            ze.setLastModifiedTime(fileTime);
            ze.setLastAccessTime(fileTime);
        }

        zos.putNextEntry(ze);
        zos.write(content);
        zos.closeEntry();
    }

    void writeIndividualSearchResult(ZipOutputStream zos, YouTubeSearchForm form, SearchResult result) throws IOException {
        final byte[] content = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(result).getBytes(StandardCharsets.UTF_8);
        final ZipEntry ze = new ZipEntry(result.getVideoId());
        ze.setSize(content.length);
        ze.setComment(result.getChannelTitle() + " | " + result.getTitle());
        final FileTime fileTime = FileTime.from(result.getLastChecked());
        ze.setCreationTime(fileTime);
        if (form.getLastChecked() != null) {
            ze.setLastAccessTime(FileTime.from(form.getLastChecked()));
        } else {
            ze.setLastAccessTime(fileTime);
        }

        zos.putNextEntry(ze);
        zos.write(content);
        zos.closeEntry();
    }
}
