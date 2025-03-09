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

package com.coyotesong.dojo.youtube.service.extensions.tabsoutliner;

import com.coyotesong.dojo.youtube.model.extensions.tabsoutliner.TreeEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of TestService
 */
@Service
public class TreeServiceImpl implements TreeService {
    private static final Logger LOG = LoggerFactory.getLogger(TreeServiceImpl.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public List<TreeEntry> loadFromFile(String filename) throws IOException {
        return scan(filename);
    }

    public List<TreeEntry> scan(String filename) throws IOException {
        final File file = new File(filename);
        Object o = MAPPER.readValue(file, Object.class);

        if (o == null) {
            LOG.info("type: null)");
        } else {
            LOG.info("type: {}", o.getClass().getName());
        }

        return List.of();
    }

    /*
    public List<TabEntry> scan(String filename) throws Exception {
        final char[] ch = new char[1];

        final List<Entry> entries = new ArrayList<>();

        final String content = Files.readString(new File(filename).toPath());
        try (PushbackReader r = new PushbackReader(new StringReader(content))) {
            // toss the first character
            if (r.read(ch) <= 0) {
                throw new EOFException();
            }
            final String header = scanHeader(r);

            while (state != State.END) {
                if (r.read(ch) <= 0) {
                    throw new EOFException();
                }
                r.unread(ch);
                if (ch[0] == '[') {
                    final Entry entry = scanEntry(r);
                    entries.add(entry);
                } else if (ch[0] == '{') {
                    final String footer = scanFooter(r);
                    state = State.END;
                } else {
                    System.out.printf("unexpected character: '%s'\n", new String(ch));
                    System.out.flush();
                    throw new IllegalStateException("Unexpected character!");
                }
            }
        }
     */


    static class Entry {
        Long code;
        String type;
        Map<String, Object> map = new LinkedHashMap<>();
        List<Integer> coords = new ArrayList<>();
    }
}
