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

package com.coyotesong.dojo.youtube.repository.jooq;

import com.coyotesong.dojo.youtube.form.VideoSearchForm;
import com.coyotesong.dojo.youtube.repository.VideoSearchRequestRepository;
import com.coyotesong.dojo.youtube.repository.jooq.generated.tables.records.*;
import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.coyotesong.dojo.youtube.repository.jooq.generated.tables.SearchRequest.SEARCH_REQUEST;

/**
 * Implementation of SearchRequestRepository
 */
@Repository
public class VideoSearchRequestRepositoryJooq extends DAOImpl<SearchRequestRecord, VideoSearchForm, String> implements VideoSearchRequestRepository {

    @Autowired
    public VideoSearchRequestRepositoryJooq(Configuration configuration) {
        super(SEARCH_REQUEST, VideoSearchForm.class, configuration);
    }

    @Override
    public String getId(VideoSearchForm form) {
        return form.getId();
    }

    @Override
    public void delete() {
        ctx().dsl().delete(SEARCH_REQUEST).execute();
        // super.delete();
    }
}