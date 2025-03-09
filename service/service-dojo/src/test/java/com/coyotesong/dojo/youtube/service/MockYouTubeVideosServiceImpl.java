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

import com.coyotesong.dojo.youtube.model.Video;
import com.coyotesong.dojo.youtube.testdata.TestVideos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.boot.test.context.TestComponent;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Mock implementation of YouTubeVideosService
 */
@TestComponent
public class MockYouTubeVideosServiceImpl implements YouTubeVideosService {
    @Override
    public @Nullable Video getVideo(@NotNull String id) throws IOException {
        if (TestVideos.VIDEO.getVideoId().equals(id)) {
            return TestVideos.VIDEO.getVideo();
        } else if (TestVideos.MISSING_VIDEO.getVideoId().equals(id)) {
            return TestVideos.MISSING_VIDEO.getVideo();
        }

        fail("unexpected videoId: " + id);
        return null;
    }

    @Override
    public @NotNull List<Video> getVideos(@NotNull @Unmodifiable List<String> ids) throws IOException {
        return Collections.emptyList();
    }
}
