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

package org.rumbledb.runtime.functions.numerics.exponential;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

import java.util.List;

public class PowFunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;

    public PowFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item base = this.children.get(0).materializeFirstItemOrNull(context);
        if (base == null) {
            return null;
        }
        Item exponent = this.children.get(1)
            .materializeFirstItemOrNull(context);
        if (exponent == null) {
            return null;
        }
        try {
            return ItemFactory.getInstance()
                .createDoubleItem(Math.pow(base.castToDoubleValue(), exponent.castToDoubleValue()));
        } catch (IteratorFlowException e) {
            throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
        }

    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext baseQuery = this.children.get(0).generateNativeQuery(nativeClauseContext);
        NativeClauseContext exponentQuery = this.children.get(1).generateNativeQuery(nativeClauseContext);
        if (baseQuery == NativeClauseContext.NoNativeQuery || exponentQuery == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (
            SequenceType.Arity.OneOrMore.isSubtypeOf(baseQuery.getResultingType().getArity())
                ||
                SequenceType.Arity.OneOrMore.isSubtypeOf(exponentQuery.getResultingType().getArity())
        ) {
            return NativeClauseContext.NoNativeQuery;
        }
        SequenceType.Arity resultingArity = (baseQuery.getResultingType().getArity() == SequenceType.Arity.One
            && exponentQuery.getResultingType().getArity() == SequenceType.Arity.One)
                ? SequenceType.Arity.One
                : SequenceType.Arity.OneOrZero;
        String resultingQuery = "pow( "
            + baseQuery.getResultingQuery()
            + ", "
            + exponentQuery.getResultingQuery()
            + " )";
        return new NativeClauseContext(
                nativeClauseContext,
                resultingQuery,
                new SequenceType(BuiltinTypesCatalogue.doubleItem, resultingArity)
        );
    }

}
