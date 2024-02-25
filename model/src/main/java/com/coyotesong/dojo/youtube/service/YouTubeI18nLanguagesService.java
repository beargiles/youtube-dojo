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

import com.coyotesong.dojo.youtube.model.I18nLanguage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * YouTube 'I18nLanguage' API client
 * <p>
 * see <a href="https://googleapis.dev/java/google-api-services-youtube/latest/index.html?com/google/api/services/youtube/YouTube.I18nLanguages.List.html">YouTube.I18nLanguages.List</a>
 */
public interface YouTubeI18nLanguagesService {

    /**
     * Retrieve information about specified i18n languages
     *
     * @param hl - language or language + country
     * @return requested languages (when available)
     */
    @NotNull
    List<I18nLanguage> getI18nLanguages(@NotNull String hl) throws IOException;
}
