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

package com.coyotesong.dojo.youtube.service;

import com.coyotesong.dojo.youtube.model.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Provide 'quotaExceeded' property
 * <p>
 * Implementation note: 'quotaExceeded' is a dynamic property for maximum flexibility in
 * our implementation. E.g., we could create a TestContainer that replays previously
 * captured network traffic in order to avoid any impact on our quota.
 * </p>
 */
public abstract class AbstractYouTubeServiceITest {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractYouTubeServiceITest.class);

    // Anton Petrov - science vlogger
    private static final String TEST_CHANNEL_ID = "UCciQ8wFcVoIIMi-lfu8-cjQ";

    private static Optional<YouTubeChannelsService> optionalChannelsService = Optional.empty();

    private static final List<Boolean> quotaExceeded = new ArrayList<>();
    // private static final Optional<Boolean> quotaExceeded = Optional.empty();

    protected AbstractYouTubeServiceITest(YouTubeChannelsService channelsService) {
        // optionalChannelsService = Optional.of(channelsService);
        // quotaExceeded.add(performTestQuery(channelsService));
    }

    /**
     * Define dynamic properties
     * <p>
     * At the moment we only perform a test query once even if the '@DirtiesContext' annotation
     * is present.
     * </p>
     * <p>
     * Possible improvement: update this flag if tests start failing
     * </p>
     *
     * @param registry
     */
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        if (quotaExceeded.isEmpty() && optionalChannelsService.isPresent()) {
            quotaExceeded.add(performTestQuery(optionalChannelsService.get()));
        }

        registry.add("quotaExceeded", () -> quotaExceeded.isEmpty() ? Boolean.TRUE : quotaExceeded.get(0));
    }

    /**
     * Perform test query
     *
     * @return true if quota is exceeded or invalid API credentials were provided
     */
    static Boolean performTestQuery(YouTubeChannelsService channelsService) {
        try {
            @SuppressWarnings("unused") final Channel channel;
            channel = channelsService.getChannel(TEST_CHANNEL_ID);
            return Boolean.FALSE;
        } catch (IOException e) {
            LOG.warn("{}: {}", e.getClass().getName(), e.getMessage());
            return Boolean.TRUE;
        } catch (YouTubeQuotaExceededException e) {
            LOG.warn("quota exceeded!");
            return Boolean.TRUE;
        } catch (YouTubeAuthenticationFailureException e) {
            LOG.warn("invalid credentials!");
            return Boolean.TRUE;
        }
    }
}
