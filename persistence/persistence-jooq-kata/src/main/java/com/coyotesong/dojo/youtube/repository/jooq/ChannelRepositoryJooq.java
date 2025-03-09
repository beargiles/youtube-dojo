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

import com.coyotesong.dojo.youtube.lang3.MyToStringStyle;
import com.coyotesong.dojo.youtube.model.Channel;
import com.coyotesong.dojo.youtube.model.ChannelSection;
import com.coyotesong.dojo.youtube.model.Playlist;
import com.coyotesong.dojo.youtube.model.WikipediaTopic;
import com.coyotesong.dojo.youtube.repository.ChannelRepository;
import com.coyotesong.dojo.youtube.repository.PlaylistRepository;
import com.coyotesong.dojo.youtube.repository.WikipediaTopicRepository;
import com.coyotesong.dojo.youtube.repository.jooq.generated.tables.records.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.*;
import org.jooq.impl.DAOImpl;
import org.jooq.impl.DefaultDSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.coyotesong.dojo.youtube.repository.jooq.generated.Tables.*;
import static com.coyotesong.dojo.youtube.repository.jooq.generated.tables.Channel.CHANNEL;
import static com.coyotesong.dojo.youtube.repository.jooq.generated.tables.ChannelSection.CHANNEL_SECTION;
import static com.coyotesong.dojo.youtube.repository.jooq.generated.tables.ChannelXFreebaseTopic.CHANNEL_X_FREEBASE_TOPIC;
import static com.coyotesong.dojo.youtube.repository.jooq.generated.tables.ChannelXWikipediaTopic.CHANNEL_X_WIKIPEDIA_TOPIC;
import static org.jooq.impl.DSL.*;

/**
 * Implementation of ChannelRepository
 * <p>
 * Notes:
 * <p>
 * - use .fetchMap() for topics ?
 * <p>
 * Open questions:
 * <p>
 * - Can we register the queries and retrieve them by name? (aka PreparedStatements)
 * <p>
 * Implementation notes (best practices):
 * <p>
 * - The queries use parameter binding instead of explicitly incorporating any
 * provided values. jOOQ should still be safer than JDBC but it's still a good
 * practice and highlights what can change in a query.
 * <p>
 * - This code follows current best practices and uses CTEs to reduce the complexity of
 * individual queries. I know this won't affect the database's optimizer but it may
 * affect jOOQ's optimizer. (I need to research this)
 * <p>
 * Other implementation notes:
 * <p>
 * - This code uses CTEs instead of database views for the Freebase and Wikipedia topics.
 * There's no change in performance unless we use __materialized__ views.
 */
@Repository
public class ChannelRepositoryJooq extends DAOImpl<ChannelRecord, Channel, String> implements ChannelRepository {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(ChannelRepositoryJooq.class);

    private final ChannelSectionRepositoryJooq channelSectionRepo;
    private final PlaylistRepository playlistRepo;
    private final WikipediaTopicRepository wikipediaTopicRepository;

    // private static final Field<String> playlistChannelIdField = PLAYLIST.field(PLAYLIST.CHANNEL_KEY.getName(), PLAYLIST.CHANNEL_KEY.getDataType());
    // private static final Param<String> playlistChannelIdParam = param("channelId", String.class);

    private static final String CHANNEL_PLAYLIST_FIELD_NAME = "playlists";

    private static final CommonTableExpression<Record3<Integer, String, String>> FREEBASE_TOPICS_CTE = name("freebaseCTE")
            .fields(CHANNEL_X_FREEBASE_TOPIC.CHANNEL_KEY.getName(), FREEBASE_TOPIC.ID.getName(), FREEBASE_TOPIC.DESCRIPTION.getName())
            .as(
                    select(CHANNEL_X_FREEBASE_TOPIC.CHANNEL_KEY, FREEBASE_TOPIC.ID, FREEBASE_TOPIC.DESCRIPTION)
                            .from(CHANNEL_X_FREEBASE_TOPIC)
                            .innerJoin(FREEBASE_TOPIC).on(CHANNEL_X_FREEBASE_TOPIC.TOPIC_KEY.eq(FREEBASE_TOPIC.KEY)));

