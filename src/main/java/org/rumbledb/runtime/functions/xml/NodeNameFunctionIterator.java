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
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.StringItem;
import org.rumbledb.runtime.cursor.ContextOrArgumentLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.util.List;

/**
 * Implementation of the fn:name function according to XQuery 3.1 specification.
 * 
 * Returns the name of a node, as an xs:string that is either the zero-length string,
 * or has the lexical form of an xs:QName.
 * 
 * Function signatures:
 * 
 * <ul>
 * <li>fn:name() as xs:string</li>
 * <li>fn:name($arg as node()?) as xs:string</li>
 * </ul>
 * 
 * Rules:
 * <ul>
 * <li>If the argument is omitted, it defaults to the context item (.)</li>
 * <li>If the argument is supplied and is the empty sequence, the function returns the zero-length string</li>
 * <li>If the node identified by $arg has no name (that is, if it is a document node, a comment,
 * a text node, or a namespace node having no name), the function returns the zero-length string</li>
 * <li>Otherwise, the function returns the value of the expression fn:string(fn:node-name($arg))</li>
 * </ul>
 * 
 * @see <a href="https://www.w3.org/TR/xpath-functions-31/#func-name">XPath Functions 3.1: fn:name</a>
 */
public class NodeNameFunctionIterator extends LocalFunctionCallIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public NodeNameFunctionIterator(
            List<RuntimePlan<Item>> parameters,
            RuntimeStaticContext staticContext
    ) {
        super(parameters, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return ContextOrArgumentLocalCursor.mapFirstArgumentOrContext(
            this.getChildren(),
            context,
            this::evaluate,
            getMetadata()
        );
    }

    private Item evaluate(Item node) {
        if (node == null) {
            return new StringItem("");
        }
        if (!node.isNode()) {
            throw new UnexpectedTypeException(
                    "The argument must be a reference to an XML node",
                    getMetadata()
            );
        }
        Name nodeName = node.nodeName();
        return new StringItem(nodeName == null ? "" : nodeName.toString());
    }

    /**
     * Helper method to get the context node.
     * If no parameters are provided, uses the context item.
     * If a parameter is provided, uses the first parameter.
     */
}
