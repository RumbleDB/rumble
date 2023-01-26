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

public class SinhFunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;

    public SinhFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        Item value = this.children.get(0).materializeFirstItemOrNull(dynamicContext);
        if (value == null) {
            return null;
        }
        double dvalue = value.getDoubleValue();
        if (Double.isNaN(dvalue) || Double.isInfinite(dvalue)) {
            return ItemFactory.getInstance().createDoubleItem(Double.NaN);
        }
        return ItemFactory.getInstance().createDoubleItem(Math.sinh(dvalue));
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext childQuery = this.children.get(0).generateNativeQuery(nativeClauseContext);
        if (childQuery == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (SequenceType.Arity.OneOrMore.isSubtypeOf(childQuery.getResultingType().getArity())) {
            return NativeClauseContext.NoNativeQuery;
        }
        String resultingQuery = "SINH( "
            + childQuery.getResultingQuery()
            + " )";
        return new NativeClauseContext(
                childQuery,
                resultingQuery,
                new SequenceType(BuiltinTypesCatalogue.doubleItem, childQuery.getResultingType().getArity())
        );
    }


}
