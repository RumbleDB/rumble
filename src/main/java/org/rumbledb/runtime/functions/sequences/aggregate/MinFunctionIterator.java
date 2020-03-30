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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.SparksoniqRuntimeException;
import org.rumbledb.items.ItemComparatorForSequences;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import sparksoniq.jsoniq.ExecutionMode;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MinFunctionIterator extends LocalFunctionCallIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;

    public MinFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        this.iterator = this.children.get(0);
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.iterator.hasNext();
        this.iterator.close();
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            ItemComparatorForSequences comparator = new ItemComparatorForSequences();
            if (!this.iterator.isRDD()) {
                List<Item> results = this.iterator.materialize(this.currentDynamicContextForLocalExecution);

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
                    return this.iterator.getRDD(this.currentDynamicContextForLocalExecution).min(comparator);
                } catch (SparksoniqRuntimeException e) {
                    throw new InvalidArgumentTypeException(
                            "Min expression input error. Input has to be non-null atomics of matching types: "
                                + e.getMessage(),
                            getMetadata()
                    );
                }
            }
        } else {
            throw new IteratorFlowException(
                    FLOW_EXCEPTION_MESSAGE + "MIN function",
                    getMetadata()
            );
        }
    }

    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        if (this.children.get(0) instanceof VariableReferenceIterator) {
            VariableReferenceIterator expr = (VariableReferenceIterator) this.children.get(0);
            Map<String, DynamicContext.VariableDependency> result =
                new TreeMap<String, DynamicContext.VariableDependency>();
            result.put(expr.getVariableName(), DynamicContext.VariableDependency.MIN);
            return result;
        } else {
            return super.getVariableDependencies();
        }
    }
}
