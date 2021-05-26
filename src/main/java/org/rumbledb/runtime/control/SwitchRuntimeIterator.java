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

package org.rumbledb.runtime.control;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.misc.ComparisonIterator;

import java.util.Map;


public class SwitchRuntimeIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final RuntimeIterator testField;
    private final Map<RuntimeIterator, RuntimeIterator> cases;
    private final RuntimeIterator defaultReturn;
    private RuntimeIterator matchingIterator = null;

    public SwitchRuntimeIterator(
            RuntimeIterator test,
            Map<RuntimeIterator, RuntimeIterator> cases,
            RuntimeIterator defaultReturn,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        this.children.add(test);
        this.children.addAll(cases.keySet());
        this.children.addAll(cases.values());
        this.children.add(defaultReturn);
        this.testField = test;
        this.cases = cases;
        this.defaultReturn = defaultReturn;
    }

    @Override
    public void openLocal() {
        this.matchingIterator = selectApplicableIterator(this.currentDynamicContextForLocalExecution);
        this.matchingIterator.open(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.matchingIterator.hasNext();
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            Item nextItem = this.matchingIterator.next();
            this.hasNext = this.matchingIterator.hasNext();
            return nextItem;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in switch statement",
                getMetadata()
        );
    }

    @Override
    public void closeLocal() {
        this.matchingIterator.close();
    }

    @Override
    public void resetLocal() {
        this.matchingIterator.close();
        this.matchingIterator = selectApplicableIterator(this.currentDynamicContextForLocalExecution);
        this.matchingIterator.open(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.matchingIterator.hasNext();
    }

    private RuntimeIterator selectApplicableIterator(
            DynamicContext dynamicContext
    ) {
        Item testValue = this.testField.materializeFirstItemOrNull(dynamicContext);

        if (testValue != null) {
            if (testValue.isArray()) {
                throw new NonAtomicKeyException(
                        "Invalid args. Switch condition cannot be an array type",
                        getMetadata()
                );
            } else if (testValue.isObject()) {
                throw new NonAtomicKeyException(
                        "Invalid args. Switch condition cannot be an object type",
                        getMetadata()
                );
            }
        }

        for (RuntimeIterator caseKey : this.cases.keySet()) {
            Item caseValue = caseKey.materializeFirstItemOrNull(dynamicContext);

            if (caseValue != null) {
                if (caseValue.isArray()) {
                    throw new NonAtomicKeyException(
                            "Invalid args. Switch case cannot be an array type",
                            getMetadata()
                    );
                } else if (caseValue.isObject()) {
                    throw new NonAtomicKeyException(
                            "Invalid args. Switch case  cannot be an object type",
                            getMetadata()
                    );
                }
            }

            // both are empty sequences
            if (testValue == null) {
                if (caseValue == null) {
                    return this.cases.get(caseKey);
                } else {
                    break;
                }
            }
            long comparison = ComparisonIterator.compareItems(
                testValue,
                caseValue,
                ComparisonOperator.VC_EQ,
                getMetadata()
            );
            if (comparison == 0) {
                return this.cases.get(caseKey);
            }
        }

        return this.defaultReturn;
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        RuntimeIterator iterator = selectApplicableIterator(dynamicContext);

        return iterator.getRDD(dynamicContext);
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        RuntimeIterator iterator = selectApplicableIterator(dynamicContext);

        return iterator.getDataFrame(dynamicContext);
    }
}
