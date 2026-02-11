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
 * Implementation of the fn:nilled function according to XPath and XQuery Functions and Operators 3.1
 * ({@code https://www.w3.org/TR/xpath-functions-31/#func-nilled}) and the XDM 3.1 nilled accessor.
 *
 * XDM 3.1 Section 5.8 nilled Accessor.
 *
 * dm:nilled($n as node()) as xs:boolean?
 *
 * "The dm:nilled accessor returns true if the element node is nilled, false if the element
 * node is not nilled, or the empty sequence if the concept of nilled does not apply."
 *
 * In this API, the optional xs:boolean result is represented as a sequence of zero or one Items
 * returned by Item.nilled().
 *
 * Function signature (Functions and Operators 3.1, {@code fn:nilled}):
 * - fn:nilled($arg as node()?) as xs:boolean?
 *
 * Rules:
 * - If the argument is supplied and is the empty sequence, the function returns the empty sequence.
 * - Otherwise, the function returns dm:nilled($arg).
 *
 * @see <a href="https://www.w3.org/TR/xpath-functions-31/#func-nilled">XPath and XQuery Functions and
 *      Operators 3.1: fn:nilled</a>
 */
public class NilledFunctionIterator extends LocalFunctionCallIterator {
    private static final long serialVersionUID = 1L;

    private List<Item> resultItems;
    private int currentIndex;

    public NilledFunctionIterator(List<RuntimeIterator> parameters, RuntimeStaticContext staticContext) {
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

        // Delegate to the XDM 3.1 dm:nilled accessor implemented by XML node item classes.
        // See Item.nilled() and XDM 3.1 Section 5.8.
        this.resultItems = node.nilled();
        this.hasNext = this.resultItems != null && !this.resultItems.isEmpty();
    }

    @Override
    public Item next() {
        if (!this.hasNext) {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " nilled function",
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


