/*
 * Copyright (c) 2023 Bear Giles <bgiles@coyotesong.com>.
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

import com.coyotesong.dojo.youtube.service.MaintenanceService;
import com.coyotesong.dojo.youtube.service.YouTubeService;
import com.google.api.services.youtube.model.VideoCategory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Implementation of MaintenanceService
 */
@Service
public class MaintenanceServiceImpl implements MaintenanceService {
    private final YouTubeService service;
    private final VideoCategoryRepository videoCategoryDao;

    public MaintenanceServiceImpl(YouTubeService service, VideoCategoryRepository videoCategoryDao) {
        this.service = service;
        this.videoCategoryDao = videoCategoryDao;
    }

    public void loadVideoCategory(String lang) throws IOException {
        final List<VideoCategory> categories = service.getVideoCategories(lang);
        videoCategoryDao.insert(categories);
    }

    public void loadVideoCategories() throws IOException {
        loadVideoCategory("de");
        loadVideoCategory("en");
        loadVideoCategory("el");
        loadVideoCategory("es");
        loadVideoCategory("fr");
        loadVideoCategory("it");
        loadVideoCategory("ru");

        // I don't know the proper ordering for these...
        loadVideoCategory("id");
        loadVideoCategory("ja");
        loadVideoCategory("ko");
        loadVideoCategory("tr");
        loadVideoCategory("zh");
    }
}
