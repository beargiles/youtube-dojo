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

package com.coyotesong.dojo.youtube.controller;

import com.coyotesong.dojo.youtube.form.SelectOption;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.coyotesong.dojo.youtube.controller.Constants.SELECT_LIST_GET_LANGUAGE_VALUES_PATH;
import static com.coyotesong.dojo.youtube.controller.Constants.SELECT_LIST_GET_SAFE_SEARCH_VALUES_PATH;
import static com.coyotesong.dojo.youtube.controller.Constants.SELECT_LIST_GET_SORT_ORDER_VALUES_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller that provides the select lists
 * <p>
 * TODO: these methods should be aware of the caller's locale
 * </p>
 */
@RestController
public class SelectListRestController {
    // @RequestMapping(
    //        path = SELECT_LIST_GET_LANGUAGE_VALUES_PATH,
    //        produces = APPLICATION_JSON_VALUE
    //)
    @GetMapping(value = SELECT_LIST_GET_LANGUAGE_VALUES_PATH, produces = APPLICATION_JSON_VALUE)
    public Map<String, List<SelectOption>> getLanguageSelectList() {
        final Map<String, List<SelectOption>> results = new LinkedHashMap<>();
        results.put("languageValues", SelectOption.LANGUAGE_SELECT_LIST);
        return results;
    }

    @GetMapping(value = SELECT_LIST_GET_SORT_ORDER_VALUES_PATH, produces = APPLICATION_JSON_VALUE)
    public Map<String, List<SelectOption>> getSortOrderSelectList() {
        final Map<String, List<SelectOption>> results = new LinkedHashMap<>();
        results.put("sortOrderValues", SelectOption.LANGUAGE_SELECT_LIST);
        return results;
    }

    @GetMapping(value = SELECT_LIST_GET_SAFE_SEARCH_VALUES_PATH, produces = APPLICATION_JSON_VALUE)
    public Map<String, List<SelectOption>> getSafeSearchSelectList() {
        final Map<String, List<SelectOption>> results = new LinkedHashMap<>();
        results.put("safeSearchValues", SelectOption.LANGUAGE_SELECT_LIST);
        return results;
    }
}
