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
import org.rumbledb.items.StringItem;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import java.util.List;

/**
 * Implementation of the fn:name function according to XQuery 3.1 specification.
 * 
 * Returns the name of a node, as an xs:string that is either the zero-length string,
 * or has the lexical form of an xs:QName.
 * 
 * Function signatures:
 * - fn:name() as xs:string
 * - fn:name($arg as node()?) as xs:string
 * 
 * Rules:
 * - If the argument is omitted, it defaults to the context item (.)
 * - If the argument is supplied and is the empty sequence, the function returns the zero-length string
 * - If the node identified by $arg has no name (that is, if it is a document node, a comment,
 * a text node, or a namespace node having no name), the function returns the zero-length string
 * - Otherwise, the function returns the value of the expression fn:string(fn:node-name($arg))
 * 
 * @see <a href="https://www.w3.org/TR/xpath-functions-31/#func-name">XPath Functions 3.1: fn:name</a>
 */
public class NodeNameFunctionIterator extends LocalFunctionCallIterator {
    private static final long serialVersionUID = 1L;

    public NodeNameFunctionIterator(List<RuntimeIterator> parameters, RuntimeStaticContext staticContext) {
        super(parameters, staticContext);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.hasNext = true;
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            Item node = getContextNode();

            // If the argument is supplied and is the empty sequence, return zero-length string
            if (node == null) {
                return new StringItem("");
            }

            // Check if the item is an XML node; otherwise, raise a type error.
            if (!node.isNode()) {
                throw new UnexpectedTypeException(
                        "The argument must be a reference to an XML node",
                        getMetadata()
                );
            }

            // Use the generic XDM 3.1 node-name accessor defined on Item and implemented
            // by XML node item classes (see Item.nodeName()).
            String nodeName = node.nodeName();

            // If the node has no name (for example, document, comment, text, or a namespace node
            // without a name), the accessor returns null or the empty string. In both cases,
            // fn:name returns the zero-length string.
            if (nodeName == null || nodeName.isEmpty()) {
                return new StringItem("");
            }

            // For named nodes (elements, attributes, processing instructions, namespace nodes with
            // a non-empty name), return the lexical form of the QName or name as provided by
            // the node implementation.
            return new StringItem(nodeName);
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " name function", getMetadata());
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
