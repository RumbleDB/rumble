package org.rumbledb.runtime.functions.util.formatting;

import com.ibm.icu.number.NumberFormatter;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.ibm.icu.util.ULocale;

import java.math.BigInteger;
import java.util.Locale;

public final class NumberWords {
    // For plain ordinal word formatting (`Ww;o`) RumbleDB selects the ICU masculine ordinal rule set when available,
    // with a small set of aliases for ICU's abbreviated rule-set names. This is implementation-defined behavior
    // permitted by F&O 3.1 and matches the specification's example outputs.
    private static final String DEFAULT_ORDINAL_WORD_RULE_SET = "%spellout-ordinal-masculine";

    private NumberWords() {
    }

    public static String cardinal(long value, ULocale locale, String requestedRuleSet) {
        RuleBasedNumberFormat f = new RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT);
        String formatted = formatWithRequestedRuleSet(value, f, requestedRuleSet);
        if (formatted != null) {
            return formatted;
        }

        return f.format(value);
    }

    public static String ordinalDigits(long value, ULocale locale) {
        RuleBasedNumberFormat f = new RuleBasedNumberFormat(locale, RuleBasedNumberFormat.ORDINAL);
        return f.format(value);
    }

    public static String ordinalWords(long value, ULocale locale, String requestedRuleSet) {
        RuleBasedNumberFormat f = new RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT);

        String formatted = formatWithRequestedRuleSet(value, f, requestedRuleSet);
        if (formatted != null) {
            return formatted;
        }

        if (requestedRuleSet == null) {
            formatted = formatWithRequestedRuleSet(value, f, DEFAULT_ORDINAL_WORD_RULE_SET);
            if (formatted != null) {
                return formatted;
            }
        }

        String requestedSuffix = requestedSuffix(requestedRuleSet);
        if (requestedSuffix != null) {
            formatted = ordinalWordWithSuffix(value, f, requestedSuffix);
            if (formatted != null) {
                return formatted;
            }
        }

        String ruleSet = neutralOrdinalRuleSet(f);
        if (ruleSet != null) {
            return f.format(value, ruleSet);
        }

        return f.format(value);
    }

    public static String roman(long value, boolean lowerCase) {
        RuleBasedNumberFormat f =
            new RuleBasedNumberFormat(ULocale.ROOT, RuleBasedNumberFormat.NUMBERING_SYSTEM);
        return f.format(value, lowerCase ? "%roman-lower" : "%roman-upper");
    }

    private static String formatWithRequestedRuleSet(
            long value,
            RuleBasedNumberFormat f,
            String requestedRuleSet
    ) {
        if (requestedRuleSet == null || !requestedRuleSet.startsWith("%")) {
            return null;
        }

        try {
            return f.format(value, requestedRuleSet);
        } catch (IllegalArgumentException e) {
            // try aliases below
        }

        String alias = requestedRuleSetAlias(requestedRuleSet);
        if (alias != null) {
            try {
                return f.format(value, alias);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        return null;
    }

    private static String requestedRuleSetAlias(String requestedRuleSet) {
        if (requestedRuleSet.endsWith("-masculine")) {
            return replaceRuleSetSuffix(requestedRuleSet, "-masculine", "-r");
        }

        if (requestedRuleSet.endsWith("-feminine")) {
            return replaceRuleSetSuffix(requestedRuleSet, "-feminine", "");
        }

        if (requestedRuleSet.endsWith("-neuter")) {
            return replaceRuleSetSuffix(requestedRuleSet, "-neuter", "-s");
        }

        return null;
    }

    private static String replaceRuleSetSuffix(String ruleSet, String oldSuffix, String newSuffix) {
        return ruleSet.substring(0, ruleSet.length() - oldSuffix.length()) + newSuffix;
    }

    private static String ordinalWordWithSuffix(
            long value,
            RuleBasedNumberFormat f,
            String requestedSuffix
    ) {
        for (String ruleSet : f.getRuleSetNames()) {
            if (!ruleSet.contains("spellout-ordinal")) {
                continue;
            }

            String formatted = f.format(value, ruleSet);

            if (formatted.toLowerCase(Locale.ROOT).endsWith(requestedSuffix)) {
                return formatted;
            }
        }

        return null;
    }

    private static String neutralOrdinalRuleSet(RuleBasedNumberFormat f) {
        String ruleSet = findRuleSet(f, "%spellout-ordinal");
        if (ruleSet != null) {
            return ruleSet;
        }

        ruleSet = findOrdinalRuleSetContaining(f, "neutral");
        if (ruleSet != null) {
            return ruleSet;
        }

        ruleSet = findOrdinalRuleSetContaining(f, "neuter");
        if (ruleSet != null) {
            return ruleSet;
        }

        ruleSet = findOrdinalRuleSetContaining(f, "common");
        if (ruleSet != null) {
            return ruleSet;
        }

        return null;
    }

    private static String findRuleSet(RuleBasedNumberFormat f, String requestedName) {
        for (String ruleSet : f.getRuleSetNames()) {
            if (ruleSet.equals(requestedName)) {
                return ruleSet;
            }
        }

        return null;
    }

    private static String findOrdinalRuleSetContaining(RuleBasedNumberFormat f, String text) {
        for (String ruleSet : f.getRuleSetNames()) {
            String lowerCaseRuleSet = ruleSet.toLowerCase(Locale.ROOT);

            if (
                lowerCaseRuleSet.contains("spellout-ordinal")
                    && lowerCaseRuleSet.contains(text)
            ) {
                return ruleSet;
            }
        }

        return null;
    }

    public static String ordinalSuffix(BigInteger value, ULocale locale) {
        if (value.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
            return "";
        }

        int intValue = value.intValueExact();
        long longValue = intValue;
        String ordinal = ordinalDigits(longValue, locale);

        String localizedDigits = NumberFormatter.withLocale(locale)
            .grouping(NumberFormatter.GroupingStrategy.AUTO)
            .format(longValue)
            .toString();
        if (ordinal.startsWith(localizedDigits)) {
            return ordinal.substring(localizedDigits.length());
        }

        String asciiDigits = value.toString();
        if (ordinal.startsWith(asciiDigits)) {
            return ordinal.substring(asciiDigits.length());
        }

        return "";
    }

    private static String requestedSuffix(String requestedRuleSet) {
        if (requestedRuleSet == null || requestedRuleSet.isEmpty() || requestedRuleSet.startsWith("%")) {
            return null;
        }

        String suffix = requestedRuleSet.startsWith("-")
            ? requestedRuleSet.substring(1)
            : requestedRuleSet;

        return suffix.isEmpty() ? null : suffix.toLowerCase(Locale.ROOT);
    }
}
