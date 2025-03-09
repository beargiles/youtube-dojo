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
import com.coyotesong.dojo.youtube.model.I18nRegion;
import com.coyotesong.dojo.youtube.model.VideoCategory;
import com.coyotesong.dojo.youtube.repository.I18nLanguageRepository;
import com.coyotesong.dojo.youtube.repository.I18nRegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of I18nService
 */
@Service
public class I18nServiceImpl implements I18nService {
    private static final Logger LOG = LoggerFactory.getLogger(I18nServiceImpl.class);

    private final YouTubeI18nLanguagesService languageService;
    private final YouTubeI18nRegionService regionService;
    private final I18nLanguageRepository languageRepository;
    private final I18nRegionRepository regionRepository;

    @Autowired
    public I18nServiceImpl(final YouTubeI18nLanguagesService languageService,
                           final YouTubeI18nRegionService regionService,
                           final I18nLanguageRepository languageRepository,
                           final I18nRegionRepository regionRepository) {
        this.languageService = languageService;
        this.regionService = regionService;
        this.languageRepository = languageRepository;
        this.regionRepository = regionRepository;
    }

    public void bootstrap() throws IOException {
        languageRepository.delete();
        regionRepository.delete();

        // get list of all locales known to us via test query
        final List<I18nLanguage> languages = languageService.getI18nLanguages("en");
        /*
        I18nLanguage en = new I18nLanguage();
        en.setCode("en");
        en.setName("English");
        en.setHl("en");

        I18nLanguage es = new I18nLanguage();
        es.setCode("es");
        es.setName("Espanol");
        es.setHl("es");
        final List<I18nLanguage> languages = List.of(es);
         */

        // retrieve the information for each of those locales
        languages.stream().map(I18nLanguage::getCode).sorted().distinct().forEach(locale -> {
            try {
                languageRepository.insert(languageService.getI18nLanguages(locale));
                regionRepository.insert(regionService.getI18nRegions(locale));
            } catch (IOException e) {
                LOG.warn("Error during bootstrap: {}", e.getMessage(), e);
            }
        });
    }

    @Override
    public void export() throws IOException {

    }

    String nullp(String s) {
        if (s == null) {
            return "\\N";
        }
        return s.trim().replace("\t", "\\t").replace("\r", "").replace("\n", "\\\\n");
    }

    void createI18nDatabase(String filename, Collection<I18nLanguage> allLanguages,
                                   Collection<I18nRegion> allRegions, Collection<VideoCategory> allCategories) throws IOException {

        try (Writer w = new FileWriter(filename);
             PrintWriter pw = new PrintWriter(w)) {
            for (I18nLanguage language : allLanguages) {
                pw.printf("%s\t%s\t%s\n",
                        nullp(language.getHl()), language.getCode(),
                        nullp(language.getName()));
            }
            pw.println();

            for (I18nRegion region : allRegions) {
                pw.printf("%s\t%s\t%s\t%s\n",
                        nullp(region.getHl()), nullp(region.getGl()),
                        region.getCode(), region.getName());
            }
            pw.println();

            // channelId is always 'UCBR8-60-B28hp2BmDPdntcQ'
            for (VideoCategory category : allCategories) {
                pw.printf("%s\t%s\t%s\t%s\t%s\t%s\n",
                        category.getEtag(), category.getParentEtag(), category.getAssignable(),
                        nullp(category.getHl()), category.getId(), category.getTitle());

            }
        }
    }
}
