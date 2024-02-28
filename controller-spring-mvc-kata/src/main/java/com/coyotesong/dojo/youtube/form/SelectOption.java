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

package com.coyotesong.dojo.youtube.form;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Available options for 'select' properties.
 */
@SuppressWarnings("unused")
public class SelectOption implements MessageSourceAware {
    private static final Logger LOG = LoggerFactory.getLogger(SelectOption.class);

    /**
     * Known uses... but I'm not sure if they should be defined here or in the controller.
     *
     * TODO: I don't think this approach will work with more than a single locale - but that's
     * not a problem since it will be replaced by a database-based one that will cache the information
     * for all locales.
     */
    public static final List<SelectOption> CHANNEL_TYPE_SELECT_LIST = loadSelectList("ChannelTypeSelectOptions");
    public static final List<SelectOption> EVENT_TYPE_SELECT_LIST = loadSelectList("EventTypeSelectOptions");
    public static final List<SelectOption> LANGUAGE_SELECT_LIST = loadSelectList("LangSelectOptions");
    public static final List<SelectOption> SORT_ORDER_SELECT_LIST = loadSelectList("OrderSelectOptions");
    public static final List<SelectOption> SAFE_SEARCH_SELECT_LIST = loadSelectList("SafeSearchSelectOptions");

    private MessageSource messageSource;

    @NotNull
    private String value;
    @NotNull
    private String label;
    private boolean defaultValue;

    public SelectOption(@NotNull String value, @NotNull String label) {
        this(value, label, false);
    }

    public SelectOption(@NotNull String value, @NotNull String label, boolean defaultValue) {
        this.value = value;
        this.label = label;
        this.defaultValue = defaultValue;
    }

    @NotNull
    public String getValue() {
        return value;
    }

    public void setValue(@NotNull String value) {
        this.value = value;
    }

    @NotNull
    public String getLabel() {
        return label;
    }

    public void setLabel(@NotNull String label) {
        this.label = label;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setMessageSource(@NotNull MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Load select options from resource bundle
     *
     * @param name
     * @return
     */
    @NotNull
    private static List<SelectOption> loadSelectList(@NotNull String name) {
        final ResourceBundle bundle = ResourceBundle.getBundle(SelectOption.class.getPackageName() + "." + name);

        // TODO: add check for 'defaultValue'
        final List<SelectOption> options = new ArrayList<>();
        options.add(new SelectOption("", "--"));
        for (String key : Collections.list(bundle.getKeys())) {
            // alternative: options.add(messageSource.getMessage("msg.txt", lang, null));
            options.add(new SelectOption(key, bundle.getString(key)));
        }
        LOG.debug("loadSelectOptions('{}') -> {} item(s)", name, options.size());

        return options;
    }

    @Override
    public String toString() {
        // TODO: should this be sanitized in some way?
        return value + ": '" + label + "'";
    }
}
