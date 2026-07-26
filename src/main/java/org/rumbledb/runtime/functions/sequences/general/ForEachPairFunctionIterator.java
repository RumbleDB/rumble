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

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.List;

public class ForEachPairFunctionIterator extends HybridRuntimeIterator {

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ForEachPairLocalCursor(this, context);
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator sequenceIterator1;
    private final RuntimeIterator sequenceIterator2;
    private final RuntimeIterator actionIterator;

    public ForEachPairFunctionIterator(
            List<RuntimeIterator> arguments,
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

    private Item resolveAction(DynamicContext context) {
        List<Item> functionItems = LocalCursorUtils.materialize(this.actionIterator, context);
        if (functionItems.size() != 1 || !functionItems.get(0).isFunction()) {
            throw new UnexpectedTypeException(
                    "The third argument of fn:for-each-pair must be a single function item [err:XPTY0004].",
                    getMetadata()
            );
        }
        Item actionFunction = functionItems.get(0);
        if (actionFunction.getIdentifier().getArity() != 2) {
            throw new UnexpectedTypeException(
                    "The function passed to fn:for-each-pair must accept exactly two arguments [err:XPTY0004].",
                    getMetadata()
            );
        }

        return actionFunction;
    }

    private RuntimeStaticContext argumentContext() {
        return RuntimeStaticContext.builder()
            .configuration(getConfiguration())
            .staticType(SequenceType.createSequenceType("item"))
            .executionMode(ExecutionMode.LOCAL)
            .metadata(getMetadata())
            .build();
    }

    private RuntimeIterator buildCallback(
            Item action,
            Item first,
            Item second,
            RuntimeStaticContext argumentContext
    ) {
        return NamedFunctions.buildFunctionItemCallIterator(
            action,
            this.staticContext,
            ExecutionMode.LOCAL,
            List.of(
                new ConstantRuntimeIterator(first, argumentContext),
                new ConstantRuntimeIterator(second, argumentContext)
            ),
            false
        );
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("fn:for-each-pair is currently supported only in local execution mode.");
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("fn:for-each-pair is currently supported only in local execution mode.");
    }

    private static final class ForEachPairLocalCursor extends AbstractLocalCursor<Item> {

        private final ForEachPairFunctionIterator plan;
        private final DynamicContext context;
        private List<Item> firstItems;
        private List<Item> secondItems;
        private Item action;
        private RuntimeStaticContext argumentContext;
        private int pairIndex;
        private LocalCursor<Item> callbackCursor;

        private ForEachPairLocalCursor(
                ForEachPairFunctionIterator plan,
                DynamicContext context
        ) {
            super(plan.getMetadata());
            this.plan = plan;
            this.context = context;
        }

        @Override
        protected void openLocal() {
            this.firstItems = LocalCursorUtils.materialize(
                this.plan.sequenceIterator1,
                this.context
            );
            this.secondItems = LocalCursorUtils.materialize(
                this.plan.sequenceIterator2,
                this.context
            );
            this.action = this.plan.resolveAction(this.context);
            this.argumentContext = this.plan.argumentContext();
            this.pairIndex = 0;
        }

        @Override
        protected boolean hasNextLocal() {
            while (this.callbackCursor == null || !this.callbackCursor.hasNext()) {
                closeCallback();
                if (this.pairIndex >= Math.min(this.firstItems.size(), this.secondItems.size())) {
                    return false;
                }
                RuntimeIterator callback = this.plan.buildCallback(
                    this.action,
                    this.firstItems.get(this.pairIndex),
                    this.secondItems.get(this.pairIndex),
                    this.argumentContext
                );
                this.pairIndex++;
                this.callbackCursor = callback.createLocalCursor(this.context);
                this.callbackCursor.open();
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