    private static final CommonTableExpression<Record4<Integer, Integer, URL, String>> WIKIPEDIA_TOPICS_CTE = name("wikipediaCTE")
            .fields(CHANNEL_X_WIKIPEDIA_TOPIC.CHANNEL_KEY.getName(), WIKIPEDIA_TOPIC.KEY.getName(), WIKIPEDIA_TOPIC.URL.getName(), WIKIPEDIA_TOPIC.LABEL.getName())
            .as(
                    select(CHANNEL_X_WIKIPEDIA_TOPIC.CHANNEL_KEY, WIKIPEDIA_TOPIC.KEY, WIKIPEDIA_TOPIC.URL, WIKIPEDIA_TOPIC.LABEL)
                            .from(CHANNEL_X_WIKIPEDIA_TOPIC)
                            .innerJoin(WIKIPEDIA_TOPIC).on(CHANNEL_X_WIKIPEDIA_TOPIC.WIKI_KEY.eq(WIKIPEDIA_TOPIC.KEY)));

    private CloseableResultQuery<? extends ChannelRecord> findAllQuery;

    private Param<String> channelIdParam;
    private Field<String> channelIdField;

    private CloseableResultQuery<? extends ChannelRecord> findByIdQuery;

    @Autowired
    public ChannelRepositoryJooq(Configuration configuration, DefaultDSLContext dsl, PlaylistRepository playlistRepo,
                                 WikipediaTopicRepository wikipediaTopicRepository) {
        super(CHANNEL, Channel.class, configuration);
        this.channelSectionRepo = new ChannelSectionRepositoryJooq(configuration);
        this.playlistRepo = playlistRepo;
        this.wikipediaTopicRepository = wikipediaTopicRepository;

        configure(configuration);
    }


    void insertChannelXFreebaseTopics(Integer channelKey, String etag, Collection<String> topics) {
        /*
        final List<ChannelXFreebaseTopicRecord> records =
                topics.stream()
                        .map(s -> new ChannelXFreebaseTopicRecord(
                                channelKey,
                                s,
                                etag))
                        .toList();
        ctx().batchInsert(records).execute();
         */
    }

    void insertChannelXWikipediaTopics(Integer channelKey, String etag, Collection<WikipediaTopic> topics) {
        final List<ChannelXWikipediaTopicRecord> records =
                topics.stream()
                        .map(s -> new ChannelXWikipediaTopicRecord(
                                channelKey,
                                s.getKey(),
                                etag))
                        .toList();
        ctx().batchInsert(records).execute();
    }

    @Override
    public long count() {
        return super.count();
    }

    @Override
    public void insert(Channel channel) {

        // ChannelRecord rec= ChannelRecord.from(channel);
        ChannelRecord rec = ctx().newRecord(CHANNEL, channel);

        final Integer key = ctx().insertInto(CHANNEL).set(rec).returningResult(CHANNEL.KEY).fetchSingleInto(Integer.class);
        channel.setKey(key);
        // ChannelRecord rec= ChannelRecord.from(channel);

        ChannelEtagRecord etagRec = ctx().newRecord(CHANNEL_ETAG);
        etagRec.setKey(key);
        etagRec.setEtag(channel.getEtag());
        etagRec.setLastChecked(channel.getLastChecked());

        int counter = 1;
        for (ChannelSection section : channel.getSections()) {
            section.setChannelKey(key);
            section.setChannelId(channel.getChannelId());
            section.setPosition(counter++);
        }
        channelSectionRepo.insert(channel.getSections());

        counter = 1;
        for (Playlist playlist : channel.getPlaylists()) {
            playlist.setChannelKey(key);
            playlist.setChannelId(channel.getChannelId());
            playlist.setPosition(counter++);
        }
        playlistRepo.insert(channel.getPlaylists());

        /*
        List<ChannelXFreebaseTopic> freebaseTopics =
                channel.getTopicIds().stream().map(i -> ctx().newRecord(CHANNEL_X_FREEBASE_TOPIC, x));
         */

        /*
        final List<Statement> statements = new ArrayList<>();
        statements.addAll(
                channel.getSections()
                        .stream()
                        .map(s -> ctx().newRecord(CHANNEL_SECTION, s))
                        .map(p -> (Statement) ctx().insertInto(CHANNEL_SECTION).set(p))
                        .toList());

        statements.addAll(
                channel.getPlaylists()
                        .stream()
                        .map(s -> ctx().newRecord(PLAYLIST, s))
                        .map(p -> (Statement) ctx().insertInto(PLAYLIST).set(p))
                        .toList());
         */

        // add freebase topics
        // add wikipedia topics
        // add videos

        /*
        statements.add(ctx().commit());

        // insert everything in a single transaction
        ctx().begin(
                statements
        ).execute();
         */
    }

