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
import sparksoniq.exceptions.InvalidArgumentTypeException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.ItemComparatorForSequences;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MinFunctionIterator extends LocalFunctionCallIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator _iterator;

    public MinFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _iterator = this._children.get(0);
        _iterator.open(_currentDynamicContextForLocalExecution);
        this._hasNext = _iterator.hasNext();
        _iterator.close();
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            ItemComparatorForSequences comparator = new ItemComparatorForSequences();
            if (!_iterator.isRDD()) {
                List<Item> results = getItemsFromIteratorWithCurrentContext(_iterator);

                try {
                    return Collections.min(results, comparator);
                } catch (SparksoniqRuntimeException e) {
                    throw new InvalidArgumentTypeException(
                            "Min expression input error. Input has to be non-null atomics of matching types: "
                                + e.getMessage(),
                            getMetadata()
                    );
                }
            } else {
                try {
                    return _iterator.getRDD(_currentDynamicContextForLocalExecution).min(comparator);
                } catch (SparksoniqRuntimeException e) {
                    throw new InvalidArgumentTypeException(
                            "Min expression input error. Input has to be non-null atomics of matching types: "
                                + e.getMessage(),
                            getMetadata()
                    );
                }
            }
        } else
            throw new IteratorFlowException(
                    FLOW_EXCEPTION_MESSAGE + "MIN function",
                    getMetadata()
            );
    }

    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        if (_children.get(0) instanceof VariableReferenceIterator) {
            VariableReferenceIterator expr = (VariableReferenceIterator) _children.get(0);
            Map<String, DynamicContext.VariableDependency> result =
                new TreeMap<String, DynamicContext.VariableDependency>();
            result.put(expr.getVariableName(), DynamicContext.VariableDependency.MIN);
            return result;
        } else {
            return super.getVariableDependencies();
        }
    }
}
