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

package com.coyotesong.dojo.youtube.repository.poi;

import com.coyotesong.dojo.youtube.model.Channel;
import com.coyotesong.dojo.youtube.repository.ChannelRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

public class ChannelRepositoryPoi implements ChannelRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ChannelRepositoryPoi.class);

    private final File file;

    public ChannelRepositoryPoi(File file) {
        this.file = file;

        // create workbook if it doesn't already exist
        if (!file.exists()) {
            createWorkbook();
        }
    }

    void createWorkbook() {
        try (Workbook wb = new XSSFWorkbook()) {
            LOG.info("creating new workbook at {}", file.getAbsolutePath());
            try (OutputStream os = new FileOutputStream(file)) {
                wb.write(os);
            }
        } catch (Exception e) {
            LOG.error("{}: {}", e.getClass().getName(), e.getMessage(), e);
        }
    }

    @Override
    public long count() {
        return -1;
    }

    @Override
    public void delete() {
        // LOCK
        file.delete();
        createWorkbook();
        // UNLOCK
    }

    @Override
    public void deleteById(String channelId) {

    }

    @Override
    public boolean existsById(String channelId) {
        return false;
    }

    @Override
    public @NotNull List<Channel> findAll() {
        return List.of();
    }

    @Override
    public @Nullable Channel findById(String channelId) {
        return null;
    }

    /**
     * Save channel details
     *
     * @param channels
     */
    @Override
    public void insert(Collection<Channel> channels) {
        // LOCK
        try (ChannelWorkbook wb = new ChannelWorkbook(file)) {
            wb.addChannels(channels);
        } catch (Exception e) {
            LOG.error("{}: {}", e.getClass().getName(), e.getMessage(), e);
            // TODO - do more?
        }
        // UNLOCK
    }

    @Override
    public void insert(Channel channel) {

    }

    @Override
    public void merge(Channel channel) {

    }

}
