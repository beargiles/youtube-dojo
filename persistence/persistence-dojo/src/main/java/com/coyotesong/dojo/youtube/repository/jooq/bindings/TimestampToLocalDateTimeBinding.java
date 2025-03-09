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

package com.coyotesong.dojo.youtube.repository.jooq.bindings;

import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.impl.DefaultBinding;

import java.io.Serial;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * jOOQ binding between Timestamp (database type) and LocalDateTime (java type)
 *
 * Per the author of jOOQ we must use a Binding for this. If we use a Converter
 * the data will be truncated into a LocalDateTime internally at some point
 * prior to the Converter.
 *
 * See [jOOQ Custom Data Types](https://www.jooq.org/doc/latest/manual/code-generation/custom-data-types/)
 *
 * See [jOOQ issue 5539](https://github.com/jOOQ/jOOQ/issues/5539)
 */
public class TimestampToLocalDateTimeBinding implements Binding<Timestamp, LocalDateTime> {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final ZoneId ZONE_ID_UTC = ZoneOffset.UTC;
    private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone(ZONE_ID_UTC);

    private final Converter<Timestamp, LocalDateTime> converter;
    private final Binding<Timestamp, LocalDateTime> delegate;

    public static class TimestampToLocalDateTimeConverter implements Converter<Timestamp, LocalDateTime> {
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * Convert from {@code LocalDateTime} to {@code Instant}
         */
        @Override
        public LocalDateTime from(Timestamp databaseObject) {
            if (databaseObject == null) {
                return null;
            }
            return databaseObject.toLocalDateTime();
        }

        /**
         * Convert from {@code Instant} to {@code Timestamp}
         */
        @Override
        public Timestamp to(LocalDateTime userObject) {
            if (userObject == null) {
                return null;
            }
            return Timestamp.valueOf(userObject);
        }

        /**
         * Return the 'from' Type Class (Database Type Class)
         */
        @Override
        @NotNull
        public Class<Timestamp> fromType() {
            return Timestamp.class;
        }

        /**
         * Return the 'to' Type Class (User type Class)
         */
        @Override
        @NotNull
        public Class<LocalDateTime> toType() {
            return LocalDateTime.class;
        }
    }

    public TimestampToLocalDateTimeBinding() {
        this.converter = new TimestampToLocalDateTimeConverter();
        this.delegate = DefaultBinding.binding(converter);
    }

    // The converter does all the work
    @Override
    @NotNull
    public Converter<Timestamp, LocalDateTime> converter() {
        return converter;
    }

    // Rending a bind variable for the binding context's value and casting it to the user type
    @Override
    public void sql(BindingSQLContext<LocalDateTime> ctx) throws SQLException {
        delegate.sql(ctx);
    }

    // Registering TIMESTAMP types for JDBC CallableStatement OUT parameters
    @Override
    public void register(BindingRegisterContext<LocalDateTime> ctx) throws SQLException {
        delegate.register(ctx);
    }

    // Getting a TIMESTAMP value from a JDBC ResultSet and converting that to an Instant
    @Override
    @SuppressWarnings("all") // we shouldn't close resultSet!
    public void get(BindingGetResultSetContext<LocalDateTime> ctx) throws SQLException {
        final Calendar calendar = Calendar.getInstance(TIMEZONE_UTC);

        final ResultSet resultSet = ctx.resultSet();
        final Timestamp timestamp = resultSet.getTimestamp(ctx.index(), calendar);

        if (timestamp == null) {
            ctx.value(null);
        } else {
            ctx.value(converter.from(timestamp));
        }
    }

    // Getting a TIMESTAMP value from a JDBC CallableStatement and converting that to an Instant
    @Override
    @SuppressWarnings("all") // we shouldn't close callableStatement!
    public void get(BindingGetStatementContext<LocalDateTime> ctx) throws SQLException {
        final Calendar calendar = Calendar.getInstance(TIMEZONE_UTC);

        final CallableStatement statement = ctx.statement();
        final Timestamp timestamp = statement.getTimestamp(ctx.index(), calendar);

        if (timestamp == null) {
            ctx.value(null);
        } else {
            ctx.value(converter.from(timestamp));
        }
    }

    // Converting the Instant to a TIMESTAMP value and setting that on a JDBC PreparedStatement
    @Override
    @SuppressWarnings("all") // we shouldn't close preparedStatement
    public void set(BindingSetStatementContext<LocalDateTime> ctx) throws SQLException {
        final Calendar calendar = Calendar.getInstance(TIMEZONE_UTC);
        final LocalDateTime value = ctx.value();
        final PreparedStatement statement = ctx.statement();

        if (value == null) {
            statement.setNull(ctx.index(), Types.TIMESTAMP);
        } else {
            final Timestamp timestamp = converter.to(value);
            statement.setTimestamp(ctx.index(), timestamp, calendar);
        }
    }

    // Getting a value from a JDBC SQLInput
    @Override
    public void get(BindingGetSQLInputContext<LocalDateTime> ctx) throws SQLException {
        final SQLInput input = ctx.input();
        final Timestamp timestamp = input.readTimestamp();

        if (timestamp == null) {
            ctx.value(null);
        } else {
            ctx.value(converter.from(timestamp));
        }
    }

    // Setting a value on a JDBC SQLOutput
    @Override
    public void set(BindingSetSQLOutputContext<LocalDateTime> ctx) throws SQLException {
        final SQLOutput output = ctx.output();
        final LocalDateTime instant = ctx.value();

        if (instant == null) {
            output.writeTimestamp(null);
        } else {
            output.writeTimestamp(converter.to(instant));
        }
    }
}
