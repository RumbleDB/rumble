/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.compiler.utils;

import java.math.BigInteger;

import org.rumbledb.exceptions.CharacterReferenceException;
import org.rumbledb.exceptions.ExceptionMetadata;

/**
 * Utility for unescaping and validating XML character references and predefined entity references.
 *
 * <p>
 * Enforces W3C XML 1.0 Char production and raises {@code err:XQST0090} ({@link CharacterReferenceException})
 * if a character reference does not expand to a legal XML character or overflows integer range.
 * </p>
 */
public final class XmlCharRefUtils {

    private static final BigInteger MAX_XML_CODEPOINT = BigInteger.valueOf(0x10FFFF);

    private XmlCharRefUtils() {}

    /**
     * Checks if a Unicode code point matches the XML 1.0 Char production:
     * {@code Char ::= #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]}
     *
     * @param cp the code point to validate
     * @return {@code true} if legal XML character, {@code false} otherwise
     */
    public static boolean isValidXmlChar(int cp) {
        return cp == 0x9
                || cp == 0xA
                || cp == 0xD
                || (cp >= 0x20 && cp <= 0xD7FF)
                || (cp >= 0xE000 && cp <= 0xFFFD)
                || (cp >= 0x10000 && cp <= 0x10FFFF);
    }

    /**
     * Unescapes XML character references and predefined entity references in the given text.
     *
     * @param text the input string to unescape
     * @param metadata metadata for error reporting
     * @return the unescaped string
     * @throws CharacterReferenceException [err:XQST0090] if any character reference does not expand to a legal XML
     *         character
     */
    public static String unescapeXml(String text, ExceptionMetadata metadata) {
        if (text == null || text.indexOf('&') < 0) {
            return text;
        }
        StringBuilder sb = new StringBuilder(text.length());
        int len = text.length();
        int i = 0;
        while (i < len) {
            char c = text.charAt(i);
            if (c == '&') {
                int semi = text.indexOf(';', i + 1);
                if (semi > i + 1) {
                    String ref = text.substring(i, semi + 1);
                    if (ref.startsWith("&#")) {
                        sb.append(decodeCharRef(ref, metadata));
                        i = semi + 1;
                        continue;
                    }
                    String entity = text.substring(i + 1, semi);
                    switch (entity) {
                        case "lt":
                            sb.append('<');
                            i = semi + 1;
                            continue;
                        case "gt":
                            sb.append('>');
                            i = semi + 1;
                            continue;
                        case "amp":
                            sb.append('&');
                            i = semi + 1;
                            continue;
                        case "quot":
                            sb.append('"');
                            i = semi + 1;
                            continue;
                        case "apos":
                            sb.append('\'');
                            i = semi + 1;
                            continue;
                        default:
                            break;
                    }
                }
            }
            sb.append(c);
            i++;
        }
        return sb.toString();
    }

    /**
     * Decodes a single character reference (e.g. {@code "&#x20;"} or {@code "&#32;"}), validating that it
     * expands to a legal XML character.
     *
     * @param ref the character reference string including {@code "&#"} and {@code ";"}
     * @param metadata metadata for error reporting
     * @return the decoded character as a string
     * @throws CharacterReferenceException [err:XQST0090] if invalid or out of XML character range
     */
    public static String decodeCharRef(String ref, ExceptionMetadata metadata) {
        if (!ref.startsWith("&#") || !ref.endsWith(";")) {
            throw new CharacterReferenceException("Malformed character reference: " + ref, metadata);
        }
        boolean isHex = ref.startsWith("&#x") || ref.startsWith("&#X");
        int prefixLen = isHex ? 3 : 2;
        String digits = ref.substring(prefixLen, ref.length() - 1);
        if (digits.isEmpty()) {
            throw new CharacterReferenceException("Empty character reference: " + ref, metadata);
        }
        int radix = isHex ? 16 : 10;
        final BigInteger val;
        try {
            val = new BigInteger(digits, radix);
        } catch (NumberFormatException e) {
            throw new CharacterReferenceException("Invalid integer in character reference: " + ref, metadata);
        }
        if (val.compareTo(BigInteger.ZERO) < 0
                || val.compareTo(MAX_XML_CODEPOINT) > 0
                || !isValidXmlChar(val.intValue())) {
            throw new CharacterReferenceException(
                    "Character reference " + ref + " does not expand to a legal XML character.", metadata);
        }
        return new String(Character.toChars(val.intValue()));
    }
}
