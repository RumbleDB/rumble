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
 * Authors: Ghislain Fourny
 *
 */

package org.rumbledb.runtime;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.types.BuiltinTypesCatalogue;

import sparksoniq.spark.SparkSessionManager;

import org.rumbledb.runtime.misc.ComparisonIterator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public abstract class AtMostOneItemLocalRuntimeIterator extends RuntimeIterator {

    private static final long serialVersionUID = 1L;
    private Item result;

    protected AtMostOneItemLocalRuntimeIterator(
            List<RuntimeIterator> children,
            RuntimeStaticContext staticContext
    ) {
        super(children, staticContext);
        if (getHighestExecutionMode() != ExecutionMode.LOCAL) {
            throw new OurBadException("At-most-one-item runtime iterators support only the local execution mode");
        }
    }

    public JavaRDD<Item> getRDD(DynamicContext context) {
        Item i = materializeFirstItemOrNull(context);
        List<Item> result = new ArrayList<>();
        if (i != null) {
            result.add(i);
        }
        return SparkSessionManager.getInstance().getJavaSparkContext().parallelize(result);
    }

    public abstract Item materializeFirstItemOrNull(
            DynamicContext context
    );

    @Override
    public void open(DynamicContext dynamicContext) {
        super.open(dynamicContext);
        this.result = materializeFirstItemOrNull(dynamicContext);
        this.hasNext = this.result != null;
    }

    @Override
    public Item next() {
        if (!this.hasNext) {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
        this.hasNext = false;
        return this.result;
    }

    @Override
    public void reset(DynamicContext dynamicContext) {
        super.reset(dynamicContext);
        this.result = materializeFirstItemOrNull(dynamicContext);
        this.hasNext = this.result != null;
    }

    @Override
    public void close() {
        super.close();
        this.result = null;
    }

    public Item materializeExactlyOneItem(
            DynamicContext dynamicContext
    )
            throws NoItemException,
                MoreThanOneItemException {
        Item result = materializeFirstItemOrNull(dynamicContext);
        if (result == null) {
            throw new NoItemException();
        }
        return result;
    }

    public Item materializeAtMostOneItemOrNull(
            DynamicContext dynamicContext
    )
            throws MoreThanOneItemException {
        return materializeFirstItemOrNull(dynamicContext);
    }

    public void materialize(DynamicContext dynamicContext, List<Item> result) {
        result.clear();
        Item item = materializeFirstItemOrNull(dynamicContext);
        if (item != null) {
            result.add(item);
        }
    }

    public void materializeNFirstItems(DynamicContext dynamicContext, List<Item> result, int n) {
        result.clear();
        if (n == 0) {
            return;
        }
        Item item = materializeFirstItemOrNull(dynamicContext);
        if (item != null) {
            result.add(item);
        }
    }

    @Override
    public boolean getEffectiveBooleanValueOrCheckPosition(DynamicContext dynamicContext, Item position) {
        Item item = materializeFirstItemOrNull(dynamicContext);
        if (item == null) {
            return false;
        }
        if (item.isBoolean()) {
            return item.getBooleanValue();
        }
        if (item.isNumeric()) {
            if (position == null) {
                if (item.isInt()) {
                    return item.getIntValue() != 0;
                } else if (item.isInteger()) {
                    return !item.getIntegerValue().equals(BigInteger.ZERO);
                } else if (item.isDouble()) {
                    return !item.isNaN() && item.getDoubleValue() != 0;
                } else if (item.isFloat()) {
                    return !item.isNaN() && item.getFloatValue() != 0;
                } else if (item.isDecimal()) {
                    return !(item.getDecimalValue().compareTo(BigDecimal.ZERO) == 0);
                } else {
                    throw new OurBadException(
                            "Unexpected numeric type found while calculating effective boolean value."
                    );
                }
            } else {
                return ComparisonIterator.compareItems(item, position, ComparisonOperator.VC_EQ, getMetadata()) == 0;
            }
        }
        if (item.isNull()) {
            return false;
        }
        if (item.getDynamicType().canBePromotedTo(BuiltinTypesCatalogue.stringItem)) {
            return !item.getStringValue().isEmpty();
        }
        /*
         * if (item.isObject()) {
         * return true;
         * }
         * if (item.isArray()) {
         * return true;
         * }
         */
        throw new InvalidArgumentTypeException(
                "Effective boolean value not defined for items of type "
                    +
                    item.getDynamicType().toString(),
                getMetadata()
        );
    }
}
