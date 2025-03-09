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

/**
 * Constants used in tests
 * <p>
 * The 'ID's are valid as of February 2024 but may need to be changed in the future.
 * </p><p>
 * The 'playlist' and 'video' related items could be updated as part of the test preparation
 * but that creates a chicken-and-egg problem since we would still need to use the YouTube
 * REST client to obtain current values.
 * </p>
 * <p>
 * See 'YouTubeContext' for how to provide authentication properties.
 * </p>
 */
public interface TestConstants {
    String TEST_HL = "en";

    // these should be fine since Anton's had this channel for years
    String TEST_CHANNEL_HANDLE = "whatdamath";
    String TEST_CHANNEL_USERNAME = "whatdamath";
    String TEST_CHANNEL_ID = "UCciQ8wFcVoIIMi-lfu8-cjQ";
    String ANTON_PETROV_CHANNEL_ID = "UCciQ8wFcVoIIMi-lfu8-cjQ";
    String MRBALLEN_CHANNEL_ID = "UCtPrkXdtCM5DACLufB9jbsA";

    // these *may* need to change over time.
    String TEST_PLAYLIST_ID = "PL9hNFus3sjE5fWOXSJRo4CEj7xnTaysOW";
    String TEST_PLAYLIST_ITEM_ID = "UEw5aE5GdXMzc2pFNWZXT1hTSlJvNENFajd4blRheXNPVy41NkI0NEY2RDEwNTU3Q0M2";
    String TEST_VIDEO_ID = "r56jAAychzY";

    String BAD_TEST_CHANNEL_ID = "test-bad-channel-id";
    String BAD_TEST_CHANNEL_HANDLE = "test-bad-channel_handle";
    String BAD_TEST_CHANNEL_USERNAME = "test-bad-channel_user";
    String BAD_TEST_HL = "bad";
    String BAD_TEST_PLAYLIST_ID = "test-bad-playlist-id";
    String BAD_TEST_PLAYLIST_ITEM_ID = "test-bad-playlist-item-id";
    String BAD_TEST_VIDEO_ID = "test-bad-video-id";

    // for testing failure handling of bad YouTube account details
    String BAD_API_KEY = "test-bad-api-key";
    String BAD_APPLICATION_NAME = "test-application-name";
}
