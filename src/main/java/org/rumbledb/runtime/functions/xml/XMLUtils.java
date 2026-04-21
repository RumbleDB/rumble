package org.rumbledb.runtime.functions.xml;

public final class XMLUtils {
    private XMLUtils() {
    }

    public static boolean isValidCodePoint(int codepoint, String xmlVersion) {
        if (codepoint < 0 || codepoint > 1114111)
            return false;

        boolean isC0 = (codepoint >= 0 && codepoint <= 31);
        boolean isC1 = (codepoint >= 127 && codepoint <= 159);

        if (isC0 || isC1) {
            if (xmlVersion.equals("1.0")) {
                return codepoint == 9 || codepoint == 10 || codepoint == 13;
            } else {
                return codepoint != 0;
            }
        }

        return (codepoint <= 55295)
            || (57344 <= codepoint && codepoint <= 65533)
            || (65536 <= codepoint);
    }

    public static String defaultXMLVersion() {
        return "1.0";
    }
}
