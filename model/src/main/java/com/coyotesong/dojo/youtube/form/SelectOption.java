/*
 * Copyright (c) 2023 Bear Giles <bgiles@coyotesong.com>.
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

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Available options for 'select' properties.
 */
public class SelectOption implements MessageSourceAware {
    public static final List<SelectOption> CHANNEL_TYPE_SELECT_OPTIONS = loadSelectOptions("channelTypeOptions");
    public static final List<SelectOption> EVENT_TYPE_SELECT_OPTIONS = loadSelectOptions("eventTypeOptions");
    public static final List<SelectOption> LANGUAGE_SELECT_OPTIONS = loadSelectOptions("langOptions");
    public static final List<SelectOption> ORDER_SELECT_OPTIONS = loadSelectOptions("orderOptions");
    public static final List<SelectOption> SAFE_SEARCH_SELECT_OPTIONS = loadSelectOptions("safeSearchOptions");

    private MessageSource messageSource;

    private String value;
    private String label;
    private boolean defaultValue;

    public SelectOption() {

    }

    public SelectOption(String value) {
        this(value, value, false);
    }

    public SelectOption(String value, String label) {
        this(value, label, false);
    }

    public SelectOption(String value, String label, boolean defaultValue) {
        this.value = value;
        this.label = label;
        this.defaultValue = defaultValue;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Load select options from resource bundle
     *
     * @param name
     * @return
     */
    private static List<SelectOption> loadSelectOptions(String name) {
        final ResourceBundle bundle = ResourceBundle.getBundle(SelectOption.class.getPackageName() + "." + name);

        // TODO: add check for 'defaultValue'
        final List<SelectOption> options = new ArrayList<>();
        options.add(new SelectOption("", "--"));
        for (String key : Collections.list(bundle.getKeys())) {
            // alternative: options.add(messageSource.getMessage("msg.txt", lang, null));
            options.add(new SelectOption(key, bundle.getString(key)));
        }

        return options;
    }
}
