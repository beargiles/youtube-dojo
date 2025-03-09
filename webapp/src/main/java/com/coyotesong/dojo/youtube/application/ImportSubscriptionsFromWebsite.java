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

package com.coyotesong.dojo.youtube.application;

import com.coyotesong.dojo.youtube.model.ChannelSummary;
import com.coyotesong.dojo.youtube.model.UserSubscription;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Import subscriptions from https://youtube.com/feed/channels
 *
 * <p>Note: this application reads a downloaded file - it does not directly access
 * the website.</p>
 */
public class ImportSubscriptionsFromWebsite {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Import subscriptions from cut-and-paste copy of 'https://youtube.com/feed/channels'
     * <p>
     * This approach captures far less information than the HTML-based approach but is not
     * limited to the first 100 entries. This is also largely irrelevant since we should soon
     * update the information with a REST call to get more information about each channel.
     * </p>
     * <p>
     * The file should contain entries with the form:
     * <ul>
     *     <li>blank line</li>
     *     <li>Username</li>
     *     <ol>
     *         <li>Canonical URL path (beyond 'https://youtube.com/') - starts with '@'</li>
     *         <li>•</li>
     *         <li>Number of subscribers</li>
     *     </ol>
     *     <li>Description</li>
     *     <li>blank line</li>
     *     <li>"Subscribed"</li>
     * </ul>
     * </p>
     */
    static class TextReader {

        public Map<String, ChannelSummary> load(final String filename) throws IOException {
            final Map<String, ChannelSummary> channels = new LinkedHashMap<>();

            final String buffer = Files.readString(new File(filename).toPath())
                    .replace("\r\n", "\n") // Windows
                    .replace("\r", "\n")   // macOS
                    .replace( "•", "\n")   // field separator
                    .trim();

            // break buffer into individual channels
            for (String channel : buffer.split("\nSubscribed")) {
                final ChannelSummary summary = new ChannelSummary();

                final String[] lines = channel.trim().split("\n", 4);
                summary.setUsername(lines[0]);
                summary.setBaseUrl("/" + lines[1]);
                summary.setSubscriberCount(lines[2]);
                if (lines.length > 3) {
                    summary.setDescription(lines[3]);
                }
                channels.put(summary.getBaseUrl(), summary);

                if (lines.length > 4) {
                    System.out.println(channel);
                    System.out.println();
                }
            }
            System.out.println();

            return channels;
        }
    }

    /**
     * Import subscriptions from saved copy of 'https://youtube.com/feed/channels'
     *
     * <p>
     * Use 'save as', not 'save source', and it the results may be limited to the
     * first 100 entries despite what's shown in the browser window.
     * </p>
     */

    static class HtmlReader {

        public Map<ChannelSummary, UserSubscription> load(final String filename) throws IOException {
            final Map<ChannelSummary, UserSubscription> subscriptions = new LinkedHashMap<>();

            final JsonNode channels = simplify(filename);

            try (Writer w = new FileWriter("/tmp/data1.json")) {
                MAPPER.writerWithDefaultPrettyPrinter().writeValue(w, channels);
            }

            channels.forEach(s -> addSubscription(subscriptions, s));

            return subscriptions;
        }

