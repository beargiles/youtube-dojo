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

package com.coyotesong.dojo.youtube.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LocaleLoader {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(LocaleLoader.class);

    /**
     * Load arbitrary resource file.
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public List<String> loadResourceFile(String filename) throws IOException {
        final Resource resource = new ClassPathResource(filename);
        try (InputStream is = resource.getInputStream();
             InputStreamReader r = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(r)) {
            final List<String> list = reader.lines().collect(Collectors.toList());

            // drop header
            return list.subList(1, list.size());
        }
    }

    /**
     * Load L18N languages
     *
     * @return
     * @throws IOException
     */
    public List<Language> loadLanguages() throws IOException {
        final List<String> csv = loadResourceFile("i18n/languages.csv");
        final List<Language> result = new ArrayList<>();

        for (final String line : csv) {
            final int idx0 = line.indexOf(",");
            if (idx0 > 0) {
                final int idx1 = line.indexOf(",", idx0 + 1);
                if (idx1 > 0) {
                    result.add(new Language(line.substring(0, idx0), line.substring(idx0 + 1, idx1), line.substring(idx1 + 1)));
                }
            }
        }

        return result;
    }

    /**
     * Load L18N regions
     *
     * @return
     * @throws IOException
     */
    public List<Region> loadRegions() throws IOException {
        final List<String> csv = loadResourceFile("i18n/regions.csv");
        final List<Region> result = new ArrayList<>();

        for (final String line : csv) {
            final int idx0 = line.indexOf(",");
            if (idx0 > 0) {
                final int idx1 = line.indexOf(",", idx0 + 1);
                if (idx1 > 0) {
                    final int idx2 = line.indexOf(",", idx1 + 1);
                    if (idx2 > 0) {
                        result.add(new Region(line.substring(0, idx0), line.substring(idx0 + 1, idx1), line.substring(idx1 + 1, idx2), line.substring(idx2 + 1)));
                    }
                }
            }
        }

        return result;
    }
}
