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

package com.coyotesong.dojo.youtube.service;

import com.coyotesong.dojo.youtube.config.PersistenceTestConfiguration;
import com.coyotesong.dojo.youtube.config.YouTubeContext;
import com.coyotesong.dojo.youtube.container.PostgreSQLContainerWithFlyway;
import com.coyotesong.dojo.youtube.model.Channel;
import com.coyotesong.dojo.youtube.security.LogSanitizerImpl;
import com.coyotesong.dojo.youtube.service.youTubeClient.ClientForChannelListFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.coyotesong.dojo.youtube.service.TestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test YouTubeChannelsService
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                PersistenceTestConfiguration.class,
                LogSanitizerImpl.class,
                YouTubeContext.class,
                ClientForChannelListFactory.class,
                YouTubeChannelsServiceImpl.class
        })
@ActiveProfiles({"test"})
public class YouTubeChannelsServiceITest extends AbstractYouTubeServiceITest {
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

    // @Value("${quotaExceeded}")
    private Boolean quotaExceeded = false;

    private final YouTubeChannelsService channelsService;

    @Autowired
    public YouTubeChannelsServiceITest(YouTubeChannelsService channelsService) {
        super(channelsService);
        this.channelsService = channelsService;
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelWithValidChannel_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final Channel channel = channelsService.getChannel(TEST_CHANNEL_ID);
        assertThat("channel not found", channel, notNullValue());
        if (channel != null) {
            assertThat("channel id does not match", channel.getChannelId(), equalTo(TEST_CHANNEL_ID));
            assertThat("channel custom URL does not match", channel.getHandle(), equalTo("@" + TEST_CHANNEL_USERNAME));
        }
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelsWithValidChannel_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final List<Channel> channels = channelsService.getChannels(Collections.singletonList(TEST_CHANNEL_ID));
        assertThat("no channels found", channels, not(empty()));
        for (Channel channel : channels) {
            assertThat("channel id does not match", channel.getChannelId(), equalTo(TEST_CHANNEL_ID));
            assertThat("channel custom URL does not match", channel.getHandle(), equalTo("@" + TEST_CHANNEL_USERNAME));
        }
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelsWithEmptyList_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final List<Channel> channels = channelsService.getChannels(Collections.emptyList());
        assertThat("channels found", channels, empty());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelForHandleWithValidHandle_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final Channel channel = channelsService.getChannelForHandle(TEST_CHANNEL_HANDLE);
        assertThat("channel not found", channel, notNullValue());
        assertThat("channel custom URL does not match", channel.getHandle(), equalTo("@" + TEST_CHANNEL_USERNAME));
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelForUsernameWithValidUsername_Then_Success() throws IOException {
        assumeFalse(quotaExceeded);
        final Channel channel = channelsService.getChannelForUsername(TEST_CHANNEL_USERNAME);
        assertThat("channel not found", channel, notNullValue());
        assertThat("channel custom URL does not match", channel.getHandle(), equalTo("@" + TEST_CHANNEL_USERNAME));
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelWithInvalidChannel_Then_ReturnNull() throws IOException {
        assumeFalse(quotaExceeded);
        final Channel channel = channelsService.getChannel(BAD_TEST_CHANNEL_ID);
        assertThat("channel should be null", channel, nullValue());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelForHandleWithInvalidHandle_Then_ReturnNull() throws IOException {
        assumeFalse(quotaExceeded);
        final Channel channel = channelsService.getChannelForHandle(BAD_TEST_CHANNEL_HANDLE);
        assertThat("channel should be null", channel, nullValue());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelForUsernameWithInvalidUsername_Then_ReturnNull() throws IOException {
        assumeFalse(quotaExceeded);
        final Channel channel = channelsService.getChannelForUsername(BAD_TEST_CHANNEL_USERNAME);
        assertThat("channel should be null", channel, nullValue());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelWithNull_Then_ThrowException() {
        assumeFalse(quotaExceeded);
        assertThrows(
                IllegalArgumentException.class,
                () -> channelsService.getChannel(null),
                "Expected getChannel(null) to throw exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelsWithNull_Then_ThrowException() {
        assumeFalse(quotaExceeded);
        assertThrows(
                IllegalArgumentException.class,
                () -> channelsService.getChannels(null),
                "Expected getChannels(null) to throw exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelForHandleWithNull_Then_ThrowException() {
        assumeFalse(quotaExceeded);
        assertThrows(
                IllegalArgumentException.class,
                () -> channelsService.getChannelForHandle(null),
                "Expected getChannelForHandle(null) to throw exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelForUsernameWithNull_Then_ThrowException() {
        assumeFalse(quotaExceeded);
        assertThrows(
                IllegalArgumentException.class,
                () -> channelsService.getChannelForUsername(null),
                "Expected getChannelForUsername(null) to throw exception");
    }
}
