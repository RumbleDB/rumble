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

package sparksoniq.jsoniq.runtime.iterator.functions.sequences.aggregate;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CountFunctionIterator extends LocalFunctionCallIterator {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CountFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            RuntimeIterator iterator = this._children.get(0);

            // the count($x) case is treated separately because we can short-circuit the
            // count, e.g., if it comes from the group-by aggregation of a non-grouping
            // key.
            if (iterator instanceof VariableReferenceIterator) {
                VariableReferenceIterator expr = (VariableReferenceIterator) iterator;
                this._hasNext = false;
                return _currentDynamicContext.getVariableCount(expr.getVariableName());
            }

            if (!iterator.isRDD(_currentDynamicContext)) {
                List<Item> results = getItemsFromIteratorWithCurrentContext(iterator);
                this._hasNext = false;
                return ItemFactory.getInstance().createIntegerItem(results.size());
            }

            // it is an RDD
            long count = iterator.getRDD(_currentDynamicContext).count();
            this._hasNext = false;
            if (count > (long) Integer.MAX_VALUE) {
                // TODO: handle too big x values
                throw new SparksoniqRuntimeException("The count value is too big to convert to integer type.");
            } else {
                return ItemFactory.getInstance().createIntegerItem((int) count);
            }
        } else {
            throw new IteratorFlowException(
                    FLOW_EXCEPTION_MESSAGE + " count function",
                    getMetadata()
            );
        }
    }

    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        if (_children.get(0) instanceof VariableReferenceIterator) {
            VariableReferenceIterator expr = (VariableReferenceIterator) _children.get(0);
            Map<String, DynamicContext.VariableDependency> result = new TreeMap<>();
            result.put(expr.getVariableName(), DynamicContext.VariableDependency.COUNT);
            return result;
        } else {
            return super.getVariableDependencies();
        }
    }
}
