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
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.cursor.ContextOrArgumentLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

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
 *
 * <ul>
 * <li>fn:node-name() as xs:QName?</li>
 * <li>fn:node-name($arg as node()?) as xs:QName?</li>
 * </ul>
 *
 * Rules:
 *
 * <ul>
 * <li>"If the argument is omitted, it defaults to the context item."</li>
 * <li>"If the argument is supplied and is the empty sequence, the function returns the empty sequence."</li>
 * <li>"Otherwise, the function returns the result of applying the dm:node-name accessor to the node
 * identified by the argument. If the dm:node-name accessor returns the empty sequence, then the
 * function returns the empty sequence."</li>
 * </ul>
 *
 * The optional {@code xs:QName} result wraps the expanded {@link Name} from {@link Item#nodeName()} in a
 * {@link org.rumbledb.items.QNameItem}; otherwise the function returns the empty sequence.
 *
 * @see <a href="https://www.w3.org/TR/xpath-functions-31/#func-node-name">XPath and XQuery Functions and
 *      Operators 3.1: fn:node-name</a>
 */
public class NodeQNameFunctionIterator extends LocalFunctionCallIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public NodeQNameFunctionIterator(List<ItemRuntimePlan> parameters, RuntimeStaticContext staticContext) {
        super(parameters, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return ContextOrArgumentLocalCursor.mapFirstArgumentOrContext(
                this.getChildren(), context, this::evaluate, getMetadata());
    }

    private Item evaluate(Item node) {
        if (node == null) {
            return null;
        }
        if (!node.isNode()) {
            throw new UnexpectedTypeException("The argument must be a reference to an XML node", getMetadata());
        }
        Name nodeName = node.nodeName();
        return nodeName == null ? null : ItemFactory.getInstance().createQNameItem(nodeName);
    }

    /**
     * Helper method to get the context node.
     * If no parameters are provided, uses the context item.
     * If a parameter is provided, uses the first parameter.
     *
     * Spec: "If the argument is omitted, it defaults to the context item."
     */
}
