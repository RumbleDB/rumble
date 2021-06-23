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
import org.rumbledb.exceptions.IteratorFlowException;
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

public class RoundFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public RoundFunctionIterator(
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
        if (value.isDouble() && Double.isNaN(value.getDoubleValue())) {
            return value;
        }
        if (value.isDouble() && Double.isInfinite(value.getDoubleValue())) {
            return value;
        }
        if (value.isDouble() && value.getDoubleValue() == 0d) {
            return value;
        }
        int precision;
        if (this.children.size() > 1) {
            precision = this.children.get(1)
                .materializeFirstItemOrNull(dynamicContext)
                .getIntValue();
        }
        // if second param is not given precision is set as 0 (rounds to a whole number)
        else {
            precision = 0;
        }
        try {
            if (value.isInt()) {
                BigDecimal bd = new BigDecimal(value.getIntValue()).setScale(precision, RoundingMode.HALF_UP);
                return ItemFactory.getInstance().createIntItem(bd.intValue());
            }
            if (value.isInteger()) {
                BigDecimal bd = new BigDecimal(value.getIntegerValue()).setScale(precision, RoundingMode.HALF_UP);
                return ItemFactory.getInstance().createIntegerItem(bd.toBigInteger());
            }
            if (value.isDecimal()) {
                BigDecimal bd = value.getDecimalValue().setScale(precision, RoundingMode.HALF_UP);
                return ItemFactory.getInstance().createDecimalItem(bd);
            }
            if (value.isDouble()) {
                BigDecimal bd = new BigDecimal(value.getDoubleValue()).setScale(precision, RoundingMode.HALF_UP);
                return ItemFactory.getInstance().createDoubleItem(bd.doubleValue());
            }
            if (value.isFloat()) {
                BigDecimal bd = new BigDecimal(value.getFloatValue()).setScale(precision, RoundingMode.HALF_UP);
                return ItemFactory.getInstance().createFloatItem(bd.floatValue());
            }
            throw new UnexpectedTypeException("Unexpected value in round(): " + value.getDynamicType(), getMetadata());

        } catch (IteratorFlowException e) {
            throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
        }
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
            + "ROUND( "
            + value.getResultingQuery()
            + " ) AS FLOAT)"
            + " )";
        return new NativeClauseContext(nativeClauseContext, resultingQuery, BuiltinTypesCatalogue.floatItem);
    }
}
