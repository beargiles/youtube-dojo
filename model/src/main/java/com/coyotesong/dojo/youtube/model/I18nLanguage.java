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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.io.Serializable;

/**
 * YouTube I18nLanguage
 * see <a href="https://googleapis.dev/java/google-api-services-youtube/latest/com/google/api/services/youtube/model/I18nLanguage.html">I18nLanguage</a>
 */
@SuppressWarnings("unused")
public class I18nLanguage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String etag;
    private String parentEtag;
    private String hl;
    private String name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getParentEtag() {
        return parentEtag;
    }

    public void setParentEtag(String parentEtag) {
        this.parentEtag = parentEtag;
    }

    public String getHl() {
        return hl;
    }

    public void setHl(String hl) {
        this.hl = hl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final I18nLanguage that = (I18nLanguage) o;

        return new EqualsBuilder()
                .append(etag, that.etag)
                .append(hl, that.hl)
                .append(name, that.name)
                .append(parentEtag, that.parentEtag)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(etag)
                .append(hl)
                .append(name)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("etag", etag)
                .append("hl", hl)
                .append("name", name)
                .append("parentEtag", parentEtag)
                .toString();
    }
}
