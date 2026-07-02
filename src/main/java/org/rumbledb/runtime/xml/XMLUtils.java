package org.rumbledb.runtime.xml;

public final class XMLUtils {
    private XMLUtils() {
    }

    public static boolean isValidXmlCharacter(int codepoint, String xmlVersion) {
        boolean inCharRange = (0 <= codepoint && codepoint <= 0xD7FF)
            || (0xE000 <= codepoint && codepoint <= 0xFFFD)
            || (0x10000 <= codepoint && codepoint <= 0x10FFFF);
        if (!inCharRange) {
            return false;
        }

        if ("1.0".equals(xmlVersion)) {
            // XML 1.0 forbids the C0 controls except tab, line feed and carriage return.
            if (codepoint < 0x20) {
                return codepoint == 0x09 || codepoint == 0x0A || codepoint == 0x0D;
            }
            return true;
        }

        // XML 1.1 permits every character in the Char range except NUL.
        return codepoint != 0x00;
    }

    public static boolean isControlCharacter(int codepoint) {
        return (codepoint >= 0x00 && codepoint <= 0x1F)
            || (codepoint >= 0x7F && codepoint <= 0x9F);
    }
}
