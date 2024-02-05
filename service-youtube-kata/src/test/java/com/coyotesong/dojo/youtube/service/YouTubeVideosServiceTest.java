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
// import static org.junit.jupiter.api.Assertions.assertThrows;

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
public class YouTubeVideosServiceTest {
    private static final String TEST_CHANNEL_ID = "UCciQ8wFcVoIIMi-lfu8-cjQ";
    private static final String TEST_CHANNEL_USERNAME = "whatdamath";
    private static final String BAD_TEST_CHANNEL_ID = "test-bad-channel-id";
    private static final String BAD_TEST_CHANNEL_USERNAME = "test-bad-channel_user";

    @Autowired
    private YouTubeChannelsService service;

    @Test
    public void givenChannelId_whenGetChannel_thenGetChannel() throws IOException {
        final Channel channel = service.getChannel(TEST_CHANNEL_ID);
        assertThat("channel id does not match", channel.getId(), equalTo(TEST_CHANNEL_ID));
        assertThat("channel custom URL does not match", channel.getCustomUrl(), equalTo("@" + TEST_CHANNEL_USERNAME));
    }

    @Test
    public void givenChannelId_whenGetChannels_thenGetChannel() throws IOException {
        final List<Channel> channels = service.getChannels(Collections.singletonList(TEST_CHANNEL_ID));
        assertThat("no channels found", not(channels.isEmpty()));
        assertThat("channel id does not match", channels.get(0).getId(), equalTo(TEST_CHANNEL_ID));
        assertThat("channel custom URL does not match", channels.get(0).getCustomUrl(), equalTo("@" + TEST_CHANNEL_USERNAME));
    }

    @Test
    public void givenUsername_whenGetChannelForUser_thenGetChannel() throws IOException {
        final Channel channel = service.getChannelForUser(TEST_CHANNEL_USERNAME);
        assertThat("channel id does not match", channel.getId(), equalTo(TEST_CHANNEL_ID));
        assertThat("channel custom URL does not match", channel.getCustomUrl(), equalTo("@" + TEST_CHANNEL_USERNAME));
    }

    @Test
    public void givenBadChannelId_whenGetChannel_thenFailure() throws IOException {
        final Channel channel = service.getChannel(BAD_TEST_CHANNEL_ID);
        assertThat("channel should be null", channel, nullValue());

        // final IOException thrown = assertThrows(
        //         IOException.class,
        //         () -> service.getChannel(BAD_TEST_CHANNEL_ID),
        //         "Expected getChannel('bad') to throw exception");
    }

    @Test
    public void givenBadUsername_whenGetChannelForUser_thenFailure() throws IOException {
        final Channel channel = service.getChannelForUser(BAD_TEST_CHANNEL_USERNAME);
        assertThat("channel should be null", channel, nullValue());

        // final IOException thrown = assertThrows(
        //         IOException.class,
        //        () -> service.getChannelForUser(BAD_TEST_CHANNEL_USERNAME),
        //         "Expected getChannelForUser('bad') to throw exception");
    }
}
