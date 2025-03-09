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

import com.coyotesong.dojo.youtube.form.VideoSearchForm;
import com.coyotesong.dojo.youtube.model.*;
import com.coyotesong.dojo.youtube.repository.*;
import com.coyotesong.dojo.youtube.repository.poi.ChannelRepositoryPoi;
import com.coyotesong.dojo.youtube.repository.zip.ChannelRepositoryZip;
import com.coyotesong.dojo.youtube.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.*;
import java.nio.file.Files;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SpringBootApplication(scanBasePackages = {"com.coyotesong.dojo.youtube"})
@EntityScan({"com.coyotesong.dojo.youtube.model"})
// @PropertySource(    --> spring.main.web-environment=false
public class RetrieveSubscribedChannelDetails {
    private static final Logger LOG = LoggerFactory.getLogger(RetrieveSubscribedChannelDetails.class);
    private static final File TMPDIR = new File(System.getProperty("java.io.tmpdir"));

    private final YouTubeChannelsService channelsService;
    private final YouTubeChannelSectionsService channelSectionsService;
    private final YouTubePlaylistsService playlistsService;
    private final YouTubeVideosService videosService;
    private final YouTubeSearchService searchService;
    private final FilePersistenceService filePersistenceService;

    private final ChannelRepository channelRepository;
    private final VideoRepository videoRepository;
    private final VideoSearchRequestRepository videoSearchRequestRepository;
    private final VideoSearchResultRepository videoSearchResultRepository;
    private final YouTubeReport report;
    private final I18nService i18nService;

    private static final File CHANNELS_REPO_ZIP = new File(TMPDIR, "channels-1.zip");
    private static final File CHANNELS_REPO_POI = new File(TMPDIR, "channels-1.xlsx");
    private final ChannelRepository zipChannelRepository = new ChannelRepositoryZip(CHANNELS_REPO_ZIP);
    private final ChannelRepository poiChannelRepository = new ChannelRepositoryPoi(CHANNELS_REPO_POI);


    // wikipedia, etc...

    @Autowired
    public RetrieveSubscribedChannelDetails(
            final YouTubeChannelsService channelsService,
            final YouTubeChannelSectionsService channelSectionsService,
            final YouTubePlaylistsService playlistsService,
            final YouTubeVideosService videosService,
            final YouTubeSearchService searchService,
            final FilePersistenceService filePersistenceService,
            final ChannelRepository channelRepository,
            final VideoRepository videoRepository,
            final VideoSearchRequestRepository videoSearchRequestRepository,
            final VideoSearchResultRepository videoSearchResultRepository,
            final YouTubeReport report,
            final I18nService i18nService) {

        this.channelsService = channelsService;
        this.channelSectionsService = channelSectionsService;
        this.playlistsService = playlistsService;
        this.videosService = videosService;
        this.searchService = searchService;
        this.filePersistenceService = filePersistenceService;

        this.channelRepository = channelRepository;
        this.videoRepository = videoRepository;
        this.videoSearchRequestRepository = videoSearchRequestRepository;
        this.videoSearchResultRepository = videoSearchResultRepository;
        this.report = report;

        this.i18nService = i18nService;
    }

    /**
     * Internal record that represents contents of cut-and-pasted information.
     * We only need 'handle'.
     */
    record ChannelSummary(
            String title,
            String handle,
            String subscriberCount,
            String description) {

        ChannelSummary(final String[] lines) {
            this(lines[0], lines[1], lines[2], (lines.length == 3) ? null : lines[3]);
        }
    }

    /**
     * Load channel summary from file
     *
     * @param filename file containing copy-and-pasted content from 'https://youtube.com/feed/channels'
     * @return Map containing summary of all subscribed channels
     * @throws IOException
     */
    Map<String, ChannelSummary> load(final String filename) throws IOException {

        // read file, normalize line separators, replace field separator between handle and subscriber count
        final String entries = Files.readString(new File(filename).toPath())
                .replace("\r\n", "\n") // Windows
                .replace("\r", "\n")   // macOS
                .replace("•", "\n")    // field separator
                .trim();

        // parse each entry
        final Map<String, ChannelSummary> channels = new LinkedHashMap<>();
        for (String buffer : entries.split("\nSubscribed")) {
            final ChannelSummary summary = new ChannelSummary(buffer.trim().split("\n", 4));
            channels.put(summary.handle(), summary);
        }

        return channels;
    }

    /**
     * Retrieve channel details from YouTube REST API
     *
     * @param handles Channel handles
     * @return
     */
    public Map<String, Channel> getChannelDetails(final Collection<String> handles) throws IOException {
        final Map<String, Channel> channels = new LinkedHashMap<>();

        int counter = 0;
        for (String handle : handles) {
            try {
                final Channel channel = channelsService.getChannelForHandle(handle);
                System.out.printf("%3d  %s\n", counter++, handle);
                if ((channel != null) && isNotBlank(channel.getChannelId())) {
                    channel.setSections(channelSectionsService.getChannelSectionsForChannelId(channel.getChannelId()));
                    channel.setPlaylists(playlistsService.getPlaylistsForChannelId(channel.getChannelId()));

                    // 'playlist->playlistImage' requires OAuth

                    channels.put(handle, channel);
                }
            } catch (YouTubeClientException e) {
                LOG.warn("YouTube Client Exception at {} - returning what we have...", handle, e);
                break;
            }
        }

        return channels;
    }

