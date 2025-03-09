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

package com.coyotesong.dojo.youtube.model;

import com.coyotesong.dojo.youtube.lang3.MyToStringStyle;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * The YouTube 'topic category' field contains a wikipedia link.
 * <p>
 * There is a many-to-many relationship between this table and (channels, videos).
 * <p>
 * This class maintains both URL and a simplified label.
 */
@SuppressWarnings("unused")
public class WikipediaTopic implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer key;
    private URL url;
    private String label;
    private boolean custom;

    /**
     * Default constructor
     */
    public WikipediaTopic() {

    }

    /**
     * Copy constructor
     */
    public WikipediaTopic(String url) throws MalformedURLException {
        this.url = URI.create(url).toURL();
        this.label = url.substring(url.lastIndexOf("/") + 1).replace("_", " ");
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final WikipediaTopic that = (WikipediaTopic) o;

        return new EqualsBuilder()
                // DO NOT INCLUDE KEY!
                .append(url, that.url)
                .append(label, that.label)
                .append(custom, that.custom)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(url).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, MyToStringStyle.DEFAULT_STYLE)
                .append("key", key)
                .append("url", url)
                .append("label", label)
                .append("custom", custom)
                .toString();
    }
}
