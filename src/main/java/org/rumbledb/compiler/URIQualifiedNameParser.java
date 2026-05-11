/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rumbledb.compiler;

import org.apache.commons.text.StringEscapeUtils;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.exceptions.PredefinedPrefixInNamespaceDeclarationException;

/**
 * Parses XQuery 3.1 {@code URIQualifiedName} tokens ({@code BracedURILiteral NCName}) into expanded {@link Name}
 * values. See <a href="https://www.w3.org/TR/xquery-31/#doc-xquery31-URIQualifiedName">XQuery 3.1</a>.
 */
public final class URIQualifiedNameParser {

    public static final String XMLNS_NAMESPACE_URI = "http://www.w3.org/2000/xmlns/";

    private URIQualifiedNameParser() {
    }

    /**
     * @param tokenText full lexer text of one {@code URIQualifiedName} token, e.g. {@code Q{http://ex}invoice}
     * @param metadata for errors
     * @return expanded name with absent prefix (per spec)
     */
    public static Name parse(String tokenText, ExceptionMetadata metadata) {
        if (tokenText == null || tokenText.length() < 4 || !tokenText.startsWith("Q{")) {
            throw new ParsingException("Invalid URIQualifiedName: " + tokenText, metadata);
        }
        int closeBrace = tokenText.indexOf('}', 2);
        if (closeBrace < 0) {
            throw new ParsingException("Invalid URIQualifiedName (no closing '}') : " + tokenText, metadata);
        }
        String uriRaw = tokenText.substring(2, closeBrace);
        String localName = tokenText.substring(closeBrace + 1);
        if (localName.isEmpty()) {
            throw new ParsingException("Invalid URIQualifiedName (missing local name): " + tokenText, metadata);
        }
        String uriUnescaped = StringEscapeUtils.unescapeXml(uriRaw);
        String namespaceUri = normalizeUriForEqName(uriUnescaped);
        if (namespaceUri != null && XMLNS_NAMESPACE_URI.equals(namespaceUri)) {
            throw new PredefinedPrefixInNamespaceDeclarationException(
                    "It is a static error [err:XQST0070] if the namespace URI for an EQName is "
                        + XMLNS_NAMESPACE_URI
                        + ".",
                    metadata
            );
        }
        return new Name(namespaceUri, null, localName);
    }

    /**
     * Whitespace normalization in the spirit of {@code xs:anyURI} (collapse, trim). Empty content after processing
     * means absent namespace URI (e.g. {@code Q{}local}).
     */
    static String normalizeUriForEqName(String uri) {
        if (uri == null) {
            return null;
        }
        String collapsed = uri.trim().replaceAll("\\s+", " ");
        return collapsed.isEmpty() ? null : collapsed;
    }
}
