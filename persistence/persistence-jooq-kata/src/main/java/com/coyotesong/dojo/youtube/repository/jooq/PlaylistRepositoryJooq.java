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

import com.coyotesong.dojo.youtube.model.Playlist;
import com.coyotesong.dojo.youtube.repository.PlaylistRepository;
import com.coyotesong.dojo.youtube.repository.jooq.generated.tables.records.ChannelSectionRecord;
import com.coyotesong.dojo.youtube.repository.jooq.generated.tables.records.PlaylistRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.jooq.SelectConditionStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static com.coyotesong.dojo.youtube.repository.jooq.generated.tables.ChannelSection.CHANNEL_SECTION;
import static com.coyotesong.dojo.youtube.repository.jooq.generated.tables.Playlist.PLAYLIST;

/**
 * Implementation of PlaylistRepository
 */
@Repository
public class PlaylistRepositoryJooq extends DAOImpl<PlaylistRecord, Playlist, Integer> implements PlaylistRepository {
    private static final Logger LOG = LoggerFactory.getLogger(PlaylistRepositoryJooq.class);

    @Autowired
    public PlaylistRepositoryJooq(Configuration configuration) {
        super(PLAYLIST, Playlist.class, configuration);
    }

    @Override
    public Integer getId(Playlist playlist) {
        return playlist.getKey();
    }

    @Override
    public void delete() {
        ctx().delete(PLAYLIST).execute();
        // super.delete();
    }

    @Override
    public void deleteByChannelId(String channelId) {
        ctx().delete(PLAYLIST).where(PLAYLIST.CHANNEL_ID.eq(channelId)).execute();
    }

    @Override
    public void deleteByChannelKey(Integer channelKey) {
        ctx().delete(PLAYLIST).where(PLAYLIST.CHANNEL_KEY.eq(channelKey)).execute();
    }

    @Override
    public List<Playlist> findByChannelId(String channelId) {
        SelectConditionStep<PlaylistRecord> step = ctx().selectFrom(PLAYLIST).where(PLAYLIST.CHANNEL_ID.eq(channelId));
        // LOG.info(step.getSQL());

        final List<Playlist> playlists =
                ctx().selectFrom(PLAYLIST).where(PLAYLIST.CHANNEL_ID.eq(channelId)).fetchInto(Playlist.class);

        // now read playlistItems...

        return playlists;
    }

    @Override
    public List<Playlist> findByChannelKey(Integer channelKey) {
        SelectConditionStep<PlaylistRecord> step = ctx().selectFrom(PLAYLIST).where(PLAYLIST.CHANNEL_KEY.eq(channelKey));
        // LOG.info(step.getSQL());

        final List<Playlist> playlists =
                ctx().selectFrom(PLAYLIST).where(PLAYLIST.CHANNEL_KEY.eq(channelKey)).fetchInto(Playlist.class);

        // now read playlistItems...

        return playlists;
    }

    @Override
    public void insert(@NotNull Playlist playlist) {
        final PlaylistRecord rec = ctx().newRecord(PLAYLIST, playlist);
        final Integer key = ctx().insertInto(PLAYLIST).set(rec).returningResult(PLAYLIST.KEY).fetchSingleInto(Integer.class);
        playlist.setKey(key);

        // TODO: thumbnails
    }

    @Override
    public void insert(@NotNull Collection<Playlist> playlists) {
        playlists.forEach(this::insert);
    }
}
