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
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.xml.NamespaceBindingUtils;

import java.util.List;

/**
 * {@code fn:QName($paramURI as xs:string?, $paramQName as xs:string) as xs:QName}
 *
 * @see <a href="https://www.w3.org/TR/xpath-functions-31/#func-QName">XPath and XQuery Functions and Operators
 *      3.1</a>
 */
public class QNameFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public QNameFunctionIterator(List<RuntimeIterator> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item uriItem = this.children.get(0).materializeFirstItemOrNull(context);
        String uriString = uriItem == null ? null : uriItem.getStringValue();

        Item lexicalItem = this.children.get(1).materializeFirstItemOrNull(context);
        if (lexicalItem == null) {
            throw new UnexpectedTypeException(
                    "fn:QName: second argument must be xs:string (got empty sequence).",
                    getMetadata()
            );
        }
        String lexicalString = lexicalItem.getStringValue();

        Name expanded = NamespaceBindingUtils.parseFnQName(uriString, lexicalString, getMetadata());
        return ItemFactory.getInstance().createQNameItem(expanded);
    }
}
