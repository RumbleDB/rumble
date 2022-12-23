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
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

import java.util.List;

public class Exp10FunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;

    public Exp10FunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item exponent = this.children.get(0).materializeFirstItemOrNull(context);
        if (exponent == null) {
            return null;
        }
        double dexponent = exponent.getDoubleValue();
        if (Double.isNaN(dexponent) || Double.isInfinite(dexponent)) {
            return ItemFactory.getInstance().createDoubleItem(Double.NaN);
        }
        return ItemFactory.getInstance().createDoubleItem(Math.pow(10.0, dexponent));
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext baseQuery = this.children.get(0).generateNativeQuery(nativeClauseContext);
        NativeClauseContext powerQuery = this.children.get(0).generateNativeQuery(nativeClauseContext);
        if (baseQuery == NativeClauseContext.NoNativeQuery || powerQuery == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (
            SequenceType.Arity.OneOrMore.isSubtypeOf(baseQuery.getResultingType().getArity())
                ||
                SequenceType.Arity.OneOrMore.isSubtypeOf(powerQuery.getResultingType().getArity())
        ) {
            return NativeClauseContext.NoNativeQuery;
        }
        String resultingQuery = "POW( "
            + "10.0"
            + ", "
            + powerQuery.getResultingQuery()
            + " )";
        return new NativeClauseContext(
                nativeClauseContext,
                resultingQuery,
                new SequenceType(BuiltinTypesCatalogue.doubleItem, SequenceType.Arity.One)
        );
    }

}
