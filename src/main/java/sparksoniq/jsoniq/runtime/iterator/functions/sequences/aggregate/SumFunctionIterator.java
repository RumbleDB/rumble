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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.semantics.DynamicContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SumFunctionIterator extends LocalFunctionCallIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator _iterator;
    private Item _zeroItem;

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

        this._iterator = this._children.get(0);
        this._iterator.open(context);
        if (!this._iterator.hasNext()) {
            if (this._children.size() > 1) {
                RuntimeIterator zeroIterator = this._children.get(1);
                zeroIterator.open(this._currentDynamicContextForLocalExecution);
                if (!zeroIterator.hasNext()) {
                    this._hasNext = false;
                    return;
                } else {
                    this._zeroItem = zeroIterator.next();
                    if (!this._zeroItem.isAtomic()) {
                        throw new NonAtomicKeyException(
                                "Invalid args. Zero item has to be of an atomic type",
                                getMetadata()
                        );
                    }
                }
            }
        }
        this._iterator.close();
        this._hasNext = true;
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            List<Item> results = this._iterator.materialize(this._currentDynamicContextForLocalExecution);

            // if input is empty sequence and _zeroItem is given
            if (results.size() == 0 && this._zeroItem != null) {
                return this._zeroItem;
            }

            results.forEach(r -> {
                if (!r.isNumeric())
                    throw new InvalidArgumentTypeException(
                            "Sum expression has non numeric args "
                                +
                                r.serialize(),
                            getMetadata()
                    );
            });
            try {
                // if input is empty sequence and _zeroItem is not given 0 is returned
                BigDecimal sumResult = new BigDecimal(0);
                for (Item r : results) {
                    BigDecimal current = r.castToDecimalValue();
                    sumResult = sumResult.add(current);
                }
                return ItemFactory.getInstance().createDecimalItem(sumResult);

            } catch (IteratorFlowException e) {
                throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
            }
        } else
            throw new IteratorFlowException(
                    FLOW_EXCEPTION_MESSAGE + "SUM function",
                    getMetadata()
            );
    }

    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        if (this._children.get(0) instanceof VariableReferenceIterator) {
            VariableReferenceIterator expr = (VariableReferenceIterator) this._children.get(0);
            Map<String, DynamicContext.VariableDependency> result =
                new TreeMap<String, DynamicContext.VariableDependency>();
            result.put(expr.getVariableName(), DynamicContext.VariableDependency.SUM);
            return result;
        } else {
            return super.getVariableDependencies();
        }
    }
}
