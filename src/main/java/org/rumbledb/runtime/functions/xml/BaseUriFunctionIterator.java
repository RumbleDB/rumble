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
package org.rumbledb.runtime.functions.xml;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.cursor.ContextOrArgumentLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

/**
 * Implementation of the fn:base-uri function according to XPath and XQuery Functions and Operators 3.1
 * ({@code https://www.w3.org/TR/xpath-functions-31/#func-base-uri}) and the XDM 3.1 base-uri accessor.
 *
 * XDM 3.1 Section 5.2 base-uri Accessor.
 *
 * dm:base-uri($n as node()) as xs:anyURI?
 *
 * "The dm:base-uri accessor returns the value of the base-uri property of the node, if it
 * has one; otherwise it returns the empty sequence."
 *
 * Function signatures (Functions and Operators 3.1, {@code fn:base-uri}):
 *
 * <ul>
 * <li>fn:base-uri() as xs:anyURI?</li>
 * <li>fn:base-uri($arg as node()?) as xs:anyURI?</li>
 * </ul>
 *
 * Rules:
 *
 * <ul>
 * <li>If the argument is omitted, it defaults to the context item (.).</li>
 * <li>If the argument is supplied and is the empty sequence, the function returns the empty sequence.</li>
 * <li>Otherwise, the function returns dm:base-uri($arg).</li>
 * </ul>
 *
 * @see <a href="https://www.w3.org/TR/xpath-functions-31/#func-base-uri">XPath and XQuery Functions and
 *      Operators 3.1: fn:base-uri</a>
 */
public class BaseUriFunctionIterator extends LocalFunctionCallIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public BaseUriFunctionIterator(List<ItemRuntimePlan> parameters, RuntimeStaticContext staticContext) {
        super(parameters, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return ContextOrArgumentLocalCursor.flatMapFirstArgumentOrContext(
                this.getChildren(), context, this::evaluate, getMetadata());
    }

    private List<Item> evaluate(Item node) {
        if (node == null) {
            return List.of();
        }
        if (!node.isNode()) {
            throw new UnexpectedTypeException("The argument must be a reference to an XML node", getMetadata());
        }
        return node.baseUri();
    }

    /**
     * Helper method to get the context node.
     * If no parameters are provided, uses the context item.
     * If a parameter is provided, uses the first parameter.
     */
}
