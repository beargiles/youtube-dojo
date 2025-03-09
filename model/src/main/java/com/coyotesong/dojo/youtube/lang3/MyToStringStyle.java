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

package com.coyotesong.dojo.youtube.lang3;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.net.URI;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MyToStringStyle {
    private static final Logger LOG = LoggerFactory.getLogger(MyToStringStyle.class);

    public static ToStringStyle YML = new ToStringStyleYml();

    // ToStringStyle.JSON_STYLE
    // ToStringStyle.MULTI_LINE_STYLE

    public static ToStringStyle DEFAULT_STYLE = YML;

    static void open() {
        // Module me = MyToStringStyle.class.getModule();
        Module me = org.apache.commons.lang3.builder.ReflectionToStringBuilder.class.getModule();
        String javaNet = URI.class.getPackageName();
        Module javaBase = URI.class.getModule();
        if (!javaBase.getPackages().contains(javaNet)) {
            LOG.info("unexpected module + package!");
//        } else if (me.isNamed()) {
//            LOG.info("this is not the unnamed module!");
        } else if (!me.isOpen(javaNet, javaBase)) {
            LOG.info("does module {} export {} unconditionally?  {}", javaBase.getName(), javaNet,
                    javaBase.isExported(javaNet));
            LOG.info("does module {} export {} to {} module?  {}", javaBase.getName(), javaNet, me,
                    me.isOpen(javaNet, javaBase));

            me.addOpens(javaNet, javaBase);

            LOG.info("does module {} export {} unconditionally?  {}", javaBase.getName(), javaNet,
                    javaBase.isExported(javaNet));
            LOG.info("does module {} export {} to {} module?  {}", javaBase.getName(), javaNet, me,
                    me.isOpen(javaNet, javaBase));
        }
    }

    public static class ToStringStyleYml extends MultilineRecursiveToStringStyle {
        @Serial
        private static final long serialVersionUID = 1L;

        private static final Pattern SAFE_FIELDNAME_PATTERN = Pattern.compile("^[[:ALNUM:]_-]+$");

        // I think we also need to check for ':'....
        private static final Pattern SAFE_SINGLELINE_PATTERN = Pattern.compile("^[^\\n]$");

        private static final String NEWLINE = "\n";
        private static final String SPACER = "  ";
        private static final String START_OF_DICTIONARY = SPACER.replaceFirst(" ", "-");
        private static final String CONTINUATION = "|";

        private static final String SQUOTES = "'";
        private static final String DQUOTES = "\"";

        private final DateTimeFormatter formatter;
        private final boolean skipNulls = true;

        private final String prefix;
        private final String listPrefix;
        private final String dictPrefix;

        public ToStringStyleYml() {
            this(DateTimeFormatter.ISO_INSTANT);
            open();
            // this.setNullText("~");
        }

        protected ToStringStyleYml(DateTimeFormatter formatter) {
            this.prefix = NEWLINE + SPACER;
            this.listPrefix = this.prefix + START_OF_DICTIONARY;
            this.dictPrefix = this.prefix + SPACER;
            this.formatter = formatter;

            super.setUseClassName(false);
            super.setUseFieldNames(true);
            super.setUseIdentityHashCode(false);

            super.setNullText("~");

            super.setContentStart(START_OF_DICTIONARY);
            super.setContentEnd("");

            super.setFieldNameValueSeparator(": ");
            super.setFieldSeparator(prefix);
            super.setFieldSeparatorAtEnd(false);
            super.setFieldSeparatorAtStart(false);

            super.setArrayContentDetail(false);
            super.setArrayEnd(" (ae) ");
            super.setArraySeparator((" (ep) "));
            super.setArrayStart(" (as) ");

            super.setSizeEndText(" (ze) ");
            super.setSizeStartText(" (zs) ");

            super.setSummaryObjectEndText(" (soe) ");
            super.setSummaryObjectStartText(" (sos) ");
        }

        protected String quoteFieldName(String fieldName) {
            if (!SAFE_FIELDNAME_PATTERN.matcher(fieldName).matches()) {
                return fieldName;
            }

            if (!fieldName.contains(SQUOTES)) {
                return SQUOTES + fieldName + SQUOTES;
            }

            if (!fieldName.contains(DQUOTES)) {
                return DQUOTES + fieldName + DQUOTES;
            }

            throw new RuntimeException("Can't handle this fieldName!");
        }

        @Override
        protected void appendDetail(StringBuffer buffer, String fieldName, byte[] array) {
            for (byte value : array) {
                buffer.append(listPrefix).append(value);
            }
        }

        @Override
        protected void appendDetail(StringBuffer buffer, String fieldName, char[] array) {
            for (char value : array) {
                buffer.append(listPrefix).append(value);
            }
        }

        @Override
        protected void appendDetail(StringBuffer buffer, String fieldName, short[] array) {
            for (short value : array) {
                buffer.append(listPrefix).append(value);
            }
        }

        @Override
        protected void appendDetail(StringBuffer buffer, String fieldName, int[] array) {
            for (int value : array) {
                buffer.append(listPrefix).append(value);
            }
        }

        @Override
        protected void appendDetail(StringBuffer buffer, String fieldName, long[] array) {
            for (long value : array) {
                buffer.append(listPrefix).append(value);
            }
        }

        @Override
        protected void appendDetail(StringBuffer buffer, String fieldName, float[] array) {
            for (float value : array) {
                buffer.append(listPrefix).append(value);
            }
        }

        @Override
        protected void appendDetail(StringBuffer buffer, String fieldName, double[] array) {
            for (double value : array) {
                buffer.append(listPrefix).append(value);
            }
        }

        @Override
        protected void appendDetail(StringBuffer buffer, String fieldName, Object[] array) {
            for (Object value : array) {
                buffer.append(listPrefix).append(value);
            }
        }

        @Override
        protected void appendDetail(StringBuffer buffer, String fieldName, Collection<?> col) {
            if (col.isEmpty()) {
                buffer.append("[]");
            } else {
                for (Object object : col) {
                    if ((object == null) && skipNulls) {
                        continue;
                    }
                    if (object instanceof String) {
                        buffer.append(listPrefix).append(object);
                    } else if (object instanceof Enum || object instanceof Number) {
                        buffer.append(listPrefix).append(object);
                    } else {
                        final String value = object.toString();
                        if (!value.contains(NEWLINE)) {
                            buffer.append(listPrefix).append(value);
                        } else {
                            buffer.append(dictPrefix);
                            buffer.append(List.of(value.split(NEWLINE)).stream().collect(Collectors.joining(dictPrefix)));
                        }
                    }

                    /*
                    String value = Objects.toString(object);
                    if (!value.contains(NEWLINE)) {
                        buffer.append(arrayPrefix);
                        buffer.append(value);
                    } else {
                        for (String line : value.split(NEWLINE)) {
                            buffer.append(prefix).append(SPACER).append(line);
                        }
                    }
                     */
                }
            }
        }

        // a Map looks like a POJO - no [], etc.
        @Override
        public void appendDetail(StringBuffer buffer, String fieldName, Map<?, ?> map) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if ((entry.getValue() == null) && skipNulls) {
                    continue;
                }

                final Object obj = entry.getKey();
                // should we limit keys to strings and simple objects?
                // we definitely need to ensure there's no ':', control characters, etc.
                final String key = Objects.toString(obj);
                buffer.append(dictPrefix).append(key).append(getFieldNameValueSeparator());
                appendDetail(buffer, entry.getValue());
            }
        }

        @Override
        public void appendDetail(StringBuffer buffer, String fieldName, Object value) {
            if ((value == null) && skipNulls) {
                return;
            }

            // buffer.append(buffer.isEmpty() ? START : prefix);
            // buffer.append(quoteFieldName(fieldName)).append(getFieldNameValueSeparator());
            appendDetail(buffer, value);
        }

        void appendDetail(StringBuffer buffer, Object value) {
            if (value == null) {
                if (!skipNulls) {
                    buffer.append("~");
                }
            } else if (value instanceof String) {
                final String s = (String) value;
                if (!s.contains(NEWLINE)) {
                    buffer.append(s);
                } else {
                    buffer.append(CONTINUATION);
                    for (String line : s.split(NEWLINE)) {
                        buffer.append(prefix).append(SPACER).append(line);
                    }
                }
            } else if (value instanceof Temporal) {
                buffer.append(formatter.format((Temporal) value));
            } else if (value instanceof URL) {
                buffer.append(((URL) value).toExternalForm());
            } else if (value instanceof URI) {
                buffer.append(value);
            } else if (value instanceof Collection) {
                buffer.append("--- collection ---");
            } else {
                buffer.append(Objects.toString(value));
            }
        }
    }
}
