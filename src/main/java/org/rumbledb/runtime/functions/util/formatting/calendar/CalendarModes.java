package org.rumbledb.runtime.functions.util.formatting.calendar;


import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class CalendarModes {

    private CalendarModes() {
    }

    // Best-effort mappings from W3C calendar designators to ICU calendar keywords.
    // ICU provides the calendar data. If formatted dates are wrong for
    // these calendars, the ICU calendar may need
    // tuning to specification. This is not exhaustevly tested by the current QT3 test.
    private static final Map<String, String> ICU_TYPES = Map.ofEntries(
        Map.entry("ISO", "gregorian"),
        Map.entry("AD", "gregorian"),
        Map.entry("CE", "gregorian"),
        Map.entry("AH", "islamic"),
        Map.entry("AP", "persian"),
        Map.entry("AM", "hebrew"),
        Map.entry("BE", "buddhist"),
        Map.entry("JE", "japanese"),
        Map.entry("CL", "chinese")
    );

    private static final Set<String> KNOWN_DESIGNATORS = Set.of(
        "AD",
        "AH",
        "AME",
        "AM",
        "AP",
        "AS",
        "BE",
        "CB",
        "CE",
        "CL",
        "CS",
        "EE",
        "FE",
        "ISO",
        "JE",
        "KE",
        "KY",
        "ME",
        "MS",
        "NS",
        "OS",
        "RS",
        "SE",
        "SH",
        "SS",
        "TE",
        "VE",
        "VS"
    );

    private static final Set<String> JAVA_TIME_FIELD_DESIGNATORS = Set.of(
        "ISO",
        "AD",
        "CE"
    );

    private static final String FALLBACK_DESIGNATOR = "AD";
    private static final String FALLBACK_ICU_TYPE = ICU_TYPES.get(FALLBACK_DESIGNATOR);

    public static String toCalendarTypeOrDefault(String designator) {
        if (designator == null) {
            return FALLBACK_ICU_TYPE;
        }
        return ICU_TYPES.getOrDefault(designator.toUpperCase(Locale.ROOT), FALLBACK_ICU_TYPE);
    }

    public static boolean isSupportedByIcu(String designator) {
        if (designator == null) {
            return false;
        }
        return ICU_TYPES.containsKey(designator.toUpperCase(Locale.ROOT));
    }

    public static boolean isKnownDesignator(String designator) {
        if (designator == null) {
            return false;
        }
        return KNOWN_DESIGNATORS.contains(designator.toUpperCase(Locale.ROOT));
    }

    public static String normalizeDesignator(String designator) {
        return designator == null ? null : designator.toUpperCase(Locale.ROOT);
    }

    public static String effectiveDesignatorOrDefault(String designator) {
        if (designator == null) {
            return FALLBACK_DESIGNATOR;
        }

        String normalized = normalizeDesignator(designator);
        return isSupportedByIcu(normalized) ? normalized : FALLBACK_DESIGNATOR;
    }

    public static boolean usesJavaTimeFields(String designator) {
        if (designator == null) {
            return true;
        }
        return JAVA_TIME_FIELD_DESIGNATORS.contains(designator.toUpperCase(Locale.ROOT));
    }

}
