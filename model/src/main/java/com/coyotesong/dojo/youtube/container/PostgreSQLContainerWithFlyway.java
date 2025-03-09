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

package com.coyotesong.dojo.youtube.container;

import com.coyotesong.dojo.youtube.security.LogDatabaseMetaData;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.images.builder.Transferable;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

/**
 * PostgreSQL test container extended with support for flyway initialization
 *
 * @param <T> PostgreSQLContainer
 */
@SuppressWarnings("unused")
public class PostgreSQLContainerWithFlyway<T extends PostgreSQLContainer<T>> extends PostgreSQLContainer<T> {
    private static final Logger LOG = LoggerFactory.getLogger(PostgreSQLContainerWithFlyway.class);

    public PostgreSQLContainerWithFlyway(String dockerImageName) {
        super(dockerImageName);

        // super.withEnv(Map.of("LANG", "en_US.UTF8", "LC_ALL", "en_US.UTF8"));
        // super.withLocalCompose(true);

        // final Locale l = Locale.getDefault();
        final Locale l = Locale.US;
        /*
        final Map<String, String> env = super.getEnvMap();
        // env.put("LANG", l.getLanguage() + "." + l.getVariant());
        env.put("LANG", "en_US.UTF8");
        // env.put("LANGUAGE", l.getLanguage());
        env.put("LC_ALL", "en_US.UTF8");
        LOG.info("-----------------------------------");
        LOG.info("LANG: {}", env.get("LANG"));
        // LOG.info("LANGUAGE: {}", env.get("LANGUAGE"));
        LOG.info("LC_ALL: {}", env.get("ALL"));
        LOG.info("-----------------------------------");
         */

        final Logger log = LoggerFactory.getLogger("Container[" + dockerImageName + "]");
        final List<Consumer<OutputFrame>> consumers = new ArrayList<>();
        consumers.add(new MySlf4jLogConsumer<>(log));
        super.setLogConsumers(consumers);
    }

    public void start() {
        /*
        org.testcontainers.utility.
        Transferable t = new Transferable();
        super.copyFileToContainer(transferable, "/etc/locale.conf");
         */
        super.start();
    }

    /**
     * Get Hikari DataSource
     *
     * Get Hikari DataSource - this is an improvement over a standard datasource
     * since it's auto-closeable.
     *
     * @return new dataSource
     */
    public HikariDataSource getDataSource() {
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(getJdbcUrl());
        config.setUsername(getUsername());
        config.setPassword(getPassword());
        config.setDriverClassName(getDriverClassName());
        config.setConnectionTestQuery(getTestQueryString());

        return new HikariDataSource(config);
    }

    /**
     * Initialize schemas (via flyway)
     */
    @Override
    protected void runInitScriptIfRequired() {
        // initialize schemas and initialized data via flyway
        try (HikariDataSource dataSource = getDataSource()) {
            final Flyway flyway = Flyway.configure().dataSource(dataSource).load();
            flyway.migrate();
        }

        super.runInitScriptIfRequired();
    }

    @Override
    protected void containerIsStarted(InspectContainerResponse containerInfo) {
        super.containerIsStarted(containerInfo);
        try (Connection conn = this.getDataSource().getConnection()) {
            super.logger().info("\n" + LogDatabaseMetaData.format(conn.getMetaData()));
        } catch (IOException | SQLException e) {
            final String message = String.format("%s: unable to get connection: %s", e.getClass().getName(), e.getMessage());
            super.logger().error(message, e);
        }
    }
}