        /**
         * Simplify HTML response to just the JSON payload we care about.
         *
         * @param filename
         * @return
         */
        JsonNode simplify(String filename) throws IOException {
            final String var1 = "\"channelRenderer\":";
            final String var = "ytInitialData = ";
            final String contents = Files.readString(new File(filename).toPath());

            final int idx = contents.indexOf(var);
            if (idx < 0) {
                throw new IOException("Could not find expected value");
            }

            final int ridx = contents.indexOf("</script>", idx);
            if (ridx < 0) {
                throw new IOException("Could not find expected termination");
            }

            final JsonNode root = MAPPER.readValue(contents.substring(idx + var.length(), ridx - 1), JsonNode.class);
            final JsonNode x = root.path("contents")
                    .path("twoColumnBrowseResultsRenderer")
                    .path("tabs").get(0)
                    .path("tabRenderer")
                    .path("content");

            MAPPER.writerWithDefaultPrettyPrinter().writeValue(new File("/tmp/data1.json"), x);

            if (x.has("richGridRenderer")) {
                throw new IOException("unsupported content");
                // richSectionRenderer
                // content []
                // richItemRenderer
                // content
            }
            if (x.has("sectionListRenderer")) {
                final JsonNode channels = x
                        .path("sectionListRenderer")
                        .path("contents").get(0)
                        .path("itemSectionRenderer")
                        .path("contents").get(0)
                        .path("shelfRenderer")
                        .path("content")
                        .path("expandedShelfContentsRenderer")
                        .path("items");

            }

            root.fieldNames().forEachRemaining(System.out::println);
            final JsonNode channels = root.path("contents")
                    .path("twoColumnBrowseResultsRenderer")
                    .path("tabs").get(0)
                    .path("tabRenderer")
                    .path("content")
                    .path("sectionListRenderer")
                    .path("contents").get(0)
                    .path("itemSectionRenderer")
                    .path("contents").get(0)
                    .path("shelfRenderer")
                    .path("content")
                    .path("expandedShelfContentsRenderer")
                    .path("items");

            channels.forEach(s -> {

                // rename field. (there doesn't seem to be a 'rename' method.)
                ((ObjectNode) s).replace("channel", s.path("channelRenderer"));
                ((ObjectNode) s).remove("channelRenderer");

                final ObjectNode node = (ObjectNode) s.path("channel");
                final ObjectNode subscribeButton = (ObjectNode) node.path("subscribeButton").path("subscribeButtonRenderer");
                final ObjectNode channelSummary = MAPPER.createObjectNode();
                final ObjectNode userSubscription = MAPPER.createObjectNode();

                node.replace("channelSummary", channelSummary);
                node.replace("userSubscription", userSubscription);

                // copy channel summary info
                channelSummary.put("username", node.path("subscriberCountText").path("simpleText").textValue());
                channelSummary.put("canonicalBaseUrl", node.path("navigationEndpoint").path("browseEndpoint").path("canonicalBaseUrl").textValue());
                channelSummary.replace("title", node.path("title"));
                channelSummary.replace("description", node.path("descriptionSnippet"));

                channelSummary.replace("shortBylineText", node.path("shortBylineText"));
                channelSummary.path("shortBylineText").path("runs").forEach(run -> ((ObjectNode) run).remove("navigationEndpoint"));

                channelSummary.replace("longBylineText", node.path("longBylineText"));
                channelSummary.path("longBylineText").path("runs").forEach(run -> ((ObjectNode) run).remove("navigationEndpoint"));

                // subscribeButton.put("currentStateId",
                //       subscribeButton.path("notificationPreferenceButton").path("subscriptionNotificationToggleButtonRenderer").path("currentStateId").intValue());

                // this looks odd but it matches practice
                channelSummary.put("subscriberCount", node.path("videoCountText").path("simpleText").textValue());

                node.path("thumbnail").path("thumbnails").forEach(thumbnail -> {
                    switch (thumbnail.path("width").intValue()) {
                        case 88:
                            channelSummary.put("smallThumbnailUrl", thumbnail.path("url").textValue());
                            break;
                        case 176:
                            channelSummary.put("mediumThumbnailUrl", thumbnail.path("url").textValue());
                            break;
                    }
                });

                final JsonNode ownerBadges = node.path("ownerBadges");
                if ((ownerBadges != null) && !ownerBadges.isEmpty()) {
                    final ArrayNode badges = MAPPER.createArrayNode();
                    ownerBadges.forEach(badge -> {
                        badges.add(badge.path("metadataBadgeRenderer").path("style").textValue().replace("BADGE_STYLE_TYPE_", ""));
                    });
                    channelSummary.replace("badges", badges);
                }

                node.remove("descriptionSnippet");
                node.remove("longBylineText");
                node.remove("navigationEndpoint");
                node.remove("ownerBadges");
                node.remove("shortBylineText");
                node.remove("subscriberCountText");
                node.remove("subscriptionButton");
                node.remove("thumbnail");
                node.remove("title");
                node.remove("trackingParams");
                node.remove("videoCountText");

                // copy user subscription information
                userSubscription.put("subscribedEntityKey", subscribeButton.path("subscribedEntityKey").textValue());
                userSubscription.put("subscribed", subscribeButton.path("subscribed").booleanValue());
                userSubscription.put("enabled", subscribeButton.path("enabled").booleanValue());
                userSubscription.put("type", subscribeButton.path("type").textValue());
                userSubscription.put("showPreferences", subscribeButton.path("showPreferences").booleanValue());
                switch (subscribeButton.path("currentStateId").intValue()) {
                    case 0:
                        userSubscription.put("notifications", "OFF");
                        break;
                    case 2:
                        userSubscription.put("notifications", "ACTIVE");
                        break;
                    case 3:
                        userSubscription.put("notifications", "NONE");
                        break;
                    default:
                        userSubscription.put("notifications", "UNKNOWN");
                }

                node.replace("userSubscription", userSubscription);
                node.remove("subscribeButton");

                // TODO: should I verify we only have 3 elements left under 'node'?
            });

            return channels;
        }