    @Override
    public void insert(Collection<Channel> channels) {
        channels.stream().forEach(this::insert);
        /*
        super.insert(channels);

        for (Channel channel : channels) {
            if (!channel.getTopicIds().isEmpty()) {
                insertChannelXFreebaseTopics(channel.getKey(), channel.getEtag(), channel.getTopicIds());
            }

            /
            if (!channel.getTopicCategories().isEmpty()) {
                wikipediaTopicRepository.merge(channel.getTopicCategories());
                insertChannelXWikipediaTopics(channel.getKey(), channel.getEtag(), channel.getTopicCategories());
            }
             /

            // channel sections
            if (!channel.getSections().isEmpty()) {
                channelSectionRepo.insert(channel.getSections());
            }

            // channel playlists
            if (!channel.getPlaylists().isEmpty()) {
                playlistRepo.insert(channel.getPlaylists());
            }
        }
        */
    }

    @Override
    public String getId(Channel channel) {
        return channel.getChannelId();
    }

    @Override
    public void delete() {
        ctx().delete(CHANNEL_SECTION).execute();

        playlistRepo.delete();

        channelSectionRepo.delete();

        ctx().delete(CHANNEL_X_FREEBASE_TOPIC).execute();
        ctx().delete(CHANNEL_X_WIKIPEDIA_TOPIC).execute();
        ctx().delete(CHANNEL).execute();

        // super.delete();
    }

    @Override
    public void deleteById(String channelId) {
        // playlistRepo.deleteByChannelKey(channelKey);

        // channelSectionRepo.deleteByChannelKey(channelKey);

        // ctx().delete(CHANNEL_X_FREEBASE_TOPIC).where(CHANNEL_X_FREEBASE_TOPIC.CHANNEL_KEY.eq(channelKey)).execute();
        // ctx().delete(CHANNEL_X_FREEBASE_TOPIC).where(CHANNEL_X_FREEBASE_TOPIC.CHANNEL_KEY.eq(channelKey)).execute();
        // ctx().delete(CHANNEL_X_WIKIPEDIA_TOPIC).where(CHANNEL_X_WIKIPEDIA_TOPIC.CHANNEL_KEY.eq(channelKey)).execute();
        // ctx().delete(CHANNEL).where(CHANNEL.KEY.eq(channelKey)).execute();

        // super.deleteById(id);
    }

    public static class ChannelSectionsType {
        String id;
        List<ChannelSection> sections;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<ChannelSection> getSections() {
            return sections;
        }

        public void setSections(List<ChannelSection> sections) {
            this.sections = sections;
        }

        public String toString() {
            return new ToStringBuilder(this, MyToStringStyle.DEFAULT_STYLE)
                    .append("id", id)
                    .append("sections", sections)
                    .build();
        }
    }

    /*
     * For internal use
     */
    /*
    public List<ChannelSectionsType> findChannelSections() {
        final CommonTableExpression<?> cte = //CHANNEL_SECTION_CTE;
        final ResultQuery<?> query = ctx().with(cte).selectFrom(cte);

        // examine(query);

        return query.fetchInto(ChannelSectionsType.class);
    }
     */

    <T extends org.jooq.Record> void examine(ResultQuery<T> query) {

        LOG.info("query:\n{}", query);
        final Results results = query.fetchMany();

        // write contents to log
        final String capture = results.stream().map(Result::format).collect(Collectors.joining("\n"));
        LOG.info("query results:\n{}", capture);
    }

