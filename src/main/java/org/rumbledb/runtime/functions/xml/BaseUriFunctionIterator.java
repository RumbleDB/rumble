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

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import java.util.List;

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
 * - fn:base-uri() as xs:anyURI?
 * - fn:base-uri($arg as node()?) as xs:anyURI?
 *
 * Rules:
 * - If the argument is omitted, it defaults to the context item (.).
 * - If the argument is supplied and is the empty sequence, the function returns the empty sequence.
 * - Otherwise, the function returns dm:base-uri($arg).
 *
 * @see <a href="https://www.w3.org/TR/xpath-functions-31/#func-base-uri">XPath and XQuery Functions and
 *      Operators 3.1: fn:base-uri</a>
 */
public class BaseUriFunctionIterator extends LocalFunctionCallIterator {
    private static final long serialVersionUID = 1L;

    private List<Item> resultItems;
    private int currentIndex;

    public BaseUriFunctionIterator(List<RuntimeIterator> parameters, RuntimeStaticContext staticContext) {
        super(parameters, staticContext);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.currentIndex = 0;

        Item node = getContextNode();

        // If the argument is supplied and is the empty sequence, return the empty sequence.
        if (node == null) {
            this.resultItems = null;
            this.hasNext = false;
            return;
        }

        // Check if the item is an XML node; otherwise, raise a type error.
        if (!node.isNode()) {
            throw new UnexpectedTypeException(
                    "The argument must be a reference to an XML node",
                    getMetadata()
            );
        }

        // Delegate to the XDM 3.1 dm:base-uri accessor implemented by XML node item classes.
        // See Item.baseUri() and XDM 3.1 Section 5.2.
        this.resultItems = node.baseUri();
        this.hasNext = this.resultItems != null && !this.resultItems.isEmpty();
    }

    @Override
    public Item next() {
        if (!this.hasNext) {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " base-uri function",
                    getMetadata()
            );
        }

        Item result = this.resultItems.get(this.currentIndex);
        this.currentIndex++;
        if (this.currentIndex >= this.resultItems.size()) {
            this.hasNext = false;
        }
        return result;
    }

    /**
     * Helper method to get the context node.
     * If no parameters are provided, uses the context item.
     * If a parameter is provided, uses the first parameter.
     */
    private Item getContextNode() {
        if (this.children.isEmpty()) {
            // No argument provided, use context item
            return this.currentDynamicContextForLocalExecution.getVariableValues()
                .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata())
                .get(0);
        }
        // Argument provided, use first parameter
        return this.children.get(0).materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
    }
}


