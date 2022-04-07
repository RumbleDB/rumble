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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.arithmetic.MultiplicativeExpression;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.arithmetics.MultiplicativeOperationIterator;
import org.rumbledb.runtime.primary.VariableReferenceIterator;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AvgFunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private Item item;

    public AvgFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item count = CountFunctionIterator.computeCount(
            this.children.get(0),
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
            this.children.get(0),
            context,
            getMetadata()
        );
        this.item = MultiplicativeOperationIterator.processItem(
            sum,
            count,
            MultiplicativeExpression.MultiplicativeOperator.DIV,
            getMetadata()
        );
        return this.item;
    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        if (this.children.get(0) instanceof VariableReferenceIterator) {
            VariableReferenceIterator expr = (VariableReferenceIterator) this.children.get(0);
            Map<Name, DynamicContext.VariableDependency> result =
                new TreeMap<Name, DynamicContext.VariableDependency>();
            result.put(expr.getVariableName(), DynamicContext.VariableDependency.AVERAGE);
            return result;
        } else {
            return super.getVariableDependencies();
        }
    }
}