    /**
     * Load channels
     * @param filenames
     * @return
     * @throws IOException
     */
    List<Channel> loadChannels(String... filenames) throws IOException {
        // load subscriptions from file
        // final Map<String, ChannelSummary> subscriptions = load(filename);

        final Properties subscriptions = new Properties();
        for (String filename : filenames) {
            try (Reader reader = new FileReader(filename)) {
                subscriptions.load(reader);
            } catch (IOException e) {
                LOG.info("{}: {}", e.getClass().getName(), e.getMessage(), e);
            }
        }

        // get current information from YouTube.
        final List<String> handles = new ArrayList<>(subscriptions.stringPropertyNames());
        for (int i = 0; i < handles.size(); i++) {
            if (handles.get(i).charAt(0) != '@') {
                final String handle = handles.get(i);
                final String newHandle = '@' + handle;
                handles.set(i, newHandle);
                subscriptions.put(newHandle, subscriptions.get(handle));
            }
        }

        // final Map<String, Channel> channels = getChannelDetails(handles.subList(0, 10));
        final Map<String, Channel> channels = getChannelDetails(handles);
        for (Channel channel : channels.values()) {
            if (subscriptions.containsKey(channel.getHandle())) {
                if ("nsfw".equals(subscriptions.get(channel.getHandle()))) {
                    channel.setNsfw(Boolean.TRUE);
                }
            }
        }

        return new ArrayList<>(channels.values());
    }

    void listBeans(ApplicationContext ctx) {
        System.out.println("Let's inspect the beans provided by Spring Boot:");
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }

    /**
     * Get start date
     * @return
     */
    Instant getStartDate(int months) {
        // 'start date' goes back two to three full months.
        final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.roll(Calendar.MONTH, -months);
        // cal.roll(Calendar.YEAR, -1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.toInstant().truncatedTo(ChronoUnit.DAYS);
    }

    List<Channel> findChannels() throws IOException {
        zipChannelRepository.delete();
        final List<Channel> channels = loadChannels(
                "/nfs/media/youtube/cache-config/channel-category.properties",
                "/nfs/media/youtube/cache-config/yt-a.properties",
                "/nfs/media/youtube/cache-config/travel.properties"

        );
        return channels;
    }

    Map<String, List<SearchResult>> performSearch(File root, List<Channel> channels) throws IOException {
        LOG.info("a");
        final Map<String, List<SearchResult>> value = new LinkedHashMap<>();
        LOG.info("b");
        for (Channel channel : channels) {
            LOG.info("c: {}", channel.getTitle());
            if (channel.getVideoCount() > 5000) {
                LOG.info("skipping due to excessive videos ({}): {}", channel.getVideoCount(), channel.getTitle());
            } else {
                final File file = new File(root, channel.getChannelId() + ".zip");
                LOG.info("{}  {}: starting search", file.getAbsoluteFile(), channel.getTitle());
                value.put(channel.getChannelId(), performSearch(file, channel));
            }
            LOG.info("d");
        }

        return value;
    }

    List<SearchResult> performSearch(File file, Channel channel) throws IOException {
        final Instant startDate = getStartDate(2);

        if (channel.getVideoCount() > 5000) {
            LOG.warn("{}: skipping due to excessive videos ({})", channel.getTitle(), channel.getVideoCount());
            return Collections.emptyList();
        } else {
            final VideoSearchForm request = new VideoSearchForm();
            request.setChannelId(channel.getChannelId());
            request.setOrder("date");
            request.setPublishedBefore(Instant.now().truncatedTo(ChronoUnit.SECONDS));
            if (channel.getVideoCount() < 100) {
                request.setMaxResults(channel.getVideoCount().intValue());
            } else {
                request.setMaxResults(100);
                request.setPublishedAfter(startDate);
                request.setLastChecked(Instant.now());
            }

            LOG.info("{}: starting search", channel.getTitle());
            final List<SearchResult> results = searchService.search(request);

            LOG.info("{}: found {} of {}", channel.getTitle(), results.size(), channel.getVideoCount());

            for (int position = 0; position < results.size(); position++) {
                results.get(position).setRequestId(request.getId());
                results.get(position).setPosition(position);
            }

            filePersistenceService.writeVideoSearchResultsToFile(file.getAbsolutePath(), request, results);
            return results;
        }
    }

