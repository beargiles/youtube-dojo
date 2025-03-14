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

package com.coyotesong.dojo.youtube.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Spring Boot web application
 */
@SpringBootApplication(scanBasePackages = {"com.coyotesong.dojo.youtube"})
@EntityScan({"com.coyotesong.dojo.youtube.model"})
//@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class YouTubeClientWebApp extends SpringBootServletInitializer {

    public static void main(String[] args) {
        final SpringApplication application = new SpringApplication(YouTubeClientWebApp.class);
        application.run(args);
    }
}