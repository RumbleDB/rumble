package org.rumbledb.runtime.misc;

import java.util.Arrays;

import org.rumbledb.api.Item;

/**
 * Atomic value-comparison semantics for operations such as {@code distinct-values} and grouping.
 *
 * This is intentionally separate from Java equality and map {@code op:same-key}: numeric value
 * comparison applies type promotion and is not transitive across mixed numeric types.
 */
public final class AtomicValueComparison {

    private AtomicValueComparison() {
    }

    public static boolean equal(Item left, Item right) {
        if (left == right) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        return AtomicDeepEqual.deepEqual(left, right);
    }

    /**
     * A hash compatible with atomic value comparison.
     *
     * Numeric values are reduced to float because float is the lowest-precision promotion target:
     * any pair equal after decimal/float/double promotion necessarily has the same float value.
     * Gregorian and duration hashes are deliberately coarse because their comparison depends on
     * timezone normalization or subtype promotion.
     */
    public static int hash(Item item) {
        if (item == null) {
            return 0;
        }
        if (item.isNumeric()) {
            float value = item.castToFloatValue();
            return value == 0.0f ? 0 : Float.hashCode(value);
        }
        if (item.isString() || item.isAnyURI() || item.isUntypedAtomic()) {
            return item.getStringValue().hashCode();
        }
        if (item.isBoolean()) {
            return Boolean.hashCode(item.getBooleanValue());
        }
        if (item.isHexBinary() || item.isBase64Binary()) {
            return Arrays.hashCode(item.getBinaryValue());
        }
        if (
            item.isDate()
                || item.isTime()
                || item.isDateTime()
                || item.isGYear()
                || item.isGYearMonth()
                || item.isGMonth()
                || item.isGMonthDay()
                || item.isGDay()
        ) {
            return 0x47;
        }
        if (item.isDuration() || item.isYearMonthDuration() || item.isDayTimeDuration()) {
            return 0x44;
        }
        if (item.isQName()) {
            return item.getQNameValue().hashCode();
        }
        return 0;
    }
}
