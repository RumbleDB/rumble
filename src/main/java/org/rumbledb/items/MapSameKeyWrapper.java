/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rumbledb.items;

import org.rumbledb.api.Item;

import java.math.BigDecimal;

/**
 * Hash/equals wrapper for map keys compatible with {@code op:same-key}.
 *
 * <p>
 * Invariant: if {@link MapAtomicSameKey#sameKey(Item, Item)} holds for two keys, their wrappers
 * must have equal {@link #hashCode()} and {@link #equals(Object)}. Collisions only affect
 * performance.
 */
public final class MapSameKeyWrapper {

    private final Item key;
    private final int hash;

    public MapSameKeyWrapper(Item key) {
        this.key = key;
        if (key == null || !key.isAtomic()) {
            this.hash = 0;
        } else if (key.isString() || key.isAnyURI() || key.isUntypedAtomic()) {
            String s = key.getStringValue();
            this.hash = s == null ? 0 : s.hashCode();
        } else if (key.isNumeric()) {
            this.hash = numericSameKeyHash(key);
        } else if (
            key.isDate()
                || key.isTime()
                || key.isDateTime()
                || key.isGYear()
                || key.isGYearMonth()
                || key.isGMonth()
                || key.isGMonthDay()
                || key.isGDay()
        ) {
            this.hash = 0x47;
        } else if (
            key.isBoolean()
                || key.isHexBinary()
                || key.isBase64Binary()
                || key.isDuration()
                || key.isYearMonthDuration()
                || key.isDayTimeDuration()
        ) {
            this.hash = 0x4D;
        } else {
            this.hash = 0x4F;
        }
    }

    public Item getKey() {
        return this.key;
    }

    @Override
    public int hashCode() {
        return this.hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MapSameKeyWrapper)) {
            return false;
        }
        MapSameKeyWrapper other = (MapSameKeyWrapper) o;
        return MapAtomicSameKey.sameKey(this.key, other.key);
    }

    private static int numericSameKeyHash(Item k) {
        if (
            (k.isFloat() && Float.isNaN(k.getFloatValue()))
                || (k.isDouble() && Double.isNaN(k.getDoubleValue()))
        ) {
            return 0x4E614E;
        }
        if (k.isDouble()) {
            double d = k.getDoubleValue();
            if (d == Double.POSITIVE_INFINITY) {
                return 0x2B494E46;
            }
            if (d == Double.NEGATIVE_INFINITY) {
                return 0x2D494E46;
            }
        }
        if (k.isFloat()) {
            float f = k.getFloatValue();
            if (f == Float.POSITIVE_INFINITY) {
                return 0x2B494E46;
            }
            if (f == Float.NEGATIVE_INFINITY) {
                return 0x2D494E46;
            }
        }
        try {
            BigDecimal d = toExactDecimalKeyForHash(k);
            if (d.signum() == 0) {
                return 0;
            }
            BigDecimal normalized = d.stripTrailingZeros();
            return normalized.toPlainString().hashCode();
        } catch (Exception e) {
            return 1;
        }
    }

    private static BigDecimal toExactDecimalKeyForHash(Item k) {
        if (k.isDecimal()) {
            return k.getDecimalValue();
        }
        if (k.isInteger()) {
            return new BigDecimal(k.getIntegerValue());
        }
        if (k.isInt()) {
            return BigDecimal.valueOf(k.getIntValue());
        }
        if (k.isDouble()) {
            return BigDecimal.valueOf(k.getDoubleValue());
        }
        if (k.isFloat()) {
            return new BigDecimal(Float.toString(k.getFloatValue()));
        }
        return k.castToDecimalValue();
    }
}
