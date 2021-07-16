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
import org.rumbledb.items.DoubleItem;
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
        if ((value.isDouble() && Double.isNaN(value.getDoubleValue())) || (value.isFloat() && Float.isNaN(value.getFloatValue()))) {
            return value;
        }
        if ((value.isDouble() && Double.isInfinite(value.getDoubleValue()))  || (value.isFloat() && Float.isInfinite(value.getFloatValue()))) {
            return value;
        }
        if ((value.isDouble() && Double.compare(value.getDoubleValue(), -0d) == 0 || (value.isFloat() && Float.compare(value.getFloatValue(), -0f) == 0)))  {
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
                double sign = getSign(value.getDecimalValue().doubleValue());
                BigDecimal bd;
                if (sign ==1) {
                    bd = value.getDecimalValue().setScale(precision, RoundingMode.HALF_UP);
                } else {
                    bd = value.getDecimalValue().setScale(precision, RoundingMode.HALF_DOWN);
                }
                return ItemFactory.getInstance().createDecimalItem(bd);
            }
            if (value.isDouble()) {
                double sign = getSign(value.getDoubleValue());
                BigDecimal bd;
                if (sign ==1) {
                    bd = new BigDecimal(value.getDoubleValue()).setScale(precision, RoundingMode.HALF_UP);
                } else {
                    bd = new BigDecimal(value.getDoubleValue()).setScale(precision, RoundingMode.HALF_DOWN);
                }

                return ItemFactory.getInstance().createDoubleItem(sign * Math.abs(bd.doubleValue()));
            }
            if (value.isFloat()) {

                double sign = getSign(value.getFloatValue());
                BigDecimal bd;
                if (sign ==1) {
                    bd = new BigDecimal(value.getFloatValue()).setScale(precision, RoundingMode.HALF_UP);
                } else {
                    bd = new BigDecimal(value.getFloatValue()).setScale(precision, RoundingMode.HALF_DOWN);
                }
                return ItemFactory.getInstance().createFloatItem((float) sign * Math.abs(bd.floatValue()));
            }
            throw new UnexpectedTypeException("Unexpected value in round(): " + value.getDynamicType(), getMetadata());

        } catch (IteratorFlowException e) {
            throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
        }
    }

    private double getSign(double doubleValue) {
        double sign = 0;
        if (doubleValue > 0) sign = 1;
        if (doubleValue < 0) sign = -1;
        return sign;
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