        /**
         * Add single subscription
         *
         * @param subscriptions
         * @param node
         */
        public void addSubscription(Map<ChannelSummary, UserSubscription> subscriptions, JsonNode node) {
            final JsonNode channel = node.path("channel");

            final ChannelSummary summary = extractChannelSummary(channel.path("channelSummary"));
            final UserSubscription subscription = extractUserSubscription(channel.path("userSubscription"));

            subscriptions.put(summary, subscription);
        }

        /**
         * Extract channel summary from JSON
         *
         * @param node JsonNode containing the channelSummary
         * @return
         */
        ChannelSummary extractChannelSummary(JsonNode node) {
            final ChannelSummary summary = new ChannelSummary();

            summary.setUsername(node.path("username").textValue());
            summary.setBaseUrl(node.path("canonicalBaseUrl").textValue());
            summary.setTitle(node.path("title").path("simpleText").textValue());

            // not all channels include descriptions
            if (node.path("description").has("runs")) {
                summary.setDescription(node.path("description").path("runs").get(0).path("text").textValue());
            }

            if (node.has("shortBylineText")) {
                summary.setShortByline(node.path("shortBylineText").path("runs").get(0).path("text").textValue());
            }

            if (node.has("longBylineText")) {
                summary.setLongByline(node.path("longBylineText").path("runs").get(0).path("text").textValue());
            }

            if (node.has("subscriberCount")) {
                summary.setSubscriberCount(node.path("subscriberCount").textValue());
            }

            if (node.has("smallThumbnailUrl")) {
                summary.setTn88(node.path("smallThumbnailUrl").textValue());
            }

            if (node.has("mediumThumbnailUrl")) {
                summary.setTn176(node.path("mediumThumbnailUrl").textValue());
            }

            // owner badges...

            return summary;
        }

        /**
         * Extract user subscription from JSON
         *
         * @param node JsonNode containing the user subscription
         * @return
         */
        UserSubscription extractUserSubscription(JsonNode node) {
            final UserSubscription us = new UserSubscription();

            // us.setChannelId(channel.path("channelId").textValue());
            us.setEntityKey(node.path("subscribedEntityKey").textValue());
            us.setSubscribed(node.path("subscribed").booleanValue());
            us.setEnabled(node.path("enabled").booleanValue());
            us.setNotifications(node.path("notifications").textValue());
            // us.setShowPreferences(node.path("showPreferences").booleanValue());
            // us.setType(node.path("type").textValue());

            return us;
        }
    }

    public static void main(final String[] args) throws IOException {
        final ImportSubscriptionsFromWebsite.TextReader importer = new ImportSubscriptionsFromWebsite.TextReader();

        final Map<String, ChannelSummary> subscriptions = importer.load("/nfs/media/youtube/cache-config/x.txt");
        subscriptions.forEach((k, v) -> System.out.printf("%-30.30s  %-30.30s  %-60.60s\n", v.getUsername(), k, (v.getDescription() == null) ? "" : v.getDescription().replace("\n", " \\n ")));

        System.out.println();
        System.out.println("" + subscriptions.size());
        System.out.println();
        System.out.println();
        System.out.println();
        subscriptions.forEach((k, v) -> {
            System.out.printf(" - baseUrl: %s\n", k);
            System.out.printf("   username: %s\n", v.getUsername());
            // System.out.printf("   subscribers: %s\n", v.getSubscriberCount());
            if (v.getDescription() != null) {
                System.out.printf("   description: %-80.80s\n", v.getDescription().replace("\n", " \\n "));
            }
        });

        // final Map<ChannelSummary, UserSubscription> subscriptions = importer.load("/tmp/data.json");
        // final Map<ChannelSummary, UserSubscription> subscriptions = importer.load("/nfs/media/youtube/cache-config/subscriptions-2/(163) All subscriptions - YouTube.html");

        // final Map<ChannelSummary, UserSubscription> subscriptions = importer.load("/nfs/media/youtube/cache-config/subscriptions-2/(163) All subscriptions - YouTube.html");

        // final Map<ChannelSummary, UserSubscription> subscriptions = importer.load("/nfs/media/youtube/cache-config/(117) All subscriptions - YouTube.html");
        // subscriptions.keySet().stream().map(ChannelSummary::getTitle).forEach(System.out::println);
    }
}