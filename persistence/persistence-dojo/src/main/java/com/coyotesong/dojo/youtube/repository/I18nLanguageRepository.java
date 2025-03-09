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

package com.coyotesong.dojo.youtube.repository;

import com.coyotesong.dojo.youtube.model.I18nLanguage;

import java.util.Collection;
import java.util.List;

/**
 * Persistence mechanism for I18n values.
 */
@SuppressWarnings("unused")
public interface I18nLanguageRepository {
    void delete();

    void insert(Collection<I18nLanguage> languages);

    List<I18nLanguage> findAll();

    I18nLanguage findByCodeAndLocale(String code, String locale);

    List<I18nLanguage> findAllForLocale(String locale);
}
