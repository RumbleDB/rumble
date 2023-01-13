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

package org.rumbledb.runtime.functions.numerics;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.SequenceType;

import java.util.List;

public class AbsFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public AbsFunctionIterator(
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
        if (value.isDouble()) {
            return ItemFactory.getInstance().createDoubleItem(Math.abs(value.getDoubleValue()));
        }
        if (value.isFloat()) {
            return ItemFactory.getInstance().createFloatItem(Math.abs(value.getFloatValue()));
        }
        if (value.isInt()) {
            if (value.getIntValue() >= 0) {
                return value;
            }
            return ItemFactory.getInstance().createIntItem(-value.getIntValue());
        }
        if (value.isInteger()) {
            return ItemFactory.getInstance().createIntegerItem(value.getIntegerValue().abs());
        }
        if (value.isDecimal()) {
            return ItemFactory.getInstance().createDecimalItem(value.getDecimalValue().abs());
        }
        throw new OurBadException("Numeric value expected in abs()");
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext nativeChildQuery = this.children.get(0).generateNativeQuery(nativeClauseContext);
        if (nativeChildQuery == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (SequenceType.Arity.OneOrMore.isSubtypeOf(nativeChildQuery.getResultingType().getArity())) {
            return NativeClauseContext.NoNativeQuery;
        }
        return new NativeClauseContext(
                nativeClauseContext,
                "ABS(" + nativeChildQuery.getResultingQuery() + ")",
                nativeChildQuery.getResultingType()
        );
    }
}
