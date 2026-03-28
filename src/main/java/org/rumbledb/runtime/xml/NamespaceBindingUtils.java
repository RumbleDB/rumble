/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
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
 *
 * Authors: Matteo Agnoletto (EPMatt)
 *
 */

package org.rumbledb.runtime.xml;

import java.util.Map;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidLexicalValueException;
import org.rumbledb.exceptions.PredefinedPrefixInNamespaceDeclarationException;

public final class NamespaceBindingUtils {

    /**
     * Resolves a namespace prefix (including {@code ""} for the default element/type namespace) to a URI,
     * or {@code null} if unbound.
     */
    @FunctionalInterface
    public interface NamespaceResolver {
        String resolvePrefix(String prefix);
    }

    public enum ReservedNamespaceBindingError {
        XML_PREFIX_WRONG_URI,
        XMLNS_PREFIX,
        NON_XML_PREFIX_XML_URI,
        XMLNS_URI
    }

    public static final String XML_NAMESPACE_URI = "http://www.w3.org/XML/1998/namespace";
    public static final String XMLNS_NAMESPACE_URI = "http://www.w3.org/2000/xmlns/";

    private NamespaceBindingUtils() {
    }

    /**
     * XML 1.0 / Namespaces in XML — NCName character checks (no colon).
     */
    public static boolean isValidNcName(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        int len = s.length();
        int i = 0;
        int cp = s.codePointAt(0);
        if (!isXmlNameStartChar(cp) || cp == ':') {
            return false;
        }
        i += Character.charCount(cp);
        while (i < len) {
            cp = s.codePointAt(i);
            if (!isXmlNameChar(cp) || cp == ':') {
                return false;
            }
            i += Character.charCount(cp);
        }
        return true;
    }

    private static boolean isXmlNameStartChar(int c) {
        return c == ':'
            || c == '_'
            || isAsciiLetter(c)
            || (c >= 0xC0 && c <= 0xD6)
            || (c >= 0xD8 && c <= 0xF6)
            || (c >= 0xF8 && c <= 0x2FF)
            || (c >= 0x370 && c <= 0x37D)
            || (c == 0x37F)
            || (c >= 0x200C && c <= 0x200D)
            || (c >= 0x2070 && c <= 0x218F)
            || (c >= 0x2C00 && c <= 0x2FEF)
            || (c >= 0x3001 && c <= 0xD7FF)
            || (c >= 0xF900 && c <= 0xFDCF)
            || (c >= 0xFDF0 && c <= 0xFFFD)
            || (c >= 0x10000 && c <= 0xEFFFF);
    }

    private static boolean isXmlNameChar(int c) {
        return isXmlNameStartChar(c)
            || c == '-'
            || c == '.'
            || (c >= '0' && c <= '9')
            || c == 0xB7
            || (c >= 0x0300 && c <= 0x036F)
            || (c >= 0x203F && c <= 0x2040);
    }

