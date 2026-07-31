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
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.AbstractItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.misc.AtomicDeepEqual;

import java.io.Serial;
import java.util.Map;
import java.util.stream.Stream;


public class SwitchRuntimeIterator extends AbstractItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item>,
            DataFrameRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> testField;
    private final Map<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>, org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> cases;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> defaultReturn;

    public SwitchRuntimeIterator(
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> test,
            Map<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>, org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> cases,
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> defaultReturn,
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
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new SwitchLocalCursor(this.testField, this.cases, this.defaultReturn, context, getMetadata());
    }

    private static final class SwitchLocalCursor extends AbstractLocalCursor<Item> {
        private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> testPlan;
        private final Map<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>, org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> cases;
        private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> defaultPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private Cursor<Item> selected;

        private SwitchLocalCursor(
                org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> testPlan,
                Map<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>, org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> cases,
                org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> defaultPlan,
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
            this.selected = selectApplicablePlan().getCursor(this.context);
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

        private org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> selectApplicablePlan() {
            Item testValue = this.testPlan.materializeFirstOrNull(this.context);
            validateAtomic(testValue, "Switch condition");
            for (org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> caseKey : this.cases.keySet()) {
                Item caseValue = caseKey.materializeFirstOrNull(this.context);
                validateAtomic(caseValue, "Switch case");
                if (testValue == null) {
                    if (caseValue == null) {
                        return this.cases.get(caseKey);
                    }
                    break;
                }
                if (AtomicDeepEqual.deepEqual(testValue, caseValue)) {
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

    private org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> selectApplicableIterator(
            DynamicContext dynamicContext
    ) {
        Item testValue = this.testField.materializeFirstOrNull(dynamicContext);

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

        for (org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> caseKey : this.cases.keySet()) {
            Item caseValue = caseKey.materializeFirstOrNull(dynamicContext);

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
            if (AtomicDeepEqual.deepEqual(testValue, caseValue)) {
                return this.cases.get(caseKey);
            }
        }

        return this.defaultReturn;
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext dynamicContext) {
        org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> iterator = selectApplicableIterator(
            dynamicContext
        );

        return iterator.getRDD(dynamicContext);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> iterator = selectApplicableIterator(
            dynamicContext
        );

        return org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(iterator, dynamicContext);
    }
}
