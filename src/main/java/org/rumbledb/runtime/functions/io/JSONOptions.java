package org.rumbledb.runtime.functions.io;

import java.util.function.Function;

public class JSONOptions {
    public static final String DUPLICATES_REJECT = "reject";
    public static final String DUPLICATES_USE_FIRST = "use-first";
    public static final String DUPLICATES_USE_LAST = "use-last";

    // Default Values as per W3C xpath-functions-31 specification, section 17.5.1
    public static final boolean DEFAULT_LIBERAL = false;
    public static final String DEFAULT_DUPLICATES = DUPLICATES_USE_FIRST;
    public static final boolean DEFAULT_ESCAPE = true;
    public static final Function<String, String> DEFAULT_FALLBACK = s -> "\uFFFD";

    private final boolean liberal;
    private final String duplicates;
    private final boolean escape;
    private final Function<String, String> fallback;

    public JSONOptions(boolean liberal, String duplicates, boolean escape, Function<String, String> fallback) {
        this.liberal = liberal;
        this.duplicates = duplicates;
        this.escape = escape;
        this.fallback = fallback;
    }

    public static JSONOptions defaultInstance() {
        return new JSONOptions(DEFAULT_LIBERAL, DEFAULT_DUPLICATES, DEFAULT_ESCAPE, DEFAULT_FALLBACK);
    }

    public boolean isLiberal() {
        return this.liberal;
    }

    public String getDuplicates() {
        return this.duplicates;
    }

    public boolean isEscape() {
        return this.escape;
    }

    public Function<String, String> getFallback() {
        return this.fallback;
    }
}