    @Bean
    public CommandLineRunner commandLineRunner(@SuppressWarnings("unused") ApplicationContext ctx) {

        LOG.info("0");
        // final String antonPetrovChannelId = "UCciQ8wFcVoIIMi-lfu8-cjQ";

        final String defaultSubscriptionFilename = "/nfs/media/youtube/cache-config/x.txt";

        final File root = TMPDIR;
        final File channelFile = new File(root, "channels.zip");
        final File searchRoot = new File(root, "searches");
        final File videoRoot = new File(root, "videos");
        searchRoot.mkdir();
        videoRoot.mkdir();


        // add subcommands
        //
        // - html -> list of subscription details
        // - txt -> list of subscriptions (handle: subscribed,title)
        // - subscription list -> sql (plus option to save raw response)
        // - create video download list
        //

        LOG.info("1");
        return args -> {

            // listBeans(ctx);
            // i18nService.bootstrap();

            LOG.info("2");

            boolean quotaExceeded = false;

            if (false) {
                zipChannelRepository.delete();
                final List<Channel> channels0 = findChannels();
                zipChannelRepository.insert(channels0);
                poiChannelRepository.insert(channels0);
            }

            if (false) {
                final List<Channel> channels1 = zipChannelRepository.findAll();
                channelRepository.delete();
                channelRepository.insert(channels1);
            }

            LOG.info("3");
            if (false) {
                LOG.info("A");
                final List<Channel> channels2 = zipChannelRepository.findAll();
                LOG.info("B: {}", channels2.size());
                final List<Channel> channels2a = new ArrayList<>();
                for (Channel channel : channels2) {
                    if (channel.getTopicCategories().stream().anyMatch(ch -> "Technology".equals(ch.getLabel()))) {
                        channels2a.add(channel);
                    }
                }
                LOG.info("C: {}", channels2a.size());

                // this still writes records to file...
                LOG.info("D");
                final Map<String, List<SearchResult>> results = performSearch(searchRoot, channels2a);
                LOG.info("E");
                // zipChannelRepository.insert(results);
            }
            LOG.info("4");

            if (false) {
                final List<Channel> channels3 = zipChannelRepository.findAll();

                videoSearchRequestRepository.delete();
                videoSearchResultRepository.delete();

                final List<Channel> channels3a = new ArrayList<>();
                for (Channel channel : channels3) {
                    if (channel.getTopicCategories().stream().anyMatch(ch -> "Technology".equals(ch.getLabel()))) {
                        channels3a.add(channel);

                        final File file = new File(searchRoot, channel.getChannelId() + ".zip");
                        if (file.exists()) {
                            final VideoSearchForm request = filePersistenceService.readVideoSearchFormFromFile(file.getAbsolutePath());
                            final List<SearchResult> results = filePersistenceService.readSearchResultsFromFile(file.getAbsolutePath());

                            if (request == null) {
                                LOG.warn("unable to retrieve request for {} ({})", file, channel.getTitle());
                            } else {
                                videoSearchRequestRepository.insert(request);
                                videoSearchResultRepository.insert(results);
                            }
                        }
                    }
                }
            }

            /*
                if (false) {
                    // alternately - read contents of directory
                    final List<Channel> channels4 = filePersistenceService.readChannelsFromFile(channelFile.getAbsolutePath());
                    for (Channel channel : channels4) {
                        final File file = new File(root, channel.getId() + ".zip");
                        if (file.exists()) {
                            final List<SearchResult> results = filePersistenceService.readSearchResultsFromFile(file.getAbsolutePath());
                            final List<Video> videos = results.stream().map(SearchResult::asVideo).filter(Optional::isPresent).map(Optional::get).toList();
                            if (false) {
                                if (!videos.isEmpty()) {
                                    final File file2 = new File(root, channel.getId() + ".zip");
                                    filePersistenceService.writeVideosToFile(file2.getAbsolutePath(), videos);
                                }
                            }
                        }
                    }
                }

                if (false) {
                    // alternately - read contents of directory
                    final List<Channel> channels5 = filePersistenceService.readChannelsFromFile(channelFile.getAbsolutePath());
                    for (Channel channel : channels5) {
                        final File file = new File(root, channel.getId() + ".zip");
                        if (file.exists()) {
                            final List<SearchResult> results = filePersistenceService.readSearchResultsFromFile(file.getAbsolutePath());
                            final List<String> videoIds = results.stream().map(SearchResult::asVideo).filter(Optional::isPresent).map(Optional::get).map(Video::getId).toList();
                            final List<Video> videos = videosService.getVideos(videoIds);

                            if (!videos.isEmpty()) {
                                final File file2 = new File(root, channel.getId() + ".zip");
                                filePersistenceService.writeVideosToFile(file2.getAbsolutePath(), videos);
                            }
                        }
                    }
                }

                if (false) {
                    final List<Channel> channels6 = filePersistenceService.readChannelsFromFile(channelFile.getAbsolutePath());
                    for (Channel channel : channels6) {
                        final File file2 = new File(root, channel.getId() + ".zip");
                        if (file2.exists()) {
                            final List<Video> videos = filePersistenceService.readVideosFromFile(file2.getAbsolutePath());
                            if (!videos.isEmpty()) {
                                videoRepository.insert(videos);
                            }
                        }
                    }
                }
            }
            */

            // report.listChannels(reportFilename, map.values());
            LOG.info("done...");

            // explicitly called so jooq shuts down
            SpringApplication.exit(ctx);
        };
    }

    public static void main(final String[] args) {
        SpringApplication.run(RetrieveSubscribedChannelDetails.class, args);
    }
}