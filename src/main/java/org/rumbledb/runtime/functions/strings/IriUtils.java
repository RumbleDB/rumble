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
package org.rumbledb.runtime.functions.strings;

import java.nio.charset.StandardCharsets;

/**
 * Utility methods for IRI to URI conversions according to RFC 3987 and W3C F&amp;O 3.1.
 */
public final class IriUtils {

    private IriUtils() {}

    /**
     * Converts an IRI to a URI according to RFC 3987 and W3C fn:iri-to-uri.
     * Characters that are not permitted in URIs (such as non-ASCII characters and certain delimiters)
     * are percent-encoded using their UTF-8 byte representation.
     *
     * @param value The IRI string to encode.
     * @return The resulting URI string.
     */
    public static String encodeIri(String value) {
        if (value == null) {
            return null;
        }
        StringBuilder result = new StringBuilder(value.length());
        value.codePoints().forEach(codePoint -> appendEncodedCodePoint(result, codePoint));
        return result.toString();
    }

    private static void appendEncodedCodePoint(StringBuilder result, int codePoint) {
        if (!mustEscape(codePoint)) {
            result.appendCodePoint(codePoint);
            return;
        }

        byte[] utf8Bytes = new String(Character.toChars(codePoint)).getBytes(StandardCharsets.UTF_8);
        for (byte currentByte : utf8Bytes) {
            result.append('%');
            int unsigned = currentByte & 0xFF;
            char high = Character.toUpperCase(Character.forDigit((unsigned >>> 4) & 0xF, 16));
            char low = Character.toUpperCase(Character.forDigit(unsigned & 0xF, 16));
            result.append(high);
            result.append(low);
        }
    }

    private static boolean mustEscape(int codePoint) {
        if (codePoint < 0x20 || codePoint > 0x7E) {
            return true;
        }
        return switch (codePoint) {
            case ' ', '"', '<', '>', '\\', '^', '`', '{', '|', '}' -> true;
            default -> false;
        };
    }
}
