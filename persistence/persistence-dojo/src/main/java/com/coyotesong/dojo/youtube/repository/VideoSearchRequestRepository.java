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

import com.coyotesong.dojo.youtube.form.VideoSearchForm;

import java.util.Collection;

/**
 * Persistence mechanism for search requests
 * <p>
 * At the moment SearchRequest and YouTubeSearchForm are synonymous but this
 * gives us some flexibility. E.g., we should move the 'uuid' out of the
 * search form and use a more UI-friendly id for that class..
 * </p>
 */
public interface VideoSearchRequestRepository {
    void delete();

    void insert(VideoSearchForm form);

    void insert(Collection<VideoSearchForm> form);
}
