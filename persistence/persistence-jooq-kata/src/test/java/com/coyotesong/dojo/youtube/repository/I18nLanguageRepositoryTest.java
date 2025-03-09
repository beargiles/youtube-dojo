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
import com.coyotesong.dojo.youtube.model.I18nLanguage;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                PersistenceTestConfiguration.class
        })
@Testcontainers
@ActiveProfiles({"test"})
public class I18nLanguageRepositoryTest {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(I18nLanguageRepositoryTest.class);

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

    private final I18nLanguageRepository languageRepository;

    private final I18nLanguage expected1;
    private final I18nLanguage expected2;

    /**
     * Constructor
     * @param testObjectFactory test object factory
     * @param languageRepository repository implementation to be tested
     */
    @Autowired
    public I18nLanguageRepositoryTest(TestObjectFactory testObjectFactory, I18nLanguageRepository languageRepository) {
        this.languageRepository = languageRepository;

        this.expected1 = testObjectFactory.newLanguage(1);
        this.expected2 = testObjectFactory.newLanguage(2);
    }

    @BeforeEach
    public void setup() {
        this.languageRepository.delete();
        this.expected1.setKey(null);
        this.expected2.setKey(null);
    }

    @Test
    public void testFindAll() {
        languageRepository.insert(List.of(expected1, expected2));

        final List<I18nLanguage> actual = languageRepository.findAll();
        assertEquals(2, actual.size());
        if (actual.get(0).getKey().equals(expected1.getKey())) {
            assertEquals(expected1, actual.get(0));
            assertEquals(expected2, actual.get(1));
        } else {
            assertEquals(expected1, actual.get(1));
            assertEquals(expected2, actual.get(2));
        }
    }

    @Test
    public void testFindAllForLocale() {
        languageRepository.insert(List.of(expected1, expected2));

        final List<I18nLanguage> actual1 = languageRepository.findAllForLocale(expected1.getHl());
        assertEquals(1, actual1.size());
        assertEquals(expected1, actual1.get(0));

        final List<I18nLanguage> actual2 = languageRepository.findAllForLocale(expected2.getHl());
        assertEquals(1, actual2.size());
        assertEquals(expected2, actual2.get(0));
    }

    @Test
    public void testFindByCodeAndLocale() {
        languageRepository.insert(List.of(expected1, expected2));

        final I18nLanguage actual1 = languageRepository.findByCodeAndLocale(expected1.getCode(), expected1.getHl());
        final I18nLanguage actual2 = languageRepository.findByCodeAndLocale(expected2.getCode(), expected2.getHl());

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }
}
