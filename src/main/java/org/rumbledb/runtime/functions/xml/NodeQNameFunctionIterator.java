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
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import java.util.List;

/**
 * Implementation of the fn:node-name function according to XPath and XQuery Functions and Operators 3.1
 * ({@code https://www.w3.org/TR/xpath-functions-31/#func-node-name}) and the XDM 3.1 node-name accessor.
 *
 * XDM 3.1 Section 5.10 node-name Accessor.
 *
 * dm:node-name($n as node()) as xs:QName?
 *
 * "The dm:node-name accessor returns the name of the node as an xs:QName, or the empty
 * sequence if the node does not have a name."
 *
 * Function signatures (Functions and Operators 3.1, {@code fn:node-name}):
 * - fn:node-name() as xs:QName?
 * - fn:node-name($arg as node()?) as xs:QName?
 *
 * Rules:
 * - "If the argument is omitted, it defaults to the context item."
 * - "If the argument is supplied and is the empty sequence, the function returns the empty sequence."
 * - "Otherwise, the function returns the result of applying the dm:node-name accessor to the node
 *    identified by the argument. If the dm:node-name accessor returns the empty sequence, then the
 *    function returns the empty sequence."
 *
 * In this implementation, the optional xs:QName result is represented via the existing Item.nodeName()
 * accessor defined on Item and implemented by XML node item classes. A non-null, non-empty lexical
 * QName is exposed as a string item, and the empty sequence is used when the node has no name.
 *
 * @see <a href="https://www.w3.org/TR/xpath-functions-31/#func-node-name">XPath and XQuery Functions and
 *      Operators 3.1: fn:node-name</a>
 */
public class NodeQNameFunctionIterator extends LocalFunctionCallIterator {
    private static final long serialVersionUID = 1L;

    private Item resultItem;

    public NodeQNameFunctionIterator(List<RuntimeIterator> parameters, RuntimeStaticContext staticContext) {
        super(parameters, staticContext);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        Item node = getContextNode();

        // Spec: "If the argument is supplied and is the empty sequence, the function returns the empty sequence."
        if (node == null) {
            this.resultItem = null;
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

        // Spec: "The dm:node-name accessor returns the name of the node as an xs:QName, or the empty
        // sequence if the node does not have a name."
        //
        // Here we use the generic XDM 3.1 node-name accessor defined on Item and implemented
        // by XML node item classes (see Item.nodeName()).
        String nodeName = node.nodeName();

        // Spec: "If the dm:node-name accessor returns the empty sequence, then the function returns the empty sequence."
        if (nodeName == null || nodeName.isEmpty()) {
            this.resultItem = null;
            this.hasNext = false;
        } else {
            // For named nodes, we expose the lexical QName as a string item.
            this.resultItem = ItemFactory.getInstance().createStringItem(nodeName);
            this.hasNext = true;
        }
    }

    @Override
    public Item next() {
        if (!this.hasNext) {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " node-name function",
                    getMetadata()
            );
        }

        this.hasNext = false;
        return this.resultItem;
    }

    /**
     * Helper method to get the context node.
     * If no parameters are provided, uses the context item.
     * If a parameter is provided, uses the first parameter.
     *
     * Spec: "If the argument is omitted, it defaults to the context item."
     */
    private Item getContextNode() {
        if (this.children.isEmpty()) {
            // No argument provided, use context item
            return this.currentDynamicContextForLocalExecution.getVariableValues()
                .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata())
                .get(0);
        }
        // Argument provided, use first parameter (may materialize to the empty sequence).
        return this.children.get(0).materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
    }
}


