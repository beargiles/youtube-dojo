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

package com.coyotesong.dojo.youtube.model.extensions.tabsoutliner;

import java.io.Serial;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Map;

/**
 * Single entry from 'Tabs Outliner' extension 'tree'
 */
public class TreeEntry implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String type;
    private Boolean active;
    private Boolean audible;
    private Boolean autoDiscardable;
    private Boolean discarded;
    private String favIconUrl;
    private Integer groupId;
    private Boolean highlighted;
    private Long id;
    private Long openerTabId;
    private String pendingUrl;
    private String title;
    private String url;
    private Long windowId;
    private String host;
    private String path;
    private String query;
    private String category;

    public TreeEntry(String type, Map<String, Object> data) {
        this.type = type;
        if (data.containsKey("active")) {
            this.active = (Boolean) data.get("active");
        }
        if (data.containsKey("audible")) {
            this.audible = (Boolean) data.get("audible");
        }
        if (data.containsKey("autoDiscardable")) {
            this.autoDiscardable = (Boolean) data.get("autoDiscardable");
        }
        if (data.containsKey("discarded")) {
            this.discarded = (Boolean) data.get("discarded");
        }
        if (data.containsKey("favIconUrl")) {
            this.favIconUrl = (String) data.get("favIconUrl");
        }
        if (data.containsKey("groupId")) {
            this.groupId = ((Long) data.get("groupId")).intValue();
        }
        if (data.containsKey("highlighted")) {
            this.highlighted = (Boolean) data.get("highlighted");
        }
        if (data.containsKey("id")) {
            this.id = (Long) data.get("id");
        }
        if (data.containsKey("openerTabId")) {
            this.openerTabId = (Long) data.get("openerTabId");
        }
        if (data.containsKey("pendingUrl")) {
            this.pendingUrl = (String) data.get("pendingUrl");
        }
        if (data.containsKey("title")) {
            this.title = (String) data.get("title");
        }
        if (data.containsKey("url")) {
            this.url = (String) data.get("url");
            try {
                final URL url = URI.create(this.url).toURL();
                this.host = url.getHost();
                this.path = url.getPath();
                this.query = url.getQuery();
            } catch (IllegalArgumentException | MalformedURLException e) {
                //
            }
        }
        if (data.containsKey("windowId")) {
            this.windowId = (Long) data.get("windowId");
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getAudible() {
        return audible;
    }

    public void setAudible(Boolean audible) {
        this.audible = audible;
    }

    public Boolean getAutoDiscardable() {
        return autoDiscardable;
    }

    public void setAutoDiscardable(Boolean autoDiscardable) {
        this.autoDiscardable = autoDiscardable;
    }

    public Boolean getDiscarded() {
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }

    public String getFavIconUrl() {
        return favIconUrl;
    }

    public void setFavIconUrl(String favIconUrl) {
        this.favIconUrl = favIconUrl;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Boolean getHighlighted() {
        return highlighted;
    }

    public void setHighlighted(Boolean highlighted) {
        this.highlighted = highlighted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOpenerTabId() {
        return openerTabId;
    }

    public void setOpenerTabId(Long openerTabId) {
        this.openerTabId = openerTabId;
    }

    public String getPendingUrl() {
        return pendingUrl;
    }

    public void setPendingUrl(String pendingUrl) {
        this.pendingUrl = pendingUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getWindowId() {
        return windowId;
    }

    public void setWindowId(Long windowId) {
        this.windowId = windowId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
