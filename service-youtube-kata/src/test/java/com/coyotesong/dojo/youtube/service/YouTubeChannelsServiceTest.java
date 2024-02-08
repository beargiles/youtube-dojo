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

import com.coyotesong.dojo.youtube.config.YouTubeContext;
import com.coyotesong.dojo.youtube.model.Channel;
import com.coyotesong.dojo.youtube.security.LogSanitizerImpl;
import com.coyotesong.dojo.youtube.service.youtubeClient.ClientForChannelListFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test YouTubeChannelsService
 * <p>
 * NOTE: this is actually an integration test
 */
// @ContextConfiguration
@ExtendWith(SpringExtension.class)
@Import({
        LogSanitizerImpl.class
})
@ImportAutoConfiguration(classes = {
        YouTubeContext.class
})
@SpringBootTest(classes = {
        ClientForChannelListFactory.class,
        YouTubeChannelsServiceImpl.class
})
public class YouTubeChannelsServiceTest {
    private static final String TEST_CHANNEL_ID = "UCciQ8wFcVoIIMi-lfu8-cjQ";
    private static final String TEST_CHANNEL_HANDLE = "whatdamath";
    private static final String TEST_CHANNEL_USERNAME = "whatdamath";
    private static final String BAD_TEST_CHANNEL_ID = "test-bad-channel-id";
    private static final String BAD_TEST_CHANNEL_USERNAME = "test-bad-channel_user";

    @Autowired
    private YouTubeChannelsService service;

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelWithValidChannel_Then_Success() throws IOException {
        final Channel channel = service.getChannel(TEST_CHANNEL_ID);
        assertThat("channel id does not match", channel.getId(), equalTo(TEST_CHANNEL_ID));
        assertThat("channel custom URL does not match", channel.getCustomUrl(), equalTo("@" + TEST_CHANNEL_USERNAME));
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelsWithValidChannel_Then_Success() throws IOException {
        final List<Channel> channels = service.getChannels(Collections.singletonList(TEST_CHANNEL_ID));
        assertThat("no channels found", not(channels.isEmpty()));
        if (!channels.isEmpty()) {
            assertThat("channel id does not match", channels.get(0).getId(), equalTo(TEST_CHANNEL_ID));
            assertThat("channel custom URL does not match", channels.get(0).getCustomUrl(), equalTo("@" + TEST_CHANNEL_USERNAME));
        }
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelsWithEmptyList_Then_Success() throws IOException {
        final List<Channel> channels = service.getChannels(Collections.emptyList());
        assertThat("channels found", channels.isEmpty());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelForHandleWithValidHandle_Then_Success() throws IOException {
        final Channel channel = service.getChannelForHandle(TEST_CHANNEL_HANDLE);
        assertThat("channel id does not match", channel.getId(), equalTo(TEST_CHANNEL_ID));
        assertThat("channel custom URL does not match", channel.getCustomUrl(), equalTo("@" + TEST_CHANNEL_USERNAME));
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelForUsernameWithValidUsername_Then_Success() throws IOException {
        final Channel channel = service.getChannelForUsername(TEST_CHANNEL_USERNAME);
        assertThat("channel id does not match", channel.getId(), equalTo(TEST_CHANNEL_ID));
        assertThat("channel custom URL does not match", channel.getCustomUrl(), equalTo("@" + TEST_CHANNEL_USERNAME));
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelWithInvalidChannel_Then_ReturnNull() throws IOException {
        final Channel channel = service.getChannel(BAD_TEST_CHANNEL_ID);
        assertThat("channel should be null", channel, nullValue());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelForUserWithInvalidUser_Then_ReturnNull() throws IOException {
        final Channel channel = service.getChannelForUsername(BAD_TEST_CHANNEL_USERNAME);
        assertThat("channel should be null", channel, nullValue());
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelWithNull_Then_ThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getChannel(null),
                "Expected getChannel(null) to throw exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelsWithNull_Then_ThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getChannels(null),
                "Expected getChannels(null) to throw exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelForHandleWithNull_Then_ThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getChannelForHandle(null),
                "Expected getChannelForHandle(null) to throw exception");
    }

    @Test
    public void Given_UserIsAuthenticated_When_GetChannelForUsernameWithNull_Then_ThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getChannelForUsername(null),
                "Expected getChannelForUsername(null) to throw exception");
    }
}
