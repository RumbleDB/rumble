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
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import sparksoniq.jsoniq.ExecutionMode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SumFunctionIterator extends LocalFunctionCallIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private Item zeroItem;

    public SumFunctionIterator(
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
        this.iterator.open(context);
        if (!this.iterator.hasNext()) {
            if (this.children.size() > 1) {
                RuntimeIterator zeroIterator = this.children.get(1);
                zeroIterator.open(this.currentDynamicContextForLocalExecution);
                if (!zeroIterator.hasNext()) {
                    this.hasNext = false;
                    return;
                } else {
                    this.zeroItem = zeroIterator.next();
                    if (!this.zeroItem.isAtomic()) {
                        throw new NonAtomicKeyException(
                                "Invalid args. Zero item has to be of an atomic type",
                                getMetadata()
                        );
                    }
                }
            }
        }
        this.iterator.close();
        this.hasNext = true;
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            List<Item> results = this.iterator.materialize(this.currentDynamicContextForLocalExecution);

            // if input is empty sequence and zeroItem is given
            if (results.size() == 0 && this.zeroItem != null) {
                return this.zeroItem;
            }

            results.forEach(r -> {
                if (!r.isNumeric()) {
                    throw new InvalidArgumentTypeException(
                            "Sum expression has non numeric args "
                                +
                                r.serialize(),
                            getMetadata()
                    );
                }
            });
            try {
                // if input is empty sequence and zeroItem is not given 0 is returned
                BigDecimal sumResult = new BigDecimal(0);
                for (Item r : results) {
                    BigDecimal current = r.castToDecimalValue();
                    sumResult = sumResult.add(current);
                }
                return ItemFactory.getInstance().createDecimalItem(sumResult);

            } catch (IteratorFlowException e) {
                throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
            }
        } else {
            throw new IteratorFlowException(
                    FLOW_EXCEPTION_MESSAGE + "SUM function",
                    getMetadata()
            );
        }
    }

    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        if (this.children.get(0) instanceof VariableReferenceIterator) {
            VariableReferenceIterator expr = (VariableReferenceIterator) this.children.get(0);
            Map<String, DynamicContext.VariableDependency> result =
                new TreeMap<String, DynamicContext.VariableDependency>();
            result.put(expr.getVariableName(), DynamicContext.VariableDependency.SUM);
            return result;
        } else {
            return super.getVariableDependencies();
        }
    }
}
