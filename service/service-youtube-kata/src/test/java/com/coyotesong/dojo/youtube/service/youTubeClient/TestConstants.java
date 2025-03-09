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

package com.coyotesong.dojo.youtube.service.youTubeClient;

import java.time.Instant;

/**
 * Constants used in tests
 */
public interface TestConstants {
    Boolean TEST_ASSIGNABLE = Boolean.TRUE;
    String TEST_CHANNEL_ID = "test-channel-id";
    String TEST_CHANNEL_TITLE = "test-channel-title";
    String TEST_DEFAULT_LANGUAGE = "en";
    String TEST_DESCRIPTION = "test-description";
    String TEST_EMBED_HTML = "http://test.embed/index.html";
    String TEST_ETAG = "test-etag";
    String TEST_GL = "test-gl";
    String TEST_HL = "en";
    String TEST_LOCALE = "en";
    String TEST_ID = "test-id";
    String TEST_LANGUAGE_NAME = "English";
    String TEST_PARENT_ETAG = "test-parent-etag";
    String TEST_PLAYLIST_ID = "test-playlist-id";
    int TEST_PLAYLIST_IMAGE_HEIGHT = 400;
    String TEST_PLAYLIST_IMAGE_TYPE = "test-playlist-image-type";
    int TEST_PLAYLIST_IMAGE_WIDTH = 600;
    Instant TEST_PUBLISHED_AT = null;
    String TEST_THUMBNAIL_URL = "http://test/thumbnail.jpg";
    String TEST_THUMBNAIL_VIDEO_ID = "test-thumbnail-video-id";
    String TEST_TITLE = "test-title";
}
