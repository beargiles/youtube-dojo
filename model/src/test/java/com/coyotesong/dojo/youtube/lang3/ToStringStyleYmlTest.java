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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for YML implementation of 'ToStringStyle'
 */
public class ToStringStyleYmlTest {
    private static final Logger LOG = LoggerFactory.getLogger(ToStringStyleYmlTest.class);

    private final ToStringStyle style = new MyToStringStyle.ToStringStyleYml();

    private static class Scalars {
        private final ToStringStyle style;

        private boolean bool;

        // single byte/char
        private byte b;
        private char c;

        // numerics
        private short s;
        private int i;
        private long l;
        private float f;
        private double d;

        // string
        private String singleline;
        private String multiline;

        // temporal
        private Instant instant;

        private int[] iArray;
        private List<Integer> iList;
        private Map<Integer, String> iMap;
        private Map<String, List<String>> cMap;

        public Scalars(ToStringStyle style) {
            this.style = style;
            this.bool = true;
            this.b = (byte) 7;
            this.c = 'c';
            this.s = (short) 1;
            this.i = 2;
            this.l = 4;
            this.f = 8.0f;
            this.d = 9.0d;
            this.singleline = "string";
            this.multiline = "huey\ndewey\nlouie";
            this.instant = Instant.ofEpochSecond(12345L);
            this.iArray = new int[] { 1, 2, 3, 4 };
            this.iList = List.of(1, 2, 3, 4);
            this.iMap = new LinkedHashMap<>();
            this.iMap.put(1, "one");
            this.iMap.put(2, "two");
            this.iMap.put(3, "three");
            this.cMap = new LinkedHashMap<>();
            this.cMap.put("a", List.of("alpha", "Alan"));
            this.cMap.put("b", List.of("beta", "Betty", "Beth"));
            this.cMap.put("c", List.of("gamma", "Charlie"));
            this.cMap.put("d", List.of("delta", "David", "Delilah"));
        }

        public boolean isBool() {
            return bool;
        }

        public void setBool(boolean bool) {
            this.bool = bool;
        }

        public double getD() {
            return d;
        }

        public void setD(double d) {
            this.d = d;
        }

        public float getF() {
            return f;
        }

        public void setF(float f) {
            this.f = f;
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

        public long getL() {
            return l;
        }

        public void setL(long l) {
            this.l = l;
        }

        public short getS() {
            return s;
        }

        public void setS(short s) {
            this.s = s;
        }

        public String getSingleline() {
            return singleline;
        }

        public void setSingleline(String singleline) {
            this.singleline = singleline;
        }

        public String getMultiline() {
            return multiline;
        }

        public void setMultiline(String multiline) {
            this.multiline = multiline;
        }

        public Instant getInstant() {
            return instant;
        }

        public void setInstant(Instant instant) {
            this.instant = instant;
        }

        public int[] getiArray() {
            return iArray;
        }

        public void setiArray(int[] iArray) {
            this.iArray = iArray;
        }


        public String toString() {
            return new ToStringBuilder(this, style)
                    .append("boolean", this.bool)
                    .append("byte", this.b)
                    .append("char", this.c)
                    .append("short", this.s)
                    .append("int", this.i)
                    .append("long", this.l)
                    .append("float", this.f)
                    .append("double", this.d)
                    .append("intArray", this.iArray)
                    .append("intList", this.iList)
                    .append("singleline", this.singleline)
                    .append("multiline", this.multiline)
                    .append("instant", this.instant)
                    .append("intMap", this.iMap)
                    .append("cMap", this.cMap)
                    .toString();
        }
    }

    @Test
    public void testScalars() {
        final Scalars s = new Scalars(style);
        LOG.info("scalars...\n{}", s);
    }
}
