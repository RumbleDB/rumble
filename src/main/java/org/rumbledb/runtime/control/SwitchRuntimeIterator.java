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
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;
import org.rumbledb.runtime.misc.ComparisonIterator;

import java.io.Serial;
import java.util.Map;
import java.util.stream.Stream;


public class SwitchRuntimeIterator extends HybridRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator testField;
    private final Map<RuntimeIterator, RuntimeIterator> cases;
    private final RuntimeIterator defaultReturn;

    public SwitchRuntimeIterator(
            RuntimeIterator test,
            Map<RuntimeIterator, RuntimeIterator> cases,
            RuntimeIterator defaultReturn,
            RuntimeStaticContext staticContext
    ) {
        super(
            Stream.concat(
                Stream.concat(Stream.of(test), cases.keySet().stream()),
                Stream.concat(cases.values().stream(), Stream.of(defaultReturn))
            ).toList(),
            staticContext
        );
        this.testField = test;
        this.cases = cases;
        this.defaultReturn = defaultReturn;
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new SwitchLocalCursor(this.testField, this.cases, this.defaultReturn, context, getMetadata());
    }

    private static final class SwitchLocalCursor extends AbstractLocalCursor<Item> {
        private final RuntimeIterator testPlan;
        private final Map<RuntimeIterator, RuntimeIterator> cases;
        private final RuntimeIterator defaultPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private LocalCursor<Item> selected;

        private SwitchLocalCursor(
                RuntimeIterator testPlan,
                Map<RuntimeIterator, RuntimeIterator> cases,
                RuntimeIterator defaultPlan,
                DynamicContext context,
                ExceptionMetadata metadata
        ) {
            super(metadata);
            this.testPlan = testPlan;
            this.cases = cases;
            this.defaultPlan = defaultPlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            this.selected = selectApplicablePlan().createLocalCursor(this.context);
        }

        @Override
        protected boolean hasNextLocal() {
            return this.selected.hasNext();
        }

        @Override
        protected Item nextLocal() {
            return this.selected.next();
        }

        @Override
        protected void closeLocal() {
            if (this.selected != null) {
                this.selected.close();
                this.selected = null;
            }
        }

        private RuntimeIterator selectApplicablePlan() {
            Item testValue = LocalCursorUtils.materializeFirst(this.testPlan, this.context);
            validateAtomic(testValue, "Switch condition");
            for (RuntimeIterator caseKey : this.cases.keySet()) {
                Item caseValue = LocalCursorUtils.materializeFirst(caseKey, this.context);
                validateAtomic(caseValue, "Switch case");
                if (testValue == null) {
                    if (caseValue == null) {
                        return this.cases.get(caseKey);
                    }
                    break;
                }
                if (
                    ComparisonIterator.compareItems(testValue, caseValue, ComparisonOperator.VC_EQ, this.metadata) == 0
                ) {
                    return this.cases.get(caseKey);
                }
            }
            return this.defaultPlan;
        }

        private void validateAtomic(Item item, String role) {
            if (item != null && item.isArray()) {
                throw new NonAtomicKeyException("Invalid args. " + role + " cannot be an array type", this.metadata);
            }
            if (item != null && item.isObject()) {
                throw new NonAtomicKeyException("Invalid args. " + role + " cannot be an object type", this.metadata);
            }
        }
    }

    private RuntimeIterator selectApplicableIterator(
            DynamicContext dynamicContext
    ) {
        Item testValue = LocalCursorUtils.materializeFirst(this.testField, dynamicContext);

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
            Item caseValue = LocalCursorUtils.materializeFirst(caseKey, dynamicContext);

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
