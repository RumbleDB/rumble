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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class RoundHalfToEvenFunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;

    public RoundHalfToEvenFunctionIterator(
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

        int precision;
        if (this.children.size() > 1) {
            precision = this.children.get(1)
                .materializeFirstItemOrNull(context)
                .getIntValue();
        }
        // if second param is not given precision is set as 0 (rounds to a whole number)
        else {
            precision = 0;
        }
        try {
            if (value.isInt()) {
                BigDecimal bd = new BigDecimal(value.getIntValue());
                bd = bd.setScale(precision, RoundingMode.HALF_EVEN);
                return ItemFactory.getInstance().createIntItem(bd.intValue());
            }
            if (value.isInteger()) {
                BigDecimal bd = new BigDecimal(value.getIntegerValue());
                bd = bd.setScale(precision, RoundingMode.HALF_EVEN);
                return ItemFactory.getInstance().createIntegerItem(bd.toBigInteger());
            }
            if (value.isDouble()) {
                BigDecimal bd = new BigDecimal(value.getDoubleValue());
                bd = bd.setScale(precision, RoundingMode.HALF_EVEN);
                return ItemFactory.getInstance().createDoubleItem(bd.doubleValue());
            }
            if (value.isDecimal()) {
                BigDecimal bd = value.getDecimalValue().setScale(precision, RoundingMode.HALF_EVEN);
                return ItemFactory.getInstance().createDecimalItem(bd);
            }
            if (value.isFloat()) {
                BigDecimal bd = new BigDecimal(value.getFloatValue());
                bd = bd.setScale(precision, RoundingMode.HALF_EVEN);
                return ItemFactory.getInstance().createFloatItem(bd.floatValue());
            }

            throw new UnexpectedTypeException(
                    "Unexpected value in round-half-to-even(): " + value.getDynamicType(),
                    getMetadata()
            );

        } catch (IteratorFlowException e) {
            throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
        }

    }


}
