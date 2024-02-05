package com.coyotesong.dojo.youtube.service;

import com.coyotesong.dojo.youtube.config.YouTubeContext;
import com.coyotesong.dojo.youtube.model.Video;
import com.coyotesong.dojo.youtube.security.LogSanitizerImpl;
import com.coyotesong.dojo.youtube.service.youtubeClient.ClientForVideoListFactory;
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
        ClientForVideoListFactory.class,
        YouTubeVideosServiceImpl.class
})
public class YouTubeSubscriptionsServiceTest {
    private static final String TEST_VIDEO_ID = "kBgABrIs45o";
    private static final String BAD_TEST_VIDEO_ID = "test-bad-video-id";

    @Autowired
    private YouTubeVideosService service;

    @Test
    public void givenVideoId_whenGetVideo_thenGetVideo() throws IOException {
        final Video video = service.getVideo(TEST_VIDEO_ID);
        assertThat("video id does not match", video.getId(), equalTo(TEST_VIDEO_ID));
    }

    @Test
    public void givenVideoId_whenGetVideos_thenGetVideo() throws IOException {
        final List<Video> videos = service.getVideos(Collections.singletonList(TEST_VIDEO_ID));
        assertThat("no videos found", not(videos.isEmpty()));
        assertThat("video id does not match", videos.get(0).getId(), equalTo(TEST_VIDEO_ID));
    }

    @Test
    public void givenBadVideoId_whenGetVideo_thenFailure() throws IOException {
        final Video video = service.getVideo(BAD_TEST_VIDEO_ID);
        assertThat("video should be null", video, nullValue());

        // final IOException thrown = assertThrows(
        //         IOException.class,
        //         () -> service.getVideo(BAD_TEST_VIDEO_ID),
        //         "Expected getVideo('bad') to throw exception");
    }
}
