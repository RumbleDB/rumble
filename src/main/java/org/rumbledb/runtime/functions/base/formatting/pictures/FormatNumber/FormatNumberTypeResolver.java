package org.rumbledb.runtime.functions.base.formatting.pictures.FormatNumber;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.OurBadException;

import java.math.BigDecimal;
import java.math.BigInteger;

final class FormatNumberTypeResolver {
    private FormatNumberTypeResolver() {
    }

    // We want to use BigDecimal for an easier formatting experience, but Float or Double can be negative zero, so we
    // pass an additional flag that can be easily checked
    static ResolvedNumber getValue(Item item) {
        if (item.isInteger()) {
            BigInteger value = item.getIntegerValue();
            return ResolvedNumber.fromInteger(
                new BigDecimal(value),
                value.signum() < 0
            );
        }

        if (item.isDecimal()) {
            BigDecimal value = item.getDecimalValue();
            return ResolvedNumber.fromDecimal(
                value,
                value.compareTo(BigDecimal.ZERO) < 0
            );
        }

        if (item.isDouble()) {
            double value = item.getDoubleValue();
            boolean isNegative = value < 0.0d
                || Double.doubleToRawLongBits(value) == Double.doubleToRawLongBits(-0.0d);

            return ResolvedNumber.fromDouble(value, isNegative);
        }

        if (item.isFloat()) {
            float value = item.getFloatValue();
            boolean isNegative = value < 0.0f
                || Float.floatToRawIntBits(value) == Float.floatToRawIntBits(-0.0f);

            return ResolvedNumber.fromFloat(value, isNegative);
        }

        throw new OurBadException("Item appears to be not numeric, this should never happen");
    }
}
