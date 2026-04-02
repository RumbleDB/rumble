package org.rumbledb.runtime.functions.base.formatting.language;

import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.Month;

public interface LanguageFormatter {

    /**
     * Returns the language identifier handled by this formatter, for example "en".
     */
    String getLanguage();

    // INTEGER FUNCTIONS

    /**
     * Returns the cardinal word representation of a non-negative integer.
     *
     * <p>
     * Examples in English: 0 -> "zero", 15 -> "fifteen", 42 -> "forty-two".
     * </p>
     *
     * <p>
     * The implementation should support at least the full non-negative Java int range.
     * </p>
     */
    String toCardinal(int value);

    /**
     * Returns the ordinal word representation of a non-negative integer.
     *
     * <p>
     * Examples in English: 1 -> "first", 15 -> "fifteenth", 42 -> "forty-second".
     * </p>
     *
     * <p>
     * The implementation should support at least the full non-negative Java int range.
     * </p>
     */
    String toOrdinal(int value);

    /**
     * Returns the ordinal suffix for the given non-negative integer, if the language supports one.
     *
     * <p>
     * Examples in English: 1 -> "st", 2 -> "nd", 11 -> "th".
     * </p>
     *
     * <p>
     * If the language does not support ordinal suffixes, implementations may return the empty string,
     * but callers should normally consult {@link #supportsOrdinalSuffix()} first.
     * </p>
     */
    String ordinalSuffix(BigInteger value);

    /**
     * Indicates whether the language supports suffix-based ordinal formatting.
     *
     * <p>
     * Examples:
     * English: true
     * Languages without productive ordinal suffixes: false
     * </p>
     */
    boolean supportsOrdinalSuffix();

    // DATETIME FUNCTIONS

    /**
     * Returns an abbreviated weekday name suitable for width-constrained rendering.
     *
     * <p>
     * The meaning of {@code maxWidth} is language-specific but should be interpreted as
     * the maximum desired character width for the abbreviation when possible.
     * </p>
     *
     * <p>
     * Examples in English:
     * Monday -> "Mon",
     * Tuesday -> "Tue" or "Tues" depending on width.
     * </p>
     */
    String dayAbbreviation(DayOfWeek day, int maxWidth);

    /**
     * Returns an abbreviated month name.
     *
     * <p>
     * Examples in English:
     * January -> "Jan", September -> "Sep".
     * </p>
     */
    String monthAbbreviation(Month month);

    /**
     * Returns the full weekday name.
     *
     * <p>
     * Example in English: Monday -> "Monday".
     * </p>
     */
    String dayName(DayOfWeek day);

    /**
     * Returns a weekday name adapted to the given width constraints.
     *
     * <p>
     * If no width was explicitly requested by the caller, {@code maxWidth} may be negative.
     * In that case, implementations should generally return the default full weekday name.
     * </p>
     *
     * <p>
     * When a width constraint is present, implementations may choose either a full or abbreviated
     * form as long as it fits the requested width in a deterministic and language-appropriate way.
     * </p>
     */
    String dayName(DayOfWeek day, int minWidth, int maxWidth);

    /**
     * Returns the full month name.
     *
     * <p>
     * Example in English: January -> "January".
     * </p>
     */
    String monthName(Month month);

    /**
     * Returns a month name adapted to the given width constraints.
     *
     * <p>
     * If no width was explicitly requested by the caller, {@code maxWidth} may be negative.
     * In that case, implementations should generally return the default full month name.
     * </p>
     *
     * <p>
     * When a width constraint is present, implementations may choose either a full or abbreviated
     * form as long as it fits the requested width in a deterministic and language-appropriate way.
     * </p>
     */
    String monthName(Month month, int minWidth, int maxWidth);
}
