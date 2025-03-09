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

import java.io.Serial;
import java.io.Serializable;

/**
 * Join table...
 */
public class ChannelXFreebaseTopic implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer channelKey;
    private Integer topicKey;
    private String etag;

    public Integer getChannelKey() {
        return channelKey;
    }

    public void setChannelKey(Integer channelKey) {
        this.channelKey = channelKey;
    }

    public Integer getTopicKey() {
        return topicKey;
    }

    public void setTopicKey(Integer topicKey) {
        this.topicKey = topicKey;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ChannelXFreebaseTopic that = (ChannelXFreebaseTopic) o;

        return new EqualsBuilder().append(channelKey, that.channelKey).append(topicKey, that.topicKey).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(channelKey).append(topicKey).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("channelKey", channelKey)
                .append("topicKey", topicKey)
                .toString();
    }
}
