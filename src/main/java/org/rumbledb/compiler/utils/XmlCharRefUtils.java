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

import org.rumbledb.config.model.SemanticsConfig;
import org.rumbledb.exceptions.CharacterReferenceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.xml.XMLUtils;

/**
 * Utility for unescaping and validating XML character references and predefined entity references.
 *
 * <p>
 * Enforces W3C XML 1.0 / 1.1 Char production and raises {@code err:XQST0090} ({@link CharacterReferenceException})
 * if a character reference does not expand to a legal XML character or overflows integer range.
 * </p>
 */
public final class XmlCharRefUtils {

    private static final BigInteger MAX_XML_CODEPOINT = BigInteger.valueOf(0x10FFFF);

    private XmlCharRefUtils() {}

    /**
     * Unescapes XML character references and predefined entity references in the given text using the specified XML
     * version.
     *
     * @param text the input string to unescape
     * @param xmlVersion the XML version ("1.0" or "1.1")
     * @param metadata metadata for error reporting
     * @return the unescaped string
     * @throws CharacterReferenceException [err:XQST0090] if any character reference does not expand to a legal XML
     *         character
     */
    public static String unescapeXml(String text, String xmlVersion, ExceptionMetadata metadata) {
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
                        sb.append(decodeCharRef(ref, xmlVersion, metadata));
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

    private static String decodeCharRef(String ref, String xmlVersion, ExceptionMetadata metadata) {
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
        String version = xmlVersion != null ? xmlVersion : SemanticsConfig.DEFAULT_XML_VERSION;
        if (val.compareTo(BigInteger.ZERO) < 0
                || val.compareTo(MAX_XML_CODEPOINT) > 0
                || !XMLUtils.isValidXmlCharacter(val.intValue(), version)) {
            throw new CharacterReferenceException(
                    "Character reference " + ref + " does not expand to a legal XML character.", metadata);
        }
        return new String(Character.toChars(val.intValue()));
    }
}
