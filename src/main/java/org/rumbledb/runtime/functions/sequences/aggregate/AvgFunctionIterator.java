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

package org.rumbledb.runtime.functions.sequences.aggregate;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.arithmetic.MultiplicativeExpression;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.arithmetics.MultiplicativeOperationIterator;
import org.rumbledb.runtime.cursor.AtMostOneLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.primary.VariableReferenceIterator;

import lombok.NonNull;
import java.io.Serial;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AvgFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public AvgFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        RuntimeIterator child = this.getChild(0);
        if (!child.isRDDOrDataFrame()) {
            return computeLocalAverage(child, context, getMetadata());
        }
        Item count = CountFunctionIterator.computeCount(
            child,
            context,
            getMetadata()
        );
        if (count.isInt() && count.getIntValue() == 0) {
            return null;
        }
        if (count.isInteger() && count.getIntegerValue().equals(BigInteger.ZERO)) {
            return null;
        }
        Item sum = SumFunctionIterator.computeSum(
            ItemFactory.getInstance().createIntegerItem(BigInteger.ZERO),
            child,
            context,
            getMetadata()
        );
        return MultiplicativeOperationIterator.processItem(
            sum,
            count,
            MultiplicativeExpression.MultiplicativeOperator.DIV,
            getMetadata()
        );
    }

    @Override
    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        if (this.getChild(0) instanceof VariableReferenceIterator expr) {
            Map<Name, DynamicContext.VariableDependency> result =
                new TreeMap<Name, DynamicContext.VariableDependency>();
            result.put(expr.getVariableName(), DynamicContext.VariableDependency.AVERAGE);
            return result;
        } else {
            return super.getVariableDependencies();
        }
    }

    private static Item computeLocalAverage(
            RuntimePlan<Item> plan,
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        Item sum = null;
        long count = 0;
        try (Cursor<Item> cursor = plan.getCursor(context)) {
            while (cursor.hasNext()) {
                sum = SumFunctionIterator.addToSum(sum, cursor.next(), metadata);
                count++;
            }
        }
        if (count == 0) {
            return null;
        }
        return MultiplicativeOperationIterator.processItem(
            sum,
            ItemFactory.getInstance().createLongItem(count),
            MultiplicativeExpression.MultiplicativeOperator.DIV,
            metadata
        );
    }

    private static final class EvaluationCursor extends AtMostOneLocalCursor<Item> {

        private final RuntimePlan<Item> childPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;

        private EvaluationCursor(
                @NonNull RuntimePlan<Item> childPlan,
                @NonNull DynamicContext context,
                @NonNull ExceptionMetadata metadata
        ) {
            super(metadata);
            this.childPlan = childPlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected Item materializeOneItemOrNull() {
            return computeLocalAverage(this.childPlan, this.context, this.metadata);
        }
    }
}
