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

package org.rumbledb.runtime.functions.numerics.trigonometric;

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

public class ATan2FunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public ATan2FunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        Item valuey = this.children.get(0).materializeFirstItemOrNull(dynamicContext);
        Item valuex = this.children.get(1).materializeFirstItemOrNull(dynamicContext);
        double y = valuey.getDoubleValue();
        double x = valuex.getDoubleValue();
        if (Double.isNaN(x) || Double.isNaN(y)) {
            return ItemFactory.getInstance().createDoubleItem(Double.NaN);
        }
        return ItemFactory.getInstance().createDoubleItem(Math.atan2(y, x));
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext yQuery = this.children.get(0).generateNativeQuery(nativeClauseContext);
        NativeClauseContext xQuery = this.children.get(1).generateNativeQuery(nativeClauseContext);
        if (yQuery == NativeClauseContext.NoNativeQuery || xQuery == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (
            SequenceType.Arity.OneOrMore.isSubtypeOf(yQuery.getResultingType().getArity())
                ||
                SequenceType.Arity.OneOrMore.isSubtypeOf(xQuery.getResultingType().getArity())
        ) {
            return NativeClauseContext.NoNativeQuery;
        }
        SequenceType.Arity resultingArity = (yQuery.getResultingType().getArity() == SequenceType.Arity.One
            && xQuery.getResultingType().getArity() == SequenceType.Arity.One)
                ? SequenceType.Arity.One
                : SequenceType.Arity.OneOrZero;
        String resultingQuery = "ATAN2( "
            + yQuery.getResultingQuery()
            + ", "
            + xQuery.getResultingQuery()
            + " )";
        return new NativeClauseContext(
                nativeClauseContext,
                resultingQuery,
                new SequenceType(BuiltinTypesCatalogue.doubleItem, resultingArity)
        );
    }
}
