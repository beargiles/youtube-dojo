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

/**
 * Services using the YouTube API.
 * <p>
 * The YouTube API offers two authentication methods. The first,
 * used here, requires a 'Developer API key'. This is sufficient
 * for broad read-only access by desktop applications.
 * </p><p>
 * The second approach is using OAuth authentication. This allows
 * the software to act on behalf of any user but requires the
 * application to have an online presence and be registered with
 * YouTube. I'm skipping this for now.
 * </p>
 * <p>
 * Implemented and often idempotent API calls
 * <ul>
 *    <li>Channels [list]</li>
 *    <li>Playlist Images [list]</li>
 *    <li>Playlist Items [list]</li>
 *    <li>Playlists [list]</li>
 *    <li>Video Categories [list]</li>
 *    <li>Videos [list]</li>
 * </ul>
 * </p>
 * <p>
 * Implemented but not idempotent API calls
 * <ul>
 *   <li>Search [list]</li>
 * </ul>
 * </p>
 * <p>
 * Unimplemented but potentially useful API calls
 * <ul>
 *   <li>Captions [list]</li>
 *   <li>Channel Sections [list]</li>
 *   <li>Comments [list]</li>
 *   <li>Comment Threads [list]</li>
 *   <li>Subscriptions [list]</li>
 * </ul>
 * </p>
 */
package com.coyotesong.dojo.youtube.service;