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
import com.coyotesong.dojo.youtube.model.Video;
import com.coyotesong.dojo.youtube.model.WikipediaTopic;
import com.coyotesong.dojo.youtube.repository.TagRepository;
import com.coyotesong.dojo.youtube.repository.VideoRepository;
import com.coyotesong.dojo.youtube.repository.WikipediaTopicRepository;
import com.coyotesong.dojo.youtube.repository.jooq.generated.tables.records.VideoXFreebaseTopicRecord;
import com.coyotesong.dojo.youtube.repository.jooq.generated.tables.records.VideoXWikipediaTopicRecord;
import com.coyotesong.dojo.youtube.repository.jooq.generated.tables.records.VideoRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.coyotesong.dojo.youtube.repository.jooq.generated.tables.Video.VIDEO;
import static com.coyotesong.dojo.youtube.repository.jooq.generated.tables.VideoXFreebaseTopic.VIDEO_X_FREEBASE_TOPIC;
import static com.coyotesong.dojo.youtube.repository.jooq.generated.tables.VideoXWikipediaTopic.VIDEO_X_WIKIPEDIA_TOPIC;

/**
 * Implementation of VideoRepository
 */
@Repository
public class VideoRepositoryJooq extends DAOImpl<VideoRecord, Video, String> implements VideoRepository {
    private static final Logger LOG = LoggerFactory.getLogger(VideoRepositoryJooq.class);

    private final TagRepository tagRepository;
    private final WikipediaTopicRepository wikipediaTopicRepository;

    @Autowired
    public VideoRepositoryJooq(@NotNull Configuration configuration, TagRepository tagRepository, WikipediaTopicRepository wikipediaTopicRepository) {
        super(VIDEO, Video.class, configuration);
        this.tagRepository = tagRepository;
        this.wikipediaTopicRepository = wikipediaTopicRepository;
    }

    void insertVideoXFreebaseTopics(String videoId, String etag, Collection<String> topics) {
        /*
        final List<VideoXFreebaseTopicRecord> records =
                topics.stream()
                        .map(s -> new VideoXFreebaseTopicRecord(
                                videoId,
                                s,
                                etag))
                        .toList();
        ctx().dsl().batchInsert(records).execute();
         */
    }

    void insertVideoXWikipediaTopics(String videoId, String etag, Collection<WikipediaTopic> topics) {
        /*
        final List<VideoXWikipediaTopicRecord> records =
                topics.stream()
                        .map(s -> new VideoXWikipediaTopicRecord(
                                videoId,
                                s.getKey(),
                                etag))
                        .toList();
        ctx().dsl().batchInsert(records).execute();
         */
    }

    @Override
    @NotNull
    public String getId(@NotNull Video video) {
        return video.getId();
    }

    @Override
    public void delete() {
        ctx().dsl().delete(VIDEO_X_FREEBASE_TOPIC).execute();
        ctx().dsl().delete(VIDEO_X_WIKIPEDIA_TOPIC).execute();

        ctx().dsl().delete(VIDEO).execute();
        // super.delete();
    }

    @Override
    public void deleteByChannelId(@NotNull String channelId) {
        ctx().dsl().delete(VIDEO).where(VIDEO.CHANNEL_ID.eq(channelId)).execute();
    }

    @Override
    @NotNull
    public List<Video> findByChannelId(@NotNull String channelId) {
        final List<Video> videos =
                ctx().dsl().selectFrom(VIDEO).where(VIDEO.CHANNEL_ID.eq(channelId)).fetchInto(Video.class);

        // now read videoItems...

        return videos;
    }

    @Override
    public void insert(Video video) {
        super.insert(video);

        // if (!video.getTopicCategories().isEmpty()) {
        //    wikipediaTopicRepository.merge(video.getTopicCategories());
        //}

        if (!video.getTags().isEmpty()) {
            if (LOG.isTraceEnabled()) {
                LOG.info("video({}) -> {}", video.getTitle(), Objects.toString(video.getTags()));
            }
            tagRepository.merge(video.getTags());
        }

        if (!video.getTopicIds().isEmpty()) {
            insertVideoXFreebaseTopics(video.getId(), video.getEtag(), video.getTopicIds());
        }

        if (!video.getTopicCategories().isEmpty()) {
            insertVideoXWikipediaTopics(video.getId(), video.getEtag(), video.getTopicCategories());
        }

        if (!video.getTags().isEmpty()) {
            // insertVideoXWikipediaTopics(video.getId(), video.getEtag(), video.getTopicCategories());
        }
    }

    @Override
    public void insert(Collection<Video> videos) {
        super.insert(videos);

        final List<WikipediaTopic> topics = new ArrayList<>();
        for (Video video : videos) {
            if (!video.getTopicCategories().isEmpty()) {
                topics.addAll(video.getTopicCategories());
            }
        }

        // if (topics.isEmpty()) {
        //    wikipediaTopicRepository.merge(topics);
        //}

        final List<Tag> tags = new ArrayList<>();
        for (Video video : videos) {
            if (!video.getTags().isEmpty()) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace("video({}) -> {}", video.getTitle(), Objects.toString(video.getTags()));
                }
                tags.addAll(video.getTags());
            }
        }

        if (!tags.isEmpty()) {
            tagRepository.merge(tags);
        }

        for (Video video : videos) {
            if (!video.getTopicIds().isEmpty()) {
                insertVideoXFreebaseTopics(video.getId(), video.getEtag(), video.getTopicIds());
            }

            if (!video.getTopicCategories().isEmpty()) {
                insertVideoXWikipediaTopics(video.getId(), video.getEtag(), video.getTopicCategories());
            }

            if (!video.getTags().isEmpty()) {
                // insertVideoXWikipediaTopics(video.getId(), video.getEtag(), video.getTopicCategories());
            }
        }
    }
}
