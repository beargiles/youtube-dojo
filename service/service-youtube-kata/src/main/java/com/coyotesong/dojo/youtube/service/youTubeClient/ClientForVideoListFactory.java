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

package com.coyotesong.dojo.youtube.service.youTubeClient;

import com.coyotesong.dojo.youtube.service.YouTubeApiCacheService;
import com.coyotesong.dojo.youtube.model.Tag;
import com.coyotesong.dojo.youtube.model.Video;
import com.coyotesong.dojo.youtube.model.WikipediaTopic;
import com.coyotesong.dojo.youtube.security.LogSanitizer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Video list client factory
 */
@Component
@SuppressWarnings("unused")
public class ClientForVideoListFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ClientForVideoListFactory.class);
    private static final YTUtils utils = new YTUtils();

    private final YouTube.Builder ytBuilder;
    @SuppressWarnings("unused")
    private final LogSanitizer sanitize;
    private final YouTubeApiCacheService cache;

    public ClientForVideoListFactory(@NotNull YouTube.Builder ytBuilder, @NotNull LogSanitizer sanitize, @NotNull YouTubeApiCacheService cache) {
        this.ytBuilder = ytBuilder;
        this.sanitize = sanitize;
        this.cache = cache;
    }

    @NotNull
    public Builder newBuilder() {
        return new Builder(ytBuilder, cache);
    }

    public static class Builder {
        private static final List<String> VIDEO_PARTS = Arrays.asList(
                Constants.Part.CONTENT_DETAILS.toString(),
                Constants.Part.ID.toString(),
                Constants.Part.PLAYER.toString(),
                Constants.Part.SNIPPET.toString(),
                Constants.Part.TOPIC_DETAILS.toString());

        private final YouTube.Builder ytBuilder;
        private final YouTubeApiCacheService cache;

        private List<String> ids = Collections.emptyList();
        private String hl;
        private String quotaUser;

        private Builder(@NotNull YouTube.Builder ytBuilder, @NotNull YouTubeApiCacheService cache) {
            this.ytBuilder = ytBuilder;
            this.cache = cache;
        }

        public Builder withId(@NotNull String id) {
            this.ids = Collections.singletonList(id);
            return this;
        }

        public Builder withIds(@NotNull List<String> ids) {
            this.ids = ids;
            return this;
        }

        public Builder withHl(@NotNull String hl) {
            this.hl = hl;
            return this;
        }

        public Builder withQuotaUser(@NotNull String quotaUser) {
            this.quotaUser = quotaUser;
            return this;
        }

        /**
         * Build new instance.
         *
         * @return
         * @throws IOException
         */
        @NotNull
        public YouTubeClient.ListVideos build() throws IOException {
            final YouTube.Videos.List request = ytBuilder.build().videos().list(VIDEO_PARTS);

            if (LOG.isTraceEnabled()) {
                LOG.trace("ids: '" + String.join(",", ids) + "'");
            }

            if (!ids.isEmpty()) {
                request.setId(ids);
            } else {
                throw new IllegalStateException("'id' must be specified");
            }

            if (isNotBlank(hl)) {
                request.setHl(hl);
            }

            if (isNotBlank(quotaUser)) {
                request.setQuotaUser(quotaUser);
            }

            final YouTubeClientState<Video, YouTube.Videos.List, VideoListResponse, com.google.api.services.youtube.model.Video> state =
                    new YouTubeClientState<>(request, ClientForVideoListFactory::convert, cache);

            return new YouTubeClient.ListVideos(state);
        }
    }

    /**
     * Convert YouTube API object to ours.
     *
     * @param video
     * @return
     */
    @NotNull
    public static Video convert(@NotNull com.google.api.services.youtube.model.Video video) {
        final Video value = new Video();

        value.setId(video.getId());
        value.setEtag(video.getEtag());
        value.setSummary(false);

        if (video.getPlayer() != null) {
            final VideoPlayer player = video.getPlayer();
            // full embedded HTML is
            // <iframe width="480" height="270" src="//www.youtube.com/embed/XYZ" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>
            final String s = player.getEmbedHtml();
            value.setEmbedHtml(s);
            if (StringUtils.isNotBlank(s)) {
                final Pattern pattern = Pattern.compile(".*src=\"([^\"]+)\".*");
                final Matcher m = pattern.matcher(s);
                if (m.matches()) {
                    value.setEmbedSrc("https:" + m.group(1));
                }
            }
            value.setEmbedHeight(player.getEmbedHeight());
            value.setEmbedWidth(player.getEmbedWidth());
        }

        if (video.getContentDetails() != null) {
            final VideoContentDetails details = video.getContentDetails();
            value.setCaption(details.getCaption());
            value.setDefinition(details.getDefinition());
            value.setDimension(details.getDimension());
            value.setDuration(details.getDuration());
            value.setHasCustomThumbnail(details.getHasCustomThumbnail());
            value.setLicensedContent(details.getLicensedContent());
            value.setProjection(details.getProjection());

            if (details.getContentRating() != null) {
                final ContentRating contentRating = details.getContentRating();
                value.setYtRating(contentRating.getYtRating());
                value.setMpaaRating(contentRating.getMpaaRating());
                // trailers, ads, DVDs
                value.setMpaatRating(contentRating.getMpaatRating());
                value.setTvpgRating(contentRating.getTvpgRating());
                try {
                    value.setContentRating(details.getContentRating().toPrettyString());
                    if ("{}".equals(value.getContentRating())) {
                        value.setContentRating(null);
                    }
                } catch (IOException e) {
                    // LOG.info("error encoding content rating for video {}", sanitize.forVideoId(video.getId()));
                }
            }
            if (details.getCountryRestriction() != null) {
                try {
                    value.setCountryRestriction(details.getCountryRestriction().toPrettyString());
                } catch (IOException e) {
                    // LOG.info("error encoding country restriction for video {}", sanitize.forVideoId(video.getId()));
                }
            }
            if (details.getRegionRestriction() != null) {
                try {
                    value.setRegionRestrictions(details.getRegionRestriction().toPrettyString());
                } catch (IOException e) {
                    // LOG.info("error encoding region restriction for video {}", sanitize.forVideoId(video.getId()));
                }
            }
        }

        if (video.getSnippet() != null) {
            final VideoSnippet snippet = video.getSnippet();
            value.setCategoryId(snippet.getCategoryId());
            value.setChannelId(snippet.getChannelId());
            value.setChannelTitle(snippet.getChannelTitle());
            // defaultAudioLanguage, defaultLanguage
            value.setDescription(snippet.getDescription());
            // liveBroadcastContent, localized
            value.setLang(snippet.getDefaultLanguage());
            if (snippet.getPublishedAt() != null) {
                value.setPublishedAt(Instant.ofEpochMilli(snippet.getPublishedAt().getValue()));
            }
            if ((snippet.getTags() != null) && !snippet.getTags().isEmpty()) {
                for (String tag : snippet.getTags()) {
                    value.getTags().add(new Tag(null, tag, tag));
                }
            }
            value.setTitle(snippet.getTitle());

            final ThumbnailDetails td = snippet.getThumbnails();
            if ((td != null) && !td.isEmpty()) {
                value.getThumbnails().putAll(utils.convertThumbnails(video.getId(), td));
            }
        }

        if (video.getStatistics() != null) {
            final VideoStatistics statistics = video.getStatistics();
            value.setDislikeCount(statistics.getDislikeCount());
            value.setFavoriteCount(statistics.getFavoriteCount());
            value.setLikeCount(statistics.getLikeCount());
            value.setViewCount(statistics.getViewCount());
        }

        if (video.getStatus() != null) {
            final VideoStatus status = video.getStatus();
            value.setEmbeddable(status.getEmbeddable());
            value.setLicense(status.getLicense());
            value.setMadeForKids(status.getMadeForKids());
            value.setPrivacyStatus(status.getPrivacyStatus());
            value.setPublicStatsViewable(status.getPublicStatsViewable());
            if (status.getPublishAt() != null) {
                value.setPublishedAt(Instant.ofEpochMilli(status.getPublishAt().getValue()));
            }
            value.setSelfDeclaredMadeForKids(status.getSelfDeclaredMadeForKids());
            value.setUploadStatus(status.getUploadStatus());
        }

        if (video.getRecordingDetails() != null) {
            final VideoRecordingDetails details = video.getRecordingDetails();
            details.getLocation();
            details.getLocationDescription();
            details.getRecordingDate();
            // value.setRecordingDetails = video.getRecordingDetails().toPrettyString();
        }

        if (video.getTopicDetails() != null) {
            final VideoTopicDetails details = video.getTopicDetails();
            for (String category : details.getTopicCategories()) {
                try {
                    value.getTopicCategories().add(new WikipediaTopic(category));
                } catch (MalformedURLException e) {
                    // @FIXME add log sanitization
                    LOG.warn("bad Wikipedia URL: {}", category);
                }
            }
            if (details.getTopicIds() != null) {
                value.getTopicIds().addAll(details.getTopicIds());
            }
            if (details.getRelevantTopicIds() != null) {
                value.getRelevantTopicIds().addAll(details.getRelevantTopicIds());
            }
        }

        value.setLastChecked(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        return value;
    }
}
