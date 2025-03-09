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
import com.coyotesong.dojo.youtube.model.WikipediaTopic;
import com.coyotesong.dojo.youtube.repository.jooq.WikipediaTopicRepositoryJooq;
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

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for WikipediaTopicRepository
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                PersistenceTestConfiguration.class,
                WikipediaTopicRepositoryJooq.class
        })
@Testcontainers
@ActiveProfiles("test")
public class WikipediaTopicRepositoryTest {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(WikipediaTopicRepositoryTest.class);

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

    private final WikipediaTopicRepository wikipediaTopicRepository;

    /**
     * Constructor
     * @param testObjectFactory test object factory
     * @param wikipediaTopicRepository repository to be tested
     */
    @Autowired
    public WikipediaTopicRepositoryTest(TestObjectFactory testObjectFactory, WikipediaTopicRepository wikipediaTopicRepository) {
        this.wikipediaTopicRepository = wikipediaTopicRepository;
    }

    @BeforeEach
    public void setup() {
    }

    @Test
    public void shouldX() throws IOException {
        final String classpath = "/db/wikipedia-topics.csv";

        wikipediaTopicRepository.delete();

        final List<WikipediaTopic> expected = ((WikipediaTopicRepositoryJooq) wikipediaTopicRepository).loadTestDataFromClasspath(classpath);
        expected.forEach(topic -> topic.setKey(null));

        wikipediaTopicRepository.insert(expected);

        final List<WikipediaTopic> actual = wikipediaTopicRepository.findAll();

        assertEquals(expected, actual);
    }
}
