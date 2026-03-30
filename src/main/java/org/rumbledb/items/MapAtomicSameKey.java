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
import org.rumbledb.runtime.misc.AtomicDeepEqual;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import java.math.BigDecimal;

/**
 * XPath/XQuery {@code op:same-key} for map keys (atomic values), per FO 3.1 section 17.1.1.
 *
 * @see <a href="https://www.w3.org/TR/xpath-functions-31/#func-same-key">op:same-key</a>
 */
public final class MapAtomicSameKey {

    private MapAtomicSameKey() {
    }

    public static boolean sameKey(Item left, Item right) {
        if (left == null || right == null) {
            return false;
        }
        if (!left.isAtomic() || !right.isAtomic()) {
            return false;
        }
        if (isStringLike(left) && isStringLike(right)) {
            return codepointEqual(left.getStringValue(), right.getStringValue());
        }
        if (left.isNumeric() && right.isNumeric()) {
            return sameNumericKey(left, right);
        }
        if (isGregorian(left) && isGregorian(right)) {
            return gregorianSameKey(left, right);
        }
        if (isMiscDeepEqualKeyType(left) && isMiscDeepEqualKeyType(right)) {
            return AtomicDeepEqual.deepEqual(left, right);
        }
        return false;
    }

    private static boolean isStringLike(Item k) {
        return k.isString() || k.isAnyURI() || k.isUntypedAtomic();
    }

    private static boolean isGregorian(Item k) {
        return k.isDate()
            || k.isTime()
            || k.isDateTime()
            || k.isGYear()
            || k.isGYearMonth()
            || k.isGMonth()
            || k.isGMonthDay()
            || k.isGDay();
    }

    private static boolean isMiscDeepEqualKeyType(Item k) {
        if (k.isBoolean() || k.isHexBinary() || k.isBase64Binary()) {
            return true;
        }
        if (k.isDuration() || k.isYearMonthDuration() || k.isDayTimeDuration()) {
            return true;
        }
        ItemType t = k.getDynamicType();
        return BuiltinTypesCatalogue.QNameItem.equals(t) || BuiltinTypesCatalogue.NOTATIONItem.equals(t);
    }

    /**
     * Gregorian branch: types match, timezone precondition (FO 3.1), then {@code fn:deep-equal}.
     */
    private static boolean gregorianSameKey(Item k1, Item k2) {
        boolean tzPrecondition = (k1.hasTimeZone() && k2.hasTimeZone())
            || (!k1.hasTimeZone() && !k2.hasTimeZone())
            || AtomicDeepEqual.deepEqual(k1, k2);
        if (!tzPrecondition) {
            return false;
        }
        return AtomicDeepEqual.deepEqual(k1, k2);
    }

    /** {@code fn:codepoint-equal}: compare Unicode codepoint sequences (no collation). */
    private static boolean codepointEqual(String a, String b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        int i = 0;
        int j = 0;
        while (i < a.length() && j < b.length()) {
            int cpa = a.codePointAt(i);
            int cpb = b.codePointAt(j);
            if (cpa != cpb) {
                return false;
            }
            i += Character.charCount(cpa);
            j += Character.charCount(cpb);
        }
        return i >= a.length() && j >= b.length();
    }

    private static boolean sameNumericKey(Item k1, Item k2) {
        if (bothNaN(k1, k2)) {
            return true;
        }
        if (isNaNFloatOrDouble(k1) || isNaNFloatOrDouble(k2)) {
            return false;
        }
        if (bothPositiveInfinity(k1, k2)) {
            return true;
        }
        if (bothNegativeInfinity(k1, k2)) {
            return true;
        }
        if (isInfinityFloatOrDouble(k1) || isInfinityFloatOrDouble(k2)) {
            return false;
        }
        try {
            BigDecimal d1 = toExactDecimalKey(k1);
            BigDecimal d2 = toExactDecimalKey(k2);
            if (d1.signum() == 0 && d2.signum() == 0) {
                return true;
            }
            return d1.compareTo(d2) == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean bothNaN(Item a, Item b) {
        return isNaNFloatOrDouble(a) && isNaNFloatOrDouble(b);
    }

    private static boolean isNaNFloatOrDouble(Item k) {
        return (k.isFloat() && Float.isNaN(k.getFloatValue()))
            || (k.isDouble() && Double.isNaN(k.getDoubleValue()));
    }

    private static boolean bothPositiveInfinity(Item a, Item b) {
        return isPosInf(a) && isPosInf(b);
    }

    private static boolean bothNegativeInfinity(Item a, Item b) {
        return isNegInf(a) && isNegInf(b);
    }

    private static boolean isInfinityFloatOrDouble(Item k) {
        return isPosInf(k) || isNegInf(k);
    }

    private static boolean isPosInf(Item k) {
        if (k.isDouble()) {
            return k.getDoubleValue() == Double.POSITIVE_INFINITY;
        }
        if (k.isFloat()) {
            return k.getFloatValue() == Float.POSITIVE_INFINITY;
        }
        return false;
    }

    private static boolean isNegInf(Item k) {
        if (k.isDouble()) {
            return k.getDoubleValue() == Double.NEGATIVE_INFINITY;
        }
        if (k.isFloat()) {
            return k.getFloatValue() == Float.NEGATIVE_INFINITY;
        }
        return false;
    }

    /**
     * Exact decimal for the XSD numeric value (no {@code eq}-style double rounding).
     */
    private static BigDecimal toExactDecimalKey(Item k) {
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
            double d = k.getDoubleValue();
            return BigDecimal.valueOf(d);
        }
        if (k.isFloat()) {
            return new BigDecimal(Float.toString(k.getFloatValue()));
        }
        return k.castToDecimalValue();
    }
}
