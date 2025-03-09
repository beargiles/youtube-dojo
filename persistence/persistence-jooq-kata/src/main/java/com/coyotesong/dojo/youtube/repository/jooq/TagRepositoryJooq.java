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

package com.coyotesong.dojo.youtube.repository.jooq;

import com.coyotesong.dojo.youtube.model.Tag;
import com.coyotesong.dojo.youtube.repository.TagRepository;
import com.coyotesong.dojo.youtube.repository.jooq.generated.tables.records.TagRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.coyotesong.dojo.youtube.repository.jooq.generated.tables.Tag.TAG;

@Repository
public class TagRepositoryJooq extends DAOImpl<TagRecord, Tag, Integer> implements TagRepository {
    private static final Logger LOG = LoggerFactory.getLogger(TagRepositoryJooq.class);

    @Autowired
    public TagRepositoryJooq(@NotNull Configuration configuration) {
        super(TAG, Tag.class, configuration);
    }

    @Override
    @NotNull
    public Integer getId(@NotNull Tag tag) {
        return tag.getId();
    }

    @Override
    @NotNull
    public Optional<Tag> findOptionalByOriginal(@NotNull String original) {
        final Optional<TagRecord> record = ctx().dsl().selectFrom(TAG).where(TAG.ORIGINAL.eq(original)).fetchOptional();
        if (record.isPresent()) {
            return Optional.of(record.get().into(Tag.class));
        }
        return Optional.empty();
    }

    @Override
    public void merge(@NotNull Collection<Tag> tags) {

        final Map<String, Tag> map = new HashMap<>();
        tags.forEach(t -> map.put(t.getOriginal(), t));

        final List<String> existing = ctx().dsl().selectQuery(TAG).fetch(TAG.ORIGINAL);
        existing.forEach(map::remove);

        super.insert(map.values());
    }
}
