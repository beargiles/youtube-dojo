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

package com.coyotesong.dojo.youtube.controller;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Startup (and shutdown) controller
 * <p>
 * This controller can be used for things like loading shared caches, storing
 * performance metrics, etc.
 * </p>
 * <p>
 * In general anything that's only used by a single controller should be
 * managed by that controller. Some people will disagree and think ala
 * startup and shutdown tasks should be handled in a single location,
 * primarily because it allows the developer full control of when each
 * task is executed.
 * </p>
 */
public class StartupController {
    private static final Logger LOG = LoggerFactory.getLogger(StartupController.class);

    /**
     * Actions taken when the application is started
     */
    @PostConstruct
    public void startup() {
    }

    /**
     * Actions taken when the application is shut down
     */
    @PreDestroy
    public void shutdown() {
    }
}
