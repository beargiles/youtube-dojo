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

import com.coyotesong.dojo.youtube.container.PostgreSQLContainerWithFlyway;
import com.coyotesong.dojo.youtube.model.Channel;
import com.coyotesong.dojo.youtube.model.ChannelSection;
import com.coyotesong.dojo.youtube.model.Playlist;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                PersistenceTestConfiguration.class
        })
@Testcontainers
@ActiveProfiles({"test"})
public class ChannelRepositoryTest {
    private static final Logger LOG = LoggerFactory.getLogger(ChannelRepositoryTest.class);

    @Container
    @ServiceConnection
    // @RestartScope
    static PostgreSQLContainerWithFlyway<?> postgres = new PostgreSQLContainerWithFlyway<>(
            "postgres:16-alpine" // , resources
    );

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgres.getJdbcUrl());
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driverClassName", postgres::getDriverClassName);
        registry.add("spring.datasource.testQueryString", postgres::getTestQueryString);
    }

    @BeforeAll
    static void startServer() {
        if (!postgres.isRunning()) {
            postgres.start();
        }
    }

    @AfterAll
    static void shutdownServer() {
        if (postgres.isRunning()) {
            postgres.stop();
        }
    }

    private final ChannelRepository channelRepository;

    private final Channel expected1;
    private final Channel expected2;

    private final ChannelSection cs1;
    private final ChannelSection cs2;

    private final Playlist playlist1;
    private final Playlist playlist2;

    /**
     * Constructor
     * @param testObjectFactory test object factory
     * @param channelRepository repository to be tested
     */
    @Autowired
    public ChannelRepositoryTest(TestObjectFactory testObjectFactory, ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;

        expected1 = testObjectFactory.newChannel(1);
        expected2 = testObjectFactory.newChannel(2);

        cs1 = testObjectFactory.newChannelSection(1);
        cs2 = testObjectFactory.newChannelSection(2);

        playlist1 = testObjectFactory.newPlaylist(1);
        playlist2 = testObjectFactory.newPlaylist(2);
    }

    void resetTestObjects() {
        expected1.setKey(null);
        expected2.setKey(null);

        cs1.setKey(null);
        cs1.setChannelKey(null);
        cs2.setKey(null);
        cs2.setChannelKey(null);

        playlist1.setKey(null);
        playlist1.setChannelKey(null);
        playlist2.setKey(null);
        playlist2.setChannelKey(null);

        cs1.setChannelId(cs1.getChannelId());
        cs1.setPosition(1);
        cs2.setChannelId(cs1.getChannelId());
        cs2.setPosition(2);
        expected1.getSections().clear();
        expected1.getSections().add(cs1);
        expected1.getSections().add(cs2);

        playlist1.setChannelId(expected1.getChannelId());
        playlist1.setPosition(1);
        playlist2.setChannelId(expected1.getChannelId());
        playlist2.setPosition(2);
        expected1.getPlaylists().clear();
        expected1.getPlaylists().add(playlist1);
        expected1.getPlaylists().add(playlist2);
    }

    @BeforeEach
    public void setup() {
        this.channelRepository.delete();
        resetTestObjects();
    }

    @Test
    public void testFindAll() {
        channelRepository.insert(expected1);
        channelRepository.insert(expected2);
    }

    @Test
    public void testFindById() {
        channelRepository.insert(expected1);
        channelRepository.insert(expected2);

        final Channel actual1 = channelRepository.findById(expected1.getChannelId());
        final Channel actual2 = channelRepository.findById(expected2.getChannelId());

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);

        LOG.info("{}", actual1);
    }
}
