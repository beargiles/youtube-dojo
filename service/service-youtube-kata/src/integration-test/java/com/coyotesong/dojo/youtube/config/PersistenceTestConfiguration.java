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

package com.coyotesong.dojo.youtube.config;

// import com.coyotesong.dojo.youtube.repository.TestObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * Test configuration (test profile)
 */
@Configuration
@Import({
        TestJdbcProperties.class,
        TestJooqProperties.class
})
@ComponentScan({
        "com.coyotesong.dojo.youtube.config",
        "com.coyotesong.dojo.youtube.repository",
        "com.coyotesong.dojo.youtube.security",
        "com.coyotesong.dojo.youtube.service"
})
@Profile("test")
public class PersistenceTestConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(PersistenceTestConfiguration.class);

    // @Bean
    // public TestObjectFactory testObjectFactory() {
    //    return new TestObjectFactory();
    //}
}
