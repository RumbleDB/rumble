package org.rumbledb.runtime.functions.util.formatting;

import com.ibm.icu.number.LocalizedNumberFormatter;
import com.ibm.icu.number.NumberFormatter;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.ibm.icu.util.ULocale;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class NumberWords {
    // For plain ordinal word formatting (`Ww;o`) RumbleDB selects the ICU masculine ordinal rule set when available,
    // with a small set of aliases for ICU's abbreviated rule-set names. This is implementation-defined behavior
    // permitted by F&O 3.1 and matches the specification's example outputs.
    private static final String DEFAULT_ORDINAL_WORD_RULE_SET = "%spellout-ordinal-masculine";

    private static final ThreadLocal<Map<RuleFormatKey, CachedRuleFormat>> RULE_FORMAT_CACHE = ThreadLocal.withInitial(
        HashMap::new
    );

    private static final Map<ULocale, LocalizedNumberFormatter> GROUPING_FORMATTER_CACHE = new ConcurrentHashMap<>();

    private NumberWords() {
    }

    public static String cardinal(long value, ULocale locale, String requestedRuleSet) {
        CachedRuleFormat f = ruleFormat(locale, RuleBasedNumberFormat.SPELLOUT);
        String formatted = formatWithRequestedRuleSet(value, f, requestedRuleSet);
        if (formatted != null) {
            return formatted;
        }

        return f.format.format(value);
    }

    public static String ordinalDigits(long value, ULocale locale) {
        CachedRuleFormat f = ruleFormat(locale, RuleBasedNumberFormat.ORDINAL);
        return f.format.format(value);
    }

    public static String ordinalWords(long value, ULocale locale, String requestedRuleSet) {
        CachedRuleFormat f = ruleFormat(locale, RuleBasedNumberFormat.SPELLOUT);

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
            return f.format.format(value, ruleSet);
        }

        return f.format.format(value);
    }

    public static String roman(long value, boolean lowerCase) {
        CachedRuleFormat f = ruleFormat(ULocale.ROOT, RuleBasedNumberFormat.NUMBERING_SYSTEM);
        return f.format.format(value, lowerCase ? "%roman-lower" : "%roman-upper");
    }

    private static LocalizedNumberFormatter groupingFormatter(ULocale locale) {
        return GROUPING_FORMATTER_CACHE.computeIfAbsent(
            locale,
            l -> NumberFormatter.withLocale(l).grouping(NumberFormatter.GroupingStrategy.AUTO)
        );
    }

    private static CachedRuleFormat ruleFormat(ULocale locale, int ruleType) {
        Map<RuleFormatKey, CachedRuleFormat> cache = RULE_FORMAT_CACHE.get();
        return cache.computeIfAbsent(
            new RuleFormatKey(locale, ruleType),
            key -> {
                RuleBasedNumberFormat f = new RuleBasedNumberFormat(key.locale, key.ruleType);
                List<String> ruleSetNames = Arrays.asList(f.getRuleSetNames());
                return new CachedRuleFormat(f, ruleSetNames, new HashSet<>(ruleSetNames));
            }
        );
    }

    private static final class CachedRuleFormat {
        private final RuleBasedNumberFormat format;
        private final List<String> ruleSetNamesInOrder;
        private final Set<String> ruleSetNames;

        private CachedRuleFormat(
                RuleBasedNumberFormat format,
                List<String> ruleSetNamesInOrder,
                Set<String> ruleSetNames
        ) {
            this.format = format;
            this.ruleSetNamesInOrder = ruleSetNamesInOrder;
            this.ruleSetNames = ruleSetNames;
        }
    }

    private static final class RuleFormatKey {
        private final ULocale locale;
        private final int ruleType;

        private RuleFormatKey(ULocale locale, int ruleType) {
            this.locale = locale;
            this.ruleType = ruleType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof RuleFormatKey)) {
                return false;
            }
            RuleFormatKey other = (RuleFormatKey) o;
            return this.ruleType == other.ruleType && this.locale.equals(other.locale);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.locale, this.ruleType);
        }
    }

    private static String formatWithRequestedRuleSet(
            long value,
            CachedRuleFormat f,
            String requestedRuleSet
    ) {
        if (requestedRuleSet == null || !requestedRuleSet.startsWith("%")) {
            return null;
        }

        if (f.ruleSetNames.contains(requestedRuleSet)) {
            return f.format.format(value, requestedRuleSet);
        }

        String alias = requestedRuleSetAlias(requestedRuleSet);
        if (alias != null && f.ruleSetNames.contains(alias)) {
            return f.format.format(value, alias);
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
            CachedRuleFormat f,
            String requestedSuffix
    ) {
        for (String ruleSet : f.ruleSetNamesInOrder) {
            if (!ruleSet.contains("spellout-ordinal")) {
                continue;
            }

            String formatted = f.format.format(value, ruleSet);

            if (formatted.toLowerCase(Locale.ROOT).endsWith(requestedSuffix)) {
                return formatted;
            }
        }

        return null;
    }

    private static String neutralOrdinalRuleSet(CachedRuleFormat f) {
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

    private static String findRuleSet(CachedRuleFormat f, String requestedName) {
        return f.ruleSetNames.contains(requestedName) ? requestedName : null;
    }

    private static String findOrdinalRuleSetContaining(CachedRuleFormat f, String text) {
        for (String ruleSet : f.ruleSetNamesInOrder) {
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

        String localizedDigits = groupingFormatter(locale).format(longValue).toString();
        if (ordinal.startsWith(localizedDigits)) {
            return ordinal.substring(localizedDigits.length());
        }

        String asciiDigits = Integer.toString(intValue);
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
