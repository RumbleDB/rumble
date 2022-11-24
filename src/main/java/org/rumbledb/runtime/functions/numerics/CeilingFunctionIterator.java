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
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.BuiltinTypesCatalogue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CeilingFunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;

    public CeilingFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item value = this.children.get(0).materializeFirstItemOrNull(context);
        if (value == null) {
            return null;
        }
        if (
            (value.isDouble() && Double.isNaN(value.getDoubleValue()))
                || (value.isFloat() && Float.isNaN(value.getFloatValue()))
        ) {
            return value;
        }
        if (
            (value.isDouble() && Double.isInfinite(value.getDoubleValue()))
                || (value.isFloat() && Float.isInfinite(value.getFloatValue()))
        ) {
            return value;
        }

        if (value.isInt()) {
            return ItemFactory.getInstance().createIntItem(value.getIntValue());
        }
        if (value.isInteger()) {
            return ItemFactory.getInstance().createIntegerItem(value.getIntegerValue());
        }
        if (value.isDecimal()) {
            BigDecimal bd = value.getDecimalValue().setScale(0, RoundingMode.CEILING);
            return ItemFactory.getInstance().createDecimalItem(bd);
        }
        if (value.isFloat()) {
            return ItemFactory.getInstance()
                .createFloatItem(
                    (float) Math.ceil(
                        value.getFloatValue()
                    )
                );
        }
        if (value.isDouble()) {
            return ItemFactory.getInstance()
                .createDoubleItem(
                    Math.ceil(
                        value.getDoubleValue()
                    )
                );
        }
        throw new UnexpectedTypeException(
                "Unexpected value in ceiling(): " + value.getDynamicType(),
                getMetadata()
        );

    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext value = this.children.get(0).generateNativeQuery(nativeClauseContext);
        if (value == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (!value.getResultingType().equals(BuiltinTypesCatalogue.floatItem)) {
            return NativeClauseContext.NoNativeQuery;
        }
        String resultingQuery = "( CAST ("
            + "CEIL( "
            + value.getResultingQuery()
            + " ) AS FLOAT)"
            + " )";
        return new NativeClauseContext(nativeClauseContext, resultingQuery, BuiltinTypesCatalogue.floatItem);
    }


}
