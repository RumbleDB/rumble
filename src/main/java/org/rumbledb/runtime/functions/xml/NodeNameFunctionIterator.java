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
import org.rumbledb.items.xml.AttributeItem;
import org.rumbledb.items.xml.DocumentItem;
import org.rumbledb.items.xml.ElementItem;
import org.rumbledb.items.xml.TextItem;
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

            // Check if node is a supported XML node type
            if (
                node instanceof DocumentItem
                    || node instanceof ElementItem
                    || node instanceof AttributeItem
                    || node instanceof TextItem
            ) {

                // If the node has no name (document, comment, text, namespace node with no name),
                // return the zero-length string
                if (node instanceof DocumentItem || node instanceof TextItem) {
                    return new StringItem("");
                }

                // For element and attribute nodes, get the node name
                if (node instanceof ElementItem) {
                    ElementItem element = (ElementItem) node;
                    String nodeName = element.nodeName();
                    if (nodeName != null && !nodeName.isEmpty()) {
                        // Return the string representation of the QName
                        // This includes the namespace prefix if present
                        return new StringItem(nodeName);
                    } else {
                        return new StringItem("");
                    }
                } else if (node instanceof AttributeItem) {
                    AttributeItem attribute = (AttributeItem) node;
                    String nodeName = attribute.nodeName();
                    if (nodeName != null && !nodeName.isEmpty()) {
                        // Return the string representation of the QName
                        // This includes the namespace prefix if present
                        return new StringItem(nodeName);
                    } else {
                        return new StringItem("");
                    }
                }

                // Fallback for other supported node types that have no name
                return new StringItem("");
            }

            throw new UnexpectedTypeException(
                    "The argument must be a reference to a supported XML node type",
                    getMetadata()
            );
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
