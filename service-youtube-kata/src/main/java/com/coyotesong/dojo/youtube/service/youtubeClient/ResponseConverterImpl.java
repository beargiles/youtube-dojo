package com.coyotesong.dojo.youtube.service;

import com.coyotesong.dojo.youtube.model.Channel;
import com.coyotesong.dojo.youtube.model.Playlist;
import com.coyotesong.dojo.youtube.model.WikipediaTopic;
import com.google.api.services.youtube.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ResponseConverterImpl implements ResponseConverter {

    @Override
    public Channel convertChannel(com.google.api.services.youtube.model.Channel channel) {
        final Channel value = new Channel();
        value.setId(channel.getId());
        value.setEtag(channel.getEtag());

        if (channel.getContentOwnerDetails() != null) {
            value.setContentOwner(channel.getContentOwnerDetails().getContentOwner());
            // skip 'timeLinked'
        }

        // skip localizations
        if (channel.getSnippet() != null) {
            final ChannelSnippet snippet = channel.getSnippet();
            value.setCountry(snippet.getCountry());
            value.setCustomUrl(snippet.getCustomUrl());
            value.setLang(snippet.getDefaultLanguage());
            value.setDescription(snippet.getDescription());
            if (snippet.getPublishedAt() != null) {
                value.setPublishedAt(Instant.ofEpochMilli(snippet.getPublishedAt().getValue()));
            }
            value.setTitle(snippet.getTitle());
            final ThumbnailDetails td = snippet.getThumbnails();
            if ((td != null) && !td.isEmpty() && !td.getDefault().isEmpty()) {
                value.setTnUrl(td.getDefault().getUrl());
            }
        }

        if (channel.getContentDetails() != null) {
            final ChannelContentDetails details = channel.getContentDetails();
            value.setUploads(details.getRelatedPlaylists().getUploads());
            // favorites = details.getRelatedPlaylists().getFavorites();
            // likes = details.getRelatedPlaylists().getLikes();
            // watchHistory = details.getRelatedPlaylists().getWatchHistory();
            // watchLater = details.getRelatedPlaylists().getWatchLater();
        }

        if (channel.getTopicDetails() != null) {
            final ChannelTopicDetails details = channel.getTopicDetails();
            if (details.getTopicCategories() != null) {
                for (String category : details.getTopicCategories()) {
                    topicCategories.add(new WikipediaTopic(category));
                }
            }
            if (details.getTopicIds() != null) {
                channel.getTopicIds().addAll(details.getTopicIds());
            }
        }

        // channel.setLastChecked(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        // statistics
        //    comments, hidden subscriber count, video count, view count
        // status
        //    isLinked, longUploadStatus, madeForKids, privacyStatus, selfDeclaredMadeForKids

        return channel;
    }


    @Override
    public Playlist convertPlaylist(com.google.api.services.youtube.model.Playlist playlist) {
        final Playlist value = new Playlist();
        value.setId(playlist.getId());
        value.setEtag(playlist.getEtag());

        final PlaylistSnippet snippet = playlist.getSnippet();
        if (snippet != null) {
            if (snippet.getPublishedAt() != null) {
                value.setPublishedAt(Instant.ofEpochMilli(snippet.getPublishedAt().getValue()));
            }

            value.setChannelId(snippet.getChannelId());
            value.setTitle(snippet.getTitle());
            value.setDescription(snippet.getDescription());
            value.setChannelTitle(snippet.getChannelTitle());
            value.setLang(snippet.getDefaultLanguage());

            value.setTnVideoId(snippet.getThumbnailVideoId());

            if (snippet.getTags() != null) {
                value.setTags("\"" + String.join("\", \"", snippet.getTags()) + "\"");
            }

            final ThumbnailDetails td = snippet.getThumbnails();
            if ((td != null) && !td.isEmpty() && !td.getDefault().isEmpty()) {
                value.setTnUrl(td.getDefault().getUrl());
            }
        }

        final PlaylistPlayer player = playlist.getPlayer();
        if (player != null) {
            value.setEmbedHtml(player.getEmbedHtml());
        }

        return value;
    }


    public Video(com.google.api.services.youtube.model.Video video) {
        this.id = video.getId();
        this.etag = video.getEtag();
        if (video.getPlayer() != null) {
            final VideoPlayer player = video.getPlayer();
            // full embedded HTML is
            // <iframe width="480" height="270" src="//www.youtube.com/embed/XYZ" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>
            final String s = player.getEmbedHtml();
            this.embedHtml = s;
            if (StringUtils.isNotBlank(s)) {
                final Pattern pattern = Pattern.compile(".*src=\"([^\"]+)\".*");
                final Matcher m = pattern.matcher(s);
                if (m.matches()) {
                    this.embedSrc = "https:" + m.group(1);
                }
            }
            this.embedHeight = player.getEmbedHeight();
            this.embedWidth = player.getEmbedWidth();
        }

        if (video.getContentDetails() != null) {
            final VideoContentDetails details = video.getContentDetails();
            this.caption = details.getCaption();
            this.definition = details.getDefinition();
            this.dimension = details.getDimension();
            this.duration = details.getDuration();
            this.hasCustomThumbnail = details.getHasCustomThumbnail();
            this.licensedContent = details.getLicensedContent();
            this.projection = details.getProjection();

            if (details.getContentRating() != null){
                ContentRating contentRating = details.getContentRating();
                this.ytRating = contentRating.getYtRating();
                this.mpaaRating = contentRating.getMpaaRating();
                // trailers, ads, DVDs
                this.mpaatRating = contentRating.getMpaatRating();
                this.tvpgRating = contentRating.getTvpgRating();
                try {
                    this.contentRating = details.getContentRating().toPrettyString();
                    if ("{}".equals(this.contentRating)) {
                        this.contentRating = null;
                    }
                } catch (IOException e) {
                    LOG.info("error encoding content rating for video {}", id);
                }
            }
            if (details.getCountryRestriction() != null) {
                try {
                    this.countryRestriction = details.getCountryRestriction().toPrettyString();
                } catch (IOException e) {
                    LOG.info("error encoding country restriction for video {}", id);
                }
            }
            if (details.getRegionRestriction() != null) {
                try {
                    this.regionRestrictions = details.getRegionRestriction().toPrettyString();
                } catch (IOException e) {
                    LOG.info("error encoding region restriction for video {}", id);
                }
            }
        }

        if (video.getSnippet() != null) {
            final VideoSnippet snippet = video.getSnippet();
            this.categoryId = snippet.getCategoryId();
            this.channelId = snippet.getChannelId();
            this.channelTitle = snippet.getChannelTitle();
            // defaultAudioLanguage, defaultLanguage
            this.description = snippet.getDescription();
            // liveBroadcastContent, localized
            this.lang = snippet.getDefaultLanguage();
            if (snippet.getPublishedAt() != null) {
                this.publishedAt = Instant.ofEpochMilli(snippet.getPublishedAt().getValue());
            }
            /*
            if ((snippet.getTags() != null) && !snippet.getTags().isEmpty()) {
                for (String tag : snippet.getTags()) {
                    this.tags.add(new Tag(id, tag, tag));
                }
            }
             */
            this.title = snippet.getTitle();

            final ThumbnailDetails td = snippet.getThumbnails();
            if ((td != null) && !td.isEmpty()) {
                this.thumbnails.putAll(utils.convertThumbnails(id, td));
            }
        }

        if (video.getStatistics() != null) {
            final VideoStatistics statistics = video.getStatistics();
            this.dislikeCount = statistics.getDislikeCount();
            this.favoriteCount = statistics.getFavoriteCount();
            this.likeCount = statistics.getLikeCount();
            this.viewCount = statistics.getViewCount();
        }

        if (video.getStatus() != null) {
            final VideoStatus status = video.getStatus();
            this.embeddable = status.getEmbeddable();
            this.license = status.getLicense();
            // this.madeForKids = status.getMadeForKids();
            this.privacyStatus = status.getPrivacyStatus();
            this.publicStatsViewable = status.getPublicStatsViewable();
            if (status.getPublishAt() != null) {
                this.publishedAt = Instant.ofEpochMilli(status.getPublishAt().getValue());
            }
            // this.selfDeclaredMadeForKids = status.getSelfDeclaredMadeForKids();
            this.uploadStatus = status.getUploadStatus();
        }

        if (video.getRecordingDetails() != null) {
            final VideoRecordingDetails details = video.getRecordingDetails();
            details.getLocation();
            details.getLocationDescription();
            details.getRecordingDate();
            // this.recordingDetails = video.getRecordingDetails().toPrettyString();
        }

        if (video.getTopicDetails() != null) {
            final VideoTopicDetails details = video.getTopicDetails();
            if (details.getTopicCategories() != null) {
                for (String category : details.getTopicCategories()) {
                    final int ridx = category.lastIndexOf("/");
                    final String shortCategory = category.substring(ridx + 1).replace("_", " ");
                    LOG.info("category: {} -> {}", category, shortCategory);
                    topicCategories.add(shortCategory);
                }
            }
            if (details.getTopicIds() != null) {
                topicIds.addAll(details.getTopicIds());
            }
            if (details.getRelevantTopicIds() != null) {
                relevantTopicIds.addAll(details.getRelevantTopicIds());
            }
        }

        // there doesn't seem to be anything here... at least nothing standard
        /*
        if (video.getProjectDetails() != null) {
            VideoProjectDetails details = video.getProjectDetails();
            try {
                this.projectDetails = video.getProjectDetails().toPrettyString());
            } catch (IOException e) {
                //
            }
        }
         */
    }
}