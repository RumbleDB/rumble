package org.rumbledb.items;

import java.math.BigDecimal;

import org.rumbledb.api.Item;

/**
 * Defines the stable Java equality and hashing contract for atomic items.
 *
 * Query value comparisons are intentionally not used here: they can be context-dependent, can
 * raise dynamic errors, and do not form an equivalence relation. The XDM {@code op:same-key}
 * relation is stable and is already the equality required by maps.
 */
public final class AtomicItemEquivalence {

    private AtomicItemEquivalence() {
    }

    public static boolean equivalent(Item left, Item right) {
        if (left == right) {
            return true;
        }
        if (left == null || right == null || !left.isAtomic() || !right.isAtomic()) {
            return false;
        }
        if (left.isNull() || right.isNull()) {
            return left.isNull() && right.isNull();
        }
        return MapAtomicSameKey.sameKey(left, right);
    }

    public static int hash(Item item) {
        if (item == null || !item.isAtomic()) {
            return 0;
        }
        if (item.isNull()) {
            return 0x4E554C4C;
        }
        if (item.isString() || item.isAnyURI() || item.isUntypedAtomic()) {
            String value = item.getStringValue();
            return value == null ? 0 : value.hashCode();
        }
        if (item.isNumeric()) {
            return numericHash(item);
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
            // A deliberately coarse hash keeps all values accepted by the Gregorian same-key
            // rules compatible, independently of timezone representation.
            return 0x47;
        }
        if (
            item.isBoolean()
                || item.isHexBinary()
                || item.isBase64Binary()
                || item.isDuration()
                || item.isYearMonthDuration()
                || item.isDayTimeDuration()
        ) {
            // These types use deep-equal. A coarse hash is safe and avoids duplicating that logic.
            return 0x4D;
        }
        // QName, NOTATION, and future atomic types remain correct with a conservative hash.
        return 0x4F;
    }

    private static int numericHash(Item item) {
        if (
            (item.isFloat() && Float.isNaN(item.getFloatValue()))
                || (item.isDouble() && Double.isNaN(item.getDoubleValue()))
        ) {
            return 0x4E614E;
        }
        if (item.isDouble()) {
            double value = item.getDoubleValue();
            if (value == Double.POSITIVE_INFINITY) {
                return 0x2B494E46;
            }
            if (value == Double.NEGATIVE_INFINITY) {
                return 0x2D494E46;
            }
        }
        if (item.isFloat()) {
            float value = item.getFloatValue();
            if (value == Float.POSITIVE_INFINITY) {
                return 0x2B494E46;
            }
            if (value == Float.NEGATIVE_INFINITY) {
                return 0x2D494E46;
            }
        }
        try {
            BigDecimal value = exactDecimal(item);
            if (value.signum() == 0) {
                return 0;
            }
            return value.stripTrailingZeros().toPlainString().hashCode();
        } catch (RuntimeException e) {
            return 1;
        }
    }

    private static BigDecimal exactDecimal(Item item) {
        if (item.isDecimal()) {
            return item.getDecimalValue();
        }
        if (item.isInteger()) {
            return new BigDecimal(item.getIntegerValue());
        }
        if (item.isInt()) {
            return BigDecimal.valueOf(item.getIntValue());
        }
        if (item.isDouble()) {
            return BigDecimal.valueOf(item.getDoubleValue());
        }
        if (item.isFloat()) {
            return new BigDecimal(Float.toString(item.getFloatValue()));
        }
        return item.castToDecimalValue();
    }
}