    /**
     * Find all channels
     */
    @Override
    @NotNull
    public List<Channel> findAll() {
        try (CloseableResultQuery<? extends ChannelRecord> query = findAllQuery) {
            final List<Channel> channels = query.fetchInto(Channel.class);

            if (LOG.isDebugEnabled()) {
                // TODO: sanitize
                LOG.debug("findAll() -> [ {} ]", channels.stream().map(Channel::getChannelId).collect(Collectors.joining(", ")));
            }

            return channels;
        }
    }

    /**
     * Find single channel
     *
     * @param channelId
     * @return
     */
    @Override
    @Nullable
    public Channel findById(@NotNull String channelId) {
        try (CloseableResultQuery<? extends ChannelRecord> query = findByIdQuery) {
            query.getParams().forEach((s, p) -> LOG.info(s));
            query.bind("channelId", channelId);
            final Channel channel = query.fetchSingleInto(Channel.class);

            if (LOG.isDebugEnabled()) {
                // TODO: sanitize
                LOG.debug("findById({}) -> {}", channelId, channel);
            }

            return channel;
        }
    }

    @Override
    public void merge(Channel channel) {
        super.merge(channel);

        // @TODO
        /*
        if (!channel.getTopicIds().isEmpty()) {
            insertChannelXFreebaseTopics(channel.getId(), channel.getEtag(), channel.getTopicIds());
        }

        if (!channel.getTopicCategories().isEmpty()) {
            insertChannelXWikipediaTopics(channel.getId(), channel.getEtag(), channel.getTopicCategories());
        }
         */

        // channelSectionRepo.merge() ;
        // playlistRepo.merge();
        // videoRepo.merge();
    }

    /**/

    // @EventListener(ApplicationReadyEvent.class)
    // @PostConstruct
    void configure(Configuration configuration) {
        final String CHANNEL_CTE_NAME = "channels_cte";
        final String CHANNEL_SECTION_CTE_NAME = "channel_sections_cte";
        final String CHANNEL_SECTION_FIELD_NAME = "sections";
        final String PLAYLIST_CTE_NAME = "playlist_cte";
        final String PLAYLIST_FIELD_NAME = "playlists";

        CommonTableExpression<?> CHANNEL_SECTION_CTE =
                defineChannelSectionCTE(CHANNEL_SECTION_CTE_NAME, CHANNEL_SECTION_FIELD_NAME, CHANNEL_SECTION, CHANNEL_SECTION.CHANNEL_KEY, CHANNEL_SECTION.POSITION);

        CommonTableExpression<?> PLAYLIST_CTE =
                defineChannelSectionCTE(PLAYLIST_CTE_NAME, PLAYLIST_FIELD_NAME, PLAYLIST, PLAYLIST.CHANNEL_KEY, PLAYLIST.POSITION);

        CommonTableExpression<?> CHANNELS_CTE = defineChannelCTE(CHANNEL_CTE_NAME, CHANNEL, CHANNEL.KEY, CHANNEL_SECTION_CTE, PLAYLIST_CTE);

        // .with(FREEBASE_TOPICS_CTE)
        // .with(WIKIPEDIA_TOPICS_CTE)
        //                        .leftJoin(PLAYLIST).on(CHANNEL.ID.eq(PLAYLIST.CHANNEL_KEY))

        //                        .leftJoin(FREEBASE_TOPICS_CTE).on(CHANNEL.ID.eq(CHANNEL.ID.getName()))
        //                        .leftJoin(WIKIPEDIA_TOPICS_CTE).on(CHANNEL.ID.eq(CHANNEL.ID.getName()));

        this.channelIdParam = param("channelId", String.class);
        this.channelIdField = CHANNELS_CTE.field(CHANNEL.CHANNEL_ID.getName(), CHANNEL.CHANNEL_ID.getDataType());

        this.findAllQuery = getFindAllQuery(CHANNELS_CTE);
        this.findByIdQuery = getFindByIdQuery(CHANNELS_CTE, channelIdParam, channelIdField);

        if (LOG.isDebugEnabled()) {
            LOG.debug("findAllQuery: {}", findAllQuery.getSQL());
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("findByIdQuery: {}", findByIdQuery.getSQL());
        }
    }

