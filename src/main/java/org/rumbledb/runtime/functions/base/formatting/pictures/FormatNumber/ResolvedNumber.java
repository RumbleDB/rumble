package org.rumbledb.runtime.functions.base.formatting.pictures.FormatNumber;

import java.math.BigDecimal;

class ResolvedNumber {
    // Type identifiers
    public static final String INTEGER = "INTEGER";
    public static final String DECIMAL = "DECIMAL";
    public static final String DOUBLE = "DOUBLE";
    public static final String FLOAT = "FLOAT";

    // Original type
    public final String type;

    // Values (only one is relevant depending on type)
    public final BigDecimal decimalValue;
    public final Double doubleValue;
    public final Float floatValue;

    // Sign info
    public final boolean isNegative;

    private ResolvedNumber(
            String type,
            BigDecimal decimalValue,
            Double doubleValue,
            Float floatValue,
            boolean isNegative
    ) {
        this.type = type;
        this.decimalValue = decimalValue;
        this.doubleValue = doubleValue;
        this.floatValue = floatValue;
        this.isNegative = isNegative;
    }

    // Factory methods

    public static ResolvedNumber fromInteger(BigDecimal value, boolean isNegative) {
        return new ResolvedNumber(
                INTEGER,
                value,
                null,
                null,
                isNegative
        );
    }

    public static ResolvedNumber fromDecimal(BigDecimal value, boolean isNegative) {
        return new ResolvedNumber(
                DECIMAL,
                value,
                null,
                null,
                isNegative
        );
    }

    public static ResolvedNumber fromDouble(double value, boolean isNegative) {
        return new ResolvedNumber(
                DOUBLE,
                BigDecimal.valueOf(value),
                value,
                null,
                isNegative
        );
    }

    public static ResolvedNumber fromFloat(float value, boolean isNegative) {
        return new ResolvedNumber(
                FLOAT,
                BigDecimal.valueOf(value),
                null,
                value,
                isNegative
        );
    }

    @Override
    public String toString() {
        return "ResolvedNumber{"
            +
            "type='"
            + this.type
            + '\''
            +
            ", decimalValue="
            + this.decimalValue
            +
            ", doubleValue="
            + this.doubleValue
            +
            ", floatValue="
            + this.floatValue
            +
            ", isNegative="
            + isNegative
            +
            '}';
    }
}
