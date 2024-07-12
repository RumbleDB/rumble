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
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.SequenceType;

import java.util.Map;

public class FunctionRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private Name functionName;
    private Map<Name, SequenceType> paramNameToSequenceTypes;
    SequenceType returnType;
    RuntimeIterator bodyIterator;

    public FunctionRuntimeIterator(
            Name functionName,
            Map<Name, SequenceType> paramNameToSequenceTypes,
            SequenceType returnType,
            RuntimeIterator bodyIterator,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        this.functionName = functionName;
        this.paramNameToSequenceTypes = paramNameToSequenceTypes;
        this.returnType = returnType;
        this.bodyIterator = bodyIterator;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        RuntimeIterator bodyIteratorCopy = ((RuntimeIterator) this.bodyIterator).deepCopy();
        FunctionItem function = new FunctionItem(
                this.functionName,
                this.paramNameToSequenceTypes,
                this.returnType,
                dynamicContext.getModuleContext(),
                bodyIteratorCopy
        );
        function.populateClosureFromDynamicContext(dynamicContext, getMetadata());
        return function;
    }
}