    private static boolean isAsciiLetter(int c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    public static NamespaceResolver builtinNamespaceResolver() {
        return StaticContext::getBuiltinNamespaceBinding;
    }

    public static NamespaceResolver namespaceResolver(RuntimeStaticContext runtimeStaticContext) {
        return runtimeStaticContext != null ? runtimeStaticContext::resolvePrefix : builtinNamespaceResolver();
    }

    public static NamespaceResolver namespaceResolver(StaticContext staticContext) {
        if (staticContext == null) {
            return builtinNamespaceResolver();
        }
        Map<String, String> inScope = staticContext.getInScopeNamespaceBindings();
        return prefix -> {
            if (inScope.containsKey(prefix)) {
                return inScope.get(prefix);
            }
            return StaticContext.getBuiltinNamespaceBinding(prefix);
        };
    }

    /**
     * Whitespace-collapsed lexical QName to expanded name (xs:QName cast / constructor).
     */
    public static Name parseLexicalQName(
            String lexical,
            NamespaceResolver namespaceResolver,
            ExceptionMetadata metadata
    ) {
        if (lexical.isEmpty()) {
            throw new InvalidLexicalValueException("Invalid xs:QName: empty lexical value.", metadata);
        }
        int colon = lexical.indexOf(':');
        final String prefix;
        final String local;
        if (colon < 0) {
            prefix = null;
            local = lexical;
        } else {
            if (colon == 0 || colon == lexical.length() - 1 || lexical.indexOf(':', colon + 1) >= 0) {
                throw new InvalidLexicalValueException(
                        "Invalid xs:QName lexical value: \"" + lexical + "\".",
                        metadata
                );
            }
            prefix = lexical.substring(0, colon);
            local = lexical.substring(colon + 1);
        }
        if ("xmlns".equals(prefix)) {
            throw new InvalidLexicalValueException("Invalid xs:QName: prefix xmlns is not allowed.", metadata);
        }
        if (!isValidNcName(local) || (prefix != null && !isValidNcName(prefix))) {
            throw new InvalidLexicalValueException(
                    "Invalid xs:QName lexical value: name is not a valid NCName.",
                    metadata
            );
        }
        if (prefix == null) {
            return new Name(namespaceResolver.resolvePrefix(""), null, local);
        }
        String uri = namespaceResolver.resolvePrefix(prefix);
        if (uri == null) {
            throw new InvalidLexicalValueException(
                    "Invalid xs:QName: prefix \"" + prefix + "\" is not bound to a namespace URI.",
                    metadata
            );
        }
        if (getReservedNamespaceBindingError(prefix, uri) != null) {
            throw new InvalidLexicalValueException(
                    "Invalid xs:QName lexical value: reserved namespace binding for prefix \"" + prefix + "\".",
                    metadata
            );
        }
        return new Name(uri, prefix, local);
    }

    public static String[] parseNamespaceDeclarationAttribute(Item attributeItem) {
        if (!attributeItem.isAttributeNode()) {
            return null;
        }
        String attributeName = attributeItem.nodeName();
        if ("xmlns".equals(attributeName)) {
            return new String[] { "", attributeItem.getStringValue() };
        }
        if (attributeName.startsWith("xmlns:")) {
            String prefix = attributeName.substring("xmlns:".length());
            return new String[] { prefix, attributeItem.getStringValue() };
        }
        return null;
    }

    public static ReservedNamespaceBindingError getReservedNamespaceBindingError(String prefix, String uri) {
        if ("xml".equals(prefix) && !XML_NAMESPACE_URI.equals(uri)) {
            return ReservedNamespaceBindingError.XML_PREFIX_WRONG_URI;
        }
        if ("xmlns".equals(prefix)) {
            return ReservedNamespaceBindingError.XMLNS_PREFIX;
        }
        if (!"xml".equals(prefix) && XML_NAMESPACE_URI.equals(uri)) {
            return ReservedNamespaceBindingError.NON_XML_PREFIX_XML_URI;
        }
        if (XMLNS_NAMESPACE_URI.equals(uri)) {
            return ReservedNamespaceBindingError.XMLNS_URI;
        }
        return null;
    }

    public static void validateNamespaceDeclaration(String prefix, String uri) {
        // XQuery 3.1, 3.9.1.2 Namespace Declaration Attributes:
        // "However, note that namespace declaration attributes (see 3.9.1.2 Namespace Declaration Attributes) do not
        // create attribute nodes."
        // "[Definition: A namespace declaration attribute is used inside a direct element constructor. Its purpose is
        // to bind a namespace prefix or to set the default element/type namespace for the constructed element node,
        // including its attributes.] Syntactically, a namespace declaration attribute has the form of an attribute
        // with namespace prefix xmlns, or with name xmlns and no namespace prefix."
        // "If the prefix of the attribute name is xmlns, then the local part of the attribute name is interpreted as a
        // namespace prefix."
        // "If the name of the namespace declaration attribute is xmlns with no prefix, then the namespace URI specifies
        // the default element/type namespace of the constructor expression (overriding any existing default), and is
        // added (with no prefix) to the in-scope namespaces of the constructed element (overriding any existing
        // namespace binding with no prefix)."
        // "It is a static error [err:XQST0070] if a namespace declaration attribute attempts to do any of the
        // following:"
        // "Bind the prefix xml to some namespace URI other than http://www.w3.org/XML/1998/namespace."
        // "Bind the prefix xmlns to any namespace URI."
        // "Bind a prefix other than xml to the namespace URI http://www.w3.org/XML/1998/namespace."
        // "Bind a prefix to the namespace URI http://www.w3.org/2000/xmlns/."
        ReservedNamespaceBindingError error = getReservedNamespaceBindingError(prefix, uri);
        if (error == null) {
            return;
        }
        switch (error) {
            case XML_PREFIX_WRONG_URI:
                throw new PredefinedPrefixInNamespaceDeclarationException(
                        "Namespace declaration attribute cannot bind the prefix xml to a non-XML namespace URI."
                );
            case XMLNS_PREFIX:
                throw new PredefinedPrefixInNamespaceDeclarationException(
                        "Namespace declaration attribute cannot bind the prefix xmlns."
                );
            case NON_XML_PREFIX_XML_URI:
                throw new PredefinedPrefixInNamespaceDeclarationException(
                        "Namespace declaration attribute cannot bind a non-xml prefix to the XML namespace URI."
                );
            case XMLNS_URI:
                throw new PredefinedPrefixInNamespaceDeclarationException(
                        "Namespace declaration attribute cannot bind any prefix to the xmlns namespace URI."
                );
            default:
                return;
        }
        // TODO: handle binding a prefix to a zero-length namespace URI
    }
}

