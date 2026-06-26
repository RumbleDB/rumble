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
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.runtime.functions;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.NumericOverflowOrUnderflow;
import org.rumbledb.exceptions.UnknownFunctionCallException;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;

public class NamedFunctionRefRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final FunctionIdentifier functionIdentifier;
    private final Name functionName;
    private final String arityLiteral;

    public NamedFunctionRefRuntimeIterator(
            FunctionIdentifier functionIdentifier,
            Name functionName,
            String arityLiteral,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        this.functionIdentifier = functionIdentifier;
        this.functionName = functionName;
        this.arityLiteral = arityLiteral;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        if (this.functionIdentifier == null) {
            throw new NumericOverflowOrUnderflow(
                    "Named function reference arity is out of range for implementation limits: "
                        + this.functionName
                        + "#"
                        + this.arityLiteral,
                    getMetadata()
            );
        }
        Item resolved = NamedFunctionLookup.lookupOrNull(
            this.functionIdentifier,
            dynamicContext,
            getConfiguration(),
            getMetadata()
        );
        if (resolved != null) {
            return resolved;
        }
        throw new UnknownFunctionCallException(
                this.functionIdentifier.getName(),
                this.functionIdentifier.getArity(),
                getMetadata()
        );
    }
}
