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
package org.rumbledb.runtime.xml;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import org.rumbledb.runtime.functions.strings.IriUtils;

/**
 * Utility methods for resolving XML Base URIs according to W3C XML Base (Second Edition)
 * and W3C XDM 3.1.
 */
public final class XmlBaseUtils {

    private static final Pattern ABSOLUTE_URI_SCHEME = Pattern.compile("^[A-Za-z][A-Za-z0-9+.-]*:");

    private XmlBaseUtils() {}

    /**
     * Resolves an {@code xml:base} attribute value against an inherited base URI.
     *
     * @param base The inherited absolute base URI (from parent or construction context), or null.
     * @param value The value of the {@code xml:base} attribute.
     * @return The resolved absolute base URI string, or null if it cannot be resolved to an absolute URI.
     */
    public static String resolve(String base, String value) {
        if (value == null || value.isEmpty()) {
            return base;
        }

        // If value has an explicit scheme, it is an absolute URI/LEIRI
        if (ABSOLUTE_URI_SCHEME.matcher(value).find()) {
            URI uri = safeParse(value);
            return uri != null && uri.isAbsolute() ? decodeIri(uri.normalize()) : null;
        }

        // A relative URI cannot be resolved without an absolute base URI
        if (base == null) {
            return null;
        }

        URI baseUri = safeParse(base);
        URI valUri = safeParse(value);
        if (baseUri == null || valUri == null || !baseUri.isAbsolute()) {
            return null;
        }

        URI resolved = baseUri.resolve(valUri).normalize();
        return resolved.isAbsolute() ? decodeIri(resolved) : null;
    }

    private static URI safeParse(String str) {
        try {
            return URI.create(str);
        } catch (IllegalArgumentException e) {
            try {
                return URI.create(IriUtils.encodeIri(str));
            } catch (IllegalArgumentException ex) {
                return null;
            }
        }
    }

    private static String decodeIri(URI uri) {
        return URLDecoder.decode(uri.toString().replace("+", "%2B"), StandardCharsets.UTF_8);
    }
}
