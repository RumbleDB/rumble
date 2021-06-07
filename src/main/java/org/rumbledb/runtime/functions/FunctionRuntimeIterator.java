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

import java.util.HashMap;
import java.util.Map;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.SequenceType;

public class FunctionRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private Name functionName;
    private Map<Name, SequenceType> paramNameToSequenceTypes;
    SequenceType returnType;
    Map<Long, RuntimeIterator> bodyIterators;

    public FunctionRuntimeIterator(
            Name functionName,
            Map<Name, SequenceType> paramNameToSequenceTypes,
            SequenceType returnType,
            Map<Long, RuntimeIterator> bodyIterators,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        this.functionName = functionName;
        this.paramNameToSequenceTypes = paramNameToSequenceTypes;
        this.returnType = returnType;
        this.bodyIterators = bodyIterators;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        Map<Long, RuntimeIterator> bodyIteratorsCopy = new HashMap<>();
        for (long l : this.bodyIterators.keySet()) {
            bodyIteratorsCopy.put(l, (RuntimeIterator) this.bodyIterators.get(l).deepCopy());
        }
        FunctionItem function = new FunctionItem(
                this.functionName,
                this.paramNameToSequenceTypes,
                this.returnType,
                dynamicContext.getModuleContext(),
                bodyIteratorsCopy
        );
        function.populateClosureFromDynamicContext(dynamicContext, getMetadata());
        return function;
    }
}
