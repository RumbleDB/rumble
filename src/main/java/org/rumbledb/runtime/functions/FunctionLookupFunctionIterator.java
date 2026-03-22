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
 */

package org.rumbledb.runtime.functions;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.QNameItem;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.math.BigInteger;
import java.util.List;

/**
 * fn:function-lookup($name as xs:QName, $arity as xs:integer) as function(*)?
 *
 * @see <a href="https://www.w3.org/TR/xpath-functions-31/#func-function-lookup">XPath and XQuery Functions and
 *      Operators 3.1</a>
 */
public class FunctionLookupFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public FunctionLookupFunctionIterator(List<RuntimeIterator> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item nameItem = this.children.get(0).materializeFirstItemOrNull(context);
        if (!nameItem.isQName()) {
            throw new UnexpectedTypeException(
                    "function-lookup: first argument must be xs:QName",
                    getMetadata()
            );
        }
        Name fnName = ((QNameItem) nameItem).getExpandedName();

        Item arityItem = this.children.get(1).materializeFirstItemOrNull(context);
        if (arityItem == null) {
            throw new UnexpectedTypeException(
                    "function-lookup: second argument must be xs:integer",
                    getMetadata()
            );
        }
        if (!arityItem.isNumeric()) {
            throw new UnexpectedTypeException(
                    "function-lookup: second argument must be xs:integer",
                    getMetadata()
            );
        }
        BigInteger big = arityItem.castToIntegerValue();
        int arity;
        try {
            arity = big.intValueExact();
        } catch (ArithmeticException e) {
            return null;
        }
        if (arity < 0) {
            return null;
        }

        FunctionIdentifier id = new FunctionIdentifier(fnName, arity);
        return NamedFunctionLookup.lookupOrNull(id, context, getConfiguration(), getMetadata());
    }
}