    /**
     * Prepared statement for 'findAll()'
     *
     * @return
     */
    CloseableResultQuery<? extends ChannelRecord> getFindAllQuery(CommonTableExpression<?> channelsCte) {
        final @NotNull ResultQuery<? extends org.jooq.Record> query = ctx()
                .with(channelsCte)
                .selectFrom(channelsCte);

        @SuppressWarnings("unchecked")
        CloseableResultQuery<? extends ChannelRecord> closeableResultQuery = (CloseableResultQuery<? extends ChannelRecord>) query;
        closeableResultQuery.keepStatement(true);
        return closeableResultQuery;
    }

    /**
     * Prepared statement for 'findById(String)'
     *
     * @return
     */
    CloseableResultQuery<? extends ChannelRecord> getFindByIdQuery(CommonTableExpression<?> channelsCte, Param<String> idParam, Field<String> idField) {
        final @NotNull ResultQuery<? extends org.jooq.Record> query = ctx()
                .with(channelsCte)
                .selectFrom(channelsCte)
                .where(idParam.eq(idField));

        @SuppressWarnings("unchecked")
        CloseableResultQuery<? extends ChannelRecord> closeableResultQuery = (CloseableResultQuery<? extends ChannelRecord>) query;
        closeableResultQuery.keepStatement(true);
        return closeableResultQuery;
    }

    /**
     * Define CTE
     *
     * @param cteName
     * @param parent
     * @param parentJoinField
     * @param childCtes
     * @param <T>
     * @return
     */
    <T> CommonTableExpression<?> defineChannelCTE(String cteName, Table<?> parent, TableField<?, T> parentJoinField, CommonTableExpression<?>... childCtes) {
        final List<Field<?>> fields = new ArrayList<>(List.of(parent.fields()));

        for (CommonTableExpression<?> child : childCtes) {
            @SuppressWarnings("unchecked") final Field<T> F = (Field<T>) child.asTable().field(0);
            fields.add(with(child).select(child.field(1)).from(child).where(parentJoinField.eq(F)).asField().as(name(child.field(1).getName())));
        }

        return name(cteName).as(select(fields).from(parent));
    }

    /**
     * Define CTE
     *
     * @param cteName
     * @param listName
     * @param parent
     * @param parentJoinField
     * @param parentOrderField
     * @param <T>
     * @return
     */
    <T> CommonTableExpression<? extends Record2<T, JSONB>> defineChannelSectionCTE(String cteName, String listName, Table<?> parent, TableField<?, T> parentJoinField, TableField<?, Integer> parentOrderField) {

        final Name keyTableName = name("x");
        final Field<T> keyJoinField = field(keyTableName.append(parentJoinField.getUnqualifiedName()), parentJoinField.getDataType());

        final Name childTableName = name("group_by_table");

        final Map<String, Field<?>> childFields = new LinkedHashMap<>();
        Arrays.asList(parent.fields())
                .stream()
                .map(f -> field(name(childTableName.append(f.getUnqualifiedName())), f.getDataType()))
                .forEach(f -> childFields.put(f.getName(), f));

        @SuppressWarnings("unchecked") final Field<T> childJoinField = (Field<T>) childFields.get(parentJoinField.getName());
        @SuppressWarnings("unchecked") final Field<T> childPositionField = (Field<T>) childFields.get(parentOrderField.getName());

        // ? extends ResultQuery<Record>
        final CommonTableExpression<?> cte = name(cteName)
                .fields(parentJoinField.getName(), listName)
                .as(
                        with(keyTableName)
                                .as(selectDistinct(parentJoinField.as(keyJoinField)).from(parent))
                                .select(keyJoinField,
                                        multiset(
                                                select(childFields.values()).from(parent.as(childTableName))
                                                        .where(childJoinField.eq(keyJoinField))
                                                        .orderBy(childPositionField))
                                                .as(listName))
                                .from(keyTableName));

        @SuppressWarnings("unchecked") final CommonTableExpression<? extends Record2<T, JSONB>> results =
                (CommonTableExpression<? extends Record2<T, JSONB>>) cte;

        return results;
    }
}
