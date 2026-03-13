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
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of fn:in-scope-prefixes according to
 * XPath and XQuery Functions and Operators 3.1, Section 14.1.
 *
 * Spec (Functions and Operators 3.1, Section 14.1):
 * "fn:in-scope-prefixes($element as element()) as xs:string*"
 *
 * "Summary: Returns the prefixes of the in-scope namespaces for $element."
 *
 * "Rules: The function returns a sequence of zero or more xs:string values."
 * "For namespace bindings that have a prefix, the prefix is returned."
 * "For the default namespace, if it exists, the zero-length string is returned."
 * "The order of the result is implementation dependent."
 *
 * This function calls namespaceNodes() on the element item, which returns
 * the in-scope namespace nodes computed via parent chaining of declared
 * namespaces, and extracts the prefixes as xs:string* items.
 *
 * @see <a href="https://www.w3.org/TR/xpath-functions-31/#func-in-scope-prefixes">XPath and XQuery Functions and
 *      Operators 3.1 : fn:in-scope-prefixes</a>
 */
public class InScopePrefixesFunctionIterator extends LocalFunctionCallIterator {
    private static final long serialVersionUID = 1L;

    private List<Item> prefixItems;
    private int currentIndex;

    public InScopePrefixesFunctionIterator(List<RuntimeIterator> parameters, RuntimeStaticContext staticContext) {
        super(parameters, staticContext);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.prefixItems = null;
        this.currentIndex = 0;

        // fn:in-scope-prefixes($element as element()) as xs:string*
        // The function requires exactly one argument of type element().
        Item element = this.children.get(0).materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);

        this.prefixItems = computeInScopePrefixes(element);
        this.hasNext = !this.prefixItems.isEmpty();
    }

    @Override
    public Item next() {
        if (!this.hasNext) {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in-scope-prefixes function",
                    getMetadata()
            );
        }

        Item result = this.prefixItems.get(this.currentIndex);
        this.currentIndex++;

        if (this.currentIndex >= this.prefixItems.size()) {
            this.hasNext = false;
        }

        return result;
    }

    /**
     * Computes the in-scope namespace prefixes for the given element.
     *
     * XPath and XQuery Functions and Operators 3.1, Section 14.1 (fn:in-scope-prefixes):
     * "Returns the prefixes of the in-scope namespaces for $element."
     * "For namespace bindings that have a prefix, the prefix is returned."
     * "For the default namespace, if it exists, the zero-length string is returned."
     * "The order of the result is implementation dependent."
     *
     * Delegates to element.namespaceNodes() which computes the in-scope
     * namespace nodes via parent chaining of declared namespaces, then
     * extracts the prefixes.
     *
     * @param element the element node for which to compute in-scope prefixes
     * @return a list of StringItem objects representing the prefixes
     */
    private List<Item> computeInScopePrefixes(Item element) {
        List<Item> result = new ArrayList<>();

        // Get namespace nodes from the element (parent chaining + declared).
        // Extract prefixes as xs:string items.
        // "For namespace bindings that have a prefix, the prefix is returned."
        // "For the default namespace, if it exists, the zero-length string is returned."
        for (Item nsNode : element.namespaceNodes()) {
            result.add(ItemFactory.getInstance().createStringItem(nsNode.nodeName()));
        }

        return result;
    }
}
