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

import com.coyotesong.dojo.youtube.model.ChannelSection;
import com.coyotesong.dojo.youtube.repository.jooq.generated.tables.records.ChannelSectionRecord;
import com.coyotesong.dojo.youtube.repository.jooq.generated.tables.records.ChannelSectionXChannelRecord;
import com.coyotesong.dojo.youtube.repository.jooq.generated.tables.records.ChannelSectionXPlaylistRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.coyotesong.dojo.youtube.repository.jooq.generated.tables.ChannelSection.CHANNEL_SECTION;
import static com.coyotesong.dojo.youtube.repository.jooq.generated.tables.ChannelSectionXChannel.CHANNEL_SECTION_X_CHANNEL;
import static com.coyotesong.dojo.youtube.repository.jooq.generated.tables.ChannelSectionXPlaylist.CHANNEL_SECTION_X_PLAYLIST;

class ChannelSectionRepositoryJooq extends DAOImpl<ChannelSectionRecord, ChannelSection, Integer> {
    private static final Logger LOG = LoggerFactory.getLogger(ChannelSectionRepositoryJooq.class);

    public ChannelSectionRepositoryJooq(Configuration configuration) {
        super(CHANNEL_SECTION, ChannelSection.class, configuration);
    }

    @Override
    public Integer getId(ChannelSection channelSection) {
        return channelSection.getKey();
    }

    public void delete() {
        ctx().delete(CHANNEL_SECTION_X_CHANNEL).execute();
        ctx().delete(CHANNEL_SECTION_X_PLAYLIST).execute();
        ctx().delete(CHANNEL_SECTION).execute();
        // super.delete();
    }

    public void deleteByChannelId(@NotNull String channelId) {
        // ctx().dsl().delete(CHANNEL_SECTION_X_CHANNEL).where(CHANNEL_SECTION_X_CHANNEL.CHANNEL_ID.eq(channelId)).execute();
        // ctx().dsl().delete(CHANNEL_SECTION).where(CHANNEL_SECTION.CHANNEL_SID.eq(channelSid)).execute();
        ctx().delete(CHANNEL_SECTION).where(CHANNEL_SECTION.CHANNEL_ID.eq(channelId)).execute();
    }

    public void deleteByChannelKey(@NotNull Integer channelKey) {
        // ctx().dsl().delete(CHANNEL_SECTION_X_CHANNEL).where(CHANNEL_SECTION_X_CHANNEL.CHANNEL_ID.eq(channelId)).execute();
        // ctx().dsl().delete(CHANNEL_SECTION).where(CHANNEL_SECTION.CHANNEL_SID.eq(channelSid)).execute();
        ctx().delete(CHANNEL_SECTION).where(CHANNEL_SECTION.CHANNEL_KEY.eq(channelKey)).execute();
    }

    @NotNull
    public List<ChannelSection> findByChannelId(@NotNull String channelId) {
        return ctx().selectFrom(CHANNEL_SECTION).where(CHANNEL_SECTION.CHANNEL_ID.eq(channelId)).fetchInto(ChannelSection.class);
    }

    @NotNull
    public List<ChannelSection> findByChannelKey(@NotNull Integer channelKey) {
        return ctx().selectFrom(CHANNEL_SECTION).where(CHANNEL_SECTION.CHANNEL_KEY.eq(channelKey)).fetchInto(ChannelSection.class);
    }

    public void insert(@NotNull ChannelSection section) {
        final ChannelSectionRecord rec = ctx().newRecord(CHANNEL_SECTION, section);
        final Integer key = ctx().insertInto(CHANNEL_SECTION).set(rec).returningResult(CHANNEL_SECTION.KEY).fetchSingleInto(Integer.class);
        section.setKey(key);

        if (!section.getChannelIds().isEmpty()) {
            LOG.error("unimplemented!");
            // insertChannelIds(section);
        }

        if (!section.getPlaylistIds().isEmpty()) {
            LOG.error("unimplemented!");
            // insertPlaylistIds(section);
        }
    }

    public void insert(@NotNull Collection<ChannelSection> sections) {
        sections.stream().forEach(this::insert);
    }

    void insertChannelIds(ChannelSection section) {
        if (!section.getChannelIds().isEmpty()) {
            final List<ChannelSectionXChannelRecord> channelIdRecords = new ArrayList<>();

            for (int position = 0; position < section.getChannelIds().size(); position++) {
                channelIdRecords.add(
                        new ChannelSectionXChannelRecord(
                                section.getChannelKey(),
                                section.getPosition(),
                                position,
                                section.getChannelIds().get(position),
                                section.getEtag()));
            }

            ctx().dsl().batchInsert(channelIdRecords).execute();
        }
    }

    void insertPlaylistIds(ChannelSection section) {
        if (!section.getPlaylistIds().isEmpty()) {
            final List<ChannelSectionXPlaylistRecord> playlistIdRecords = new ArrayList<>();

            for (int position = 0; position < section.getPlaylistIds().size(); position++) {
                playlistIdRecords.add(
                        new ChannelSectionXPlaylistRecord(
                                section.getChannelKey(),
                                section.getPosition(),
                                position,
                                null,
                                // section.getPlaylistIds().get(position),
                                section.getEtag()));
            }

            ctx().dsl().batchInsert(playlistIdRecords).execute();
        }
    }
}
