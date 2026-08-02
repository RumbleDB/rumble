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

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.cursor.ContextOrArgumentLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import java.io.Serial;
import java.util.List;

/**
 * Implementation of the fn:document-uri function according to XPath and XQuery Functions and Operators 3.1
 * ({@code https://www.w3.org/TR/xpath-functions-31/#func-document-uri}) and the XDM 3.1 document-uri accessor.
 *
 * XDM 3.1 Section 5.4 document-uri Accessor.
 *
 * dm:document-uri($n as document-node()) as xs:anyURI?
 *
 * "The dm:document-uri accessor returns the value of the document-uri property of a
 * document node, if it has one; otherwise it returns the empty sequence."
 *
 * Function signature (Functions and Operators 3.1, {@code fn:document-uri}):
 * 
 * <ul>
 * <li>fn:document-uri($arg as node()?) as xs:anyURI?</li>
 * </ul>
 *
 * Rules:
 * 
 * <ul>
 * <li>If the argument is supplied and is the empty sequence, the function returns the empty sequence.</li>
 * <li>Otherwise, the function returns dm:document-uri($arg).</li>
 * </ul>
 * 
 * @see <a href="https://www.w3.org/TR/xpath-functions-31/#func-document-uri">XPath and XQuery Functions and
 *      Operators 3.1: fn:document-uri</a>
 */
public class DocumentUriFunctionIterator extends LocalFunctionCallIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public DocumentUriFunctionIterator(
            List<ItemRuntimePlan> parameters,
            RuntimeStaticContext staticContext
    ) {
        super(parameters, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return ContextOrArgumentLocalCursor.flatMapFirstArgumentOrContext(
            this.getChildren(),
            context,
            this::evaluate,
            getMetadata()
        );
    }

    private List<Item> evaluate(Item node) {
        if (node == null) {
            return List.of();
        }
        if (!node.isNode()) {
            throw new UnexpectedTypeException(
                    "The argument must be a reference to an XML node",
                    getMetadata()
            );
        }
        return node.documentUri();
    }

    /**
     * Helper method to get the context node.
     * If no parameters are provided, uses the context item.
     * If a parameter is provided, uses the first parameter.
     */
}
