/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package sparksoniq.jsoniq.runtime.iterator.functions.sequences.aggregate;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.compiler.translator.expr.primary.VariableReference;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CountFunctionIterator extends AggregateFunctionIterator {
    public CountFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, AggregateFunctionIterator.AggregateFunctionOperator.COUNT, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            RuntimeIterator iterator = this._children.get(0);
            if (!iterator.isRDD()) {
                if(_children.get(0) instanceof VariableReferenceIterator)
                {
                    VariableReferenceIterator expr = (VariableReferenceIterator) _children.get(0);
                    this._hasNext = false;
                    return _currentDynamicContext.getVariableCount(expr.getVariableName());
                }
                List<Item> results = getItemsFromIteratorWithCurrentContext(iterator);
                this._hasNext = false;
                return ItemFactory.getInstance().createIntegerItem(results.size());
            } else {
                Long count = iterator.getRDD(_currentDynamicContext).count();
                this._hasNext = false;
                if (count > (long) Integer.MAX_VALUE) {
                    // TODO: handle too big x values
                    throw new SparksoniqRuntimeException("The count value is too big to convert to integer type.");
                } else {
                    return ItemFactory.getInstance().createIntegerItem(count.intValue());
                }
            }
        } else
            throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + " count function",
                    getMetadata());
    }

    public Map<String, RuntimeIterator.VariableDependency> getVariableDependencies()
    {
        if(_children.get(0) instanceof VariableReferenceIterator)
        {
            VariableReferenceIterator expr = (VariableReferenceIterator) _children.get(0);
            Map<String, RuntimeIterator.VariableDependency> result = new TreeMap<String, RuntimeIterator.VariableDependency>();
            result.put(expr.getVariableName(), RuntimeIterator.VariableDependency.COUNT);
            return result;
        } else {
            return super.getVariableDependencies();
        }
    }
}
