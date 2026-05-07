package org.rumbledb.runtime.functions.xml;

public final class XMLUtils {
    private XMLUtils() {
    }

    public static boolean isValidCodePoint(int codepoint, String xmlVersion) {
        if (codepoint < 0 || codepoint > 0x10FFFF) {
            return false;
        }

        if (isControlCharacter(codepoint) && !isPermittedControlCharacter(codepoint, xmlVersion)) {
            return false;
        }

        return (codepoint <= 0xD7FF)
            || (0xE000 <= codepoint && codepoint <= 0xFFFD)
            || (0x10000 <= codepoint);
    }

    public static boolean isControlCharacter(int codepoint) {
        return (codepoint >= 0x00 && codepoint <= 0x1F)
            || (codepoint >= 0x7F && codepoint <= 0x9F);
    }

    public static boolean isPermittedControlCharacter(int codepoint, String xmlVersion) {
        if (!isControlCharacter(codepoint)) {
            return false;
        }

        if ("1.0".equals(xmlVersion)) {
            return codepoint == 0x09
                || codepoint == 0x0A
                || codepoint == 0x0D;
        }

        return codepoint != 0x00;
    }

    public static String defaultXMLVersion() {
        return "1.0";
    }
}
