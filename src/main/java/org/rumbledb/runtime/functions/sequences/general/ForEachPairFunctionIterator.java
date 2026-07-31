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

package org.rumbledb.runtime.functions.sequences.general;

import org.rumbledb.runtime.HybridRuntimeIterator;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.List;

public class ForEachPairFunctionIterator extends HybridRuntimeIterator
        implements
            DataFrameRuntimePlan<Item> {

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new ForEachPairLocalCursor(
                this.sequenceIterator1,
                this.sequenceIterator2,
                this.actionIterator,
                this.staticContext,
                context
        );
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> sequenceIterator1;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> sequenceIterator2;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> actionIterator;

    public ForEachPairFunctionIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 3) {
            throw new OurBadException("fn:for-each-pair must have exactly three arguments.");
        }
        this.sequenceIterator1 = arguments.get(0);
        this.sequenceIterator2 = arguments.get(1);
        this.actionIterator = arguments.get(2);
    }

    private static Item resolveAction(
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> actionPlan,
            RuntimeStaticContext staticContext,
            DynamicContext context
    ) {
        List<Item> functionItems = actionPlan.materialize(context);
        if (functionItems.size() != 1 || !functionItems.get(0).isFunction()) {
            throw new UnexpectedTypeException(
                    "The third argument of fn:for-each-pair must be a single function item [err:XPTY0004].",
                    staticContext.getMetadata()
            );
        }
        Item actionFunction = functionItems.get(0);
        if (actionFunction.getIdentifier().getArity() != 2) {
            throw new UnexpectedTypeException(
                    "The function passed to fn:for-each-pair must accept exactly two arguments [err:XPTY0004].",
                    staticContext.getMetadata()
            );
        }

        return actionFunction;
    }

    private static RuntimeStaticContext argumentContext(RuntimeStaticContext staticContext) {
        return RuntimeStaticContext.builder()
            .configuration(staticContext.getConfiguration())
            .staticType(SequenceType.createSequenceType("item"))
            .executionMode(ExecutionMode.LOCAL)
            .metadata(staticContext.getMetadata())
            .build();
    }

    private static org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> buildCallback(
            Item action,
            Item first,
            Item second,
            RuntimeStaticContext argumentContext,
            RuntimeStaticContext staticContext
    ) {
        return NamedFunctions.buildFunctionItemCallIterator(
            action,
            staticContext,
            ExecutionMode.LOCAL,
            List.of(
                new ConstantRuntimeIterator(first, argumentContext),
                new ConstantRuntimeIterator(second, argumentContext)
            ),
            false
        );
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("fn:for-each-pair is currently supported only in local execution mode.");
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("fn:for-each-pair is currently supported only in local execution mode.");
    }

    private static final class ForEachPairLocalCursor extends AbstractLocalCursor<Item> {

        private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> firstPlan;
        private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> secondPlan;
        private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> actionPlan;
        private final RuntimeStaticContext staticContext;
        private final DynamicContext context;
        private List<Item> firstItems;
        private List<Item> secondItems;
        private Item action;
        private RuntimeStaticContext argumentContext;
        private int pairIndex;
        private Cursor<Item> callbackCursor;

        private ForEachPairLocalCursor(
                org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> firstPlan,
                org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> secondPlan,
                org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> actionPlan,
                RuntimeStaticContext staticContext,
                DynamicContext context
        ) {
            super(staticContext.getMetadata());
            this.firstPlan = firstPlan;
            this.secondPlan = secondPlan;
            this.actionPlan = actionPlan;
            this.staticContext = staticContext;
            this.context = context;
        }

        @Override
        protected void openLocal() {
            this.firstItems = this.firstPlan.materialize(this.context);
            this.secondItems = this.secondPlan.materialize(this.context);
            this.action = resolveAction(this.actionPlan, this.staticContext, this.context);
            this.argumentContext = argumentContext(this.staticContext);
            this.pairIndex = 0;
        }

        @Override
        protected boolean hasNextLocal() {
            while (this.callbackCursor == null || !this.callbackCursor.hasNext()) {
                closeCallback();
                if (this.pairIndex >= Math.min(this.firstItems.size(), this.secondItems.size())) {
                    return false;
                }
                org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> callback = buildCallback(
                    this.action,
                    this.firstItems.get(this.pairIndex),
                    this.secondItems.get(this.pairIndex),
                    this.argumentContext,
                    this.staticContext
                );
                this.pairIndex++;
                this.callbackCursor = callback.getCursor(this.context);
            }
            return true;
        }

        @Override
        protected Item nextLocal() {
            if (!hasNextLocal()) {
                throw invalidState("No more fn:for-each-pair results are available.");
            }
            return this.callbackCursor.next();
        }

        private void closeCallback() {
            if (this.callbackCursor != null) {
                this.callbackCursor.close();
                this.callbackCursor = null;
            }
        }

        @Override
        protected void closeLocal() {
            closeCallback();
            this.firstItems = null;
            this.secondItems = null;
            this.action = null;
            this.argumentContext = null;
            this.pairIndex = 0;
        }
    }
}
