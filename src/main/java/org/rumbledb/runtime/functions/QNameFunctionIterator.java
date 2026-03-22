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
 */

package org.rumbledb.runtime.functions;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.items.QNameItem;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

/**
 * fn:QName($paramURI as xs:string?, $paramQName as xs:string) as xs:QName
 *
 * @see <a href="https://www.w3.org/TR/xpath-functions-31/#func-QName">XPath and XQuery Functions and Operators 3.1</a>
 */
public class QNameFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public QNameFunctionIterator(List<RuntimeIterator> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item uriItem = this.children.get(0).materializeFirstItemOrNull(context);
        String namespaceUri;
        if (uriItem == null) {
            namespaceUri = null;
        } else {
            namespaceUri = uriItem.getStringValue();
            if (namespaceUri.isEmpty()) {
                namespaceUri = null;
            }
        }

        Item lexicalItem = this.children.get(1).materializeFirstItemOrNull(context);
        if (lexicalItem == null) {
            throw new CastException(
                    "Invalid value for QName: empty sequence where xs:string is required",
                    getMetadata()
            );
        }
        String lexical = lexicalItem.getStringValue();

        String prefix;
        String local;
        int colon = lexical.indexOf(':');
        if (colon >= 0) {
            if (colon == 0 || colon == lexical.length() - 1 || lexical.indexOf(':', colon + 1) >= 0) {
                throw new CastException("Invalid lexical QName: \"" + lexical + "\"", getMetadata());
            }
            prefix = lexical.substring(0, colon);
            local = lexical.substring(colon + 1);
        } else {
            prefix = null;
            local = lexical;
        }

        if (!isValidNCName(local) || (prefix != null && !isValidNCName(prefix))) {
            throw new CastException("Invalid lexical QName: \"" + lexical + "\"", getMetadata());
        }

        if (prefix != null && namespaceUri == null) {
            throw new CastException(
                    "Invalid QName: namespace URI is absent but a prefix is present in \"" + lexical + "\"",
                    getMetadata()
            );
        }

        Name name;
        if (namespaceUri == null) {
            name = new Name(null, null, local);
        } else {
            String p = prefix != null ? prefix : "";
            name = new Name(namespaceUri, p, local);
        }
        return new QNameItem(name);
    }

    private static boolean isValidNCName(String s) {
        if (s == null || s.isEmpty() || s.indexOf(':') >= 0) {
            return false;
        }
        if (!Character.isLetter(s.charAt(0)) && s.charAt(0) != '_') {
            return false;
        }
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!(Character.isLetterOrDigit(c) || c == '.' || c == '-' || c == '_')) {
                return false;
            }
        }
        return true;
    }
}
