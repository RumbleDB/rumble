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

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.PredefinedPrefixInNamespaceDeclarationException;

public final class NamespaceBindingUtils {

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

