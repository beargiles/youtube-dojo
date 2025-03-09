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

package com.coyotesong.dojo.youtube.security;

import org.slf4j.MDC;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;

/**
 * Provide additional information in the logs.
 *
 * This class uses the 'MDC' mechanism to provide additional information
 * in the logs. This has several benefits:
 *
 *  - it allows the provided log messages to be shorter
 *  - it ensures the additional information can be reliably extracted from the log messages
 *  - it allows sensitive information to be sanitized.
 *
 * MDCCloseable is thread-local.
 *
 * In practice I have found this particularly useful when working with
 * external resources - it allows me to ensure all information I will need
 * to reproduce the connection present (albeit sanitized). This can be
 * things like URL, username, connection status (when appropriate), etc.
 *
 * IMPORTANT: this should only be used in try-with-resources blocks.
 * The MDC.MDCCloseable object does not implement AutoCloseable so
 * the content will occur in ALL subsequent log messages.
 *
 * IMPORTANT: the values are pass-by-value and that's fine since the
 * values are almost always static within the try-with-resource block.
 * In the rare cases where the value may change within this block
 * (e.g., you want to add an object created earlier in the try-with-resource
 * block) the class will also accept a Supplier(). It is important to
 * remember that these values are not cached - the supplier must work
 * no matter how many times it's called.
 */
public class Facts implements AutoCloseable {
    // the MDC values must be released in the reverse order of their addition.
    private final Stack<MDC.MDCCloseable> stack = new Stack<>();

    protected Facts() {
    }

    public Facts(String key, Object value) {
        stack.push(MDC.putCloseable(key, format(value)));
    }

    public Facts(Map<String, Object> values) {
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            stack.push(MDC.putCloseable(entry.getKey(), format(entry.getValue())));
        }
    }

    public Facts add(String key, Object value) {
        stack.push(MDC.putCloseable(key, format(value)));
        return this;
    }

    public void close() {
        while (!stack.isEmpty()) {
            stack.pop().close();
        }
    }

    /**
     * Recursively format an arbitrary object for log messages
     *
     * Note: this should be extended to include our model classes. This
     * would allow us to sanitize sensitive content. (There will also
     * need to be a way to conditionally disable the sanitization.
     *
     * Note: this should be extended to include additional standard java
     * classes as required. For instance REST requests and responses
     * are excellent choices.
     *
     * @TODO this should be modified to prove clean JSON. We could use
     * jackson but we need the ability to sanitize sensitive values first.
     *
     * @param value
     * @return
     */
    protected String format(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Supplier) {
            return format(((Supplier<?>) value).get());
        } else if (value instanceof Number) {
            // we could be more specific, e.g., to limit the precision
            return String.valueOf(value);
        } else if (value instanceof Boolean) {
            return Boolean.toString((Boolean) value);
        } else if (value instanceof Collection) {
            return formatCollection((Collection<?>) value);
        } else if (value instanceof Map) {
            return formatMap((Map<?, ?>) value);
        } else if (value instanceof Date) {
            return formatDate((Date) value);
        } else if (value instanceof Instant) {
            return formatInstant((Instant) value);
        } else if (value instanceof String) {
            // @TODO we don't want to blindly prepend/append quotes since
            // the string may also include quotes. We need to escape them.
            return (String) value;
        } else {
            return String.valueOf(value);
        }
    }

    protected String formatCollection(Collection<?> collection) {
        if (collection.isEmpty()) {
            return "[ ]";
        }

        return "[ " + String.join(", ", collection.stream().map(this::format).toList())+ " ]";
    }

    protected String formatMap(Map<?, ?> map) {
        if (map.isEmpty()) {
            return "{}";
        }
        final StringBuilder sb = new StringBuilder("{ ");
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            sb.append(format(entry.getKey()));
            sb.append(": ");
            sb.append(format(entry.getValue()));
            sb.append(", ");
        }
        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }
        sb.append(" }");

        return sb.toString();
    }

    protected String formatDate(Date date) {
        return DateTimeFormatter.ISO_INSTANT.format(Instant.ofEpochMilli(date.getTime()));
    }

    protected String formatInstant(Instant Instant) {
        return DateTimeFormatter.ISO_INSTANT.format(Instant);
    }
}
