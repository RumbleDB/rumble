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
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.functions.DynamicFunctionCallIterator;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ForEachFunctionIterator extends HybridRuntimeIterator implements DataFrameRuntimePlan<Item> {

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ForEachLocalCursor(
                this.sequenceIterator,
                this.actionIterator,
                context,
                getRuntimeStaticContext()
        );
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator sequenceIterator;
    private final RuntimeIterator actionIterator;

    public ForEachFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2) {
            throw new OurBadException("fn:for-each must have exactly two arguments.");
        }
        this.sequenceIterator = arguments.get(0);
        this.actionIterator = arguments.get(1);
    }

    private static Item resolveAction(
            RuntimeIterator actionIterator,
            DynamicContext context,
            RuntimeStaticContext staticContext
    ) {
        List<Item> functionItems = actionIterator.materialize(context);
        if (functionItems.size() != 1) {
            throw new UnexpectedTypeException(
                    "The second argument of fn:for-each must be a single function item [err:XPTY0004].",
                    staticContext.getMetadata()
            );
        }
        Item function = functionItems.get(0);
        if (!acceptsSingleArgument(function)) {
            throw new UnexpectedTypeException(
                    "The function passed to fn:for-each must accept exactly one argument [err:XPTY0004].",
                    staticContext.getMetadata()
            );
        }

        return function;
    }

    private static List<Item> invokeAction(
            Item function,
            Item item,
            DynamicContext context,
            RuntimeStaticContext staticContext
    ) {
        RuntimeStaticContext argumentContext = RuntimeStaticContext.builder()
            .configuration(staticContext.getConfiguration())
            .staticType(SequenceType.createSequenceType("item"))
            .executionMode(ExecutionMode.LOCAL)
            .metadata(staticContext.getMetadata())
            .build();
        List<RuntimeIterator> callbackArguments = new ArrayList<>(1);
        callbackArguments.add(new ConstantRuntimeIterator(item, argumentContext));
        RuntimeStaticContext functionItemContext = RuntimeStaticContext.builder()
            .configuration(staticContext.getConfiguration())
            .staticType(SequenceType.createSequenceType("item*"))
            .executionMode(ExecutionMode.LOCAL)
            .metadata(staticContext.getMetadata())
            .build();
        RuntimeIterator callback = new DynamicFunctionCallIterator(
                new ConstantRuntimeIterator(function, functionItemContext),
                callbackArguments,
                functionItemContext
        );
        return callback.materialize(context);
    }

    private static boolean acceptsSingleArgument(Item item) {
        if (item.isMap() || item.isArray()) {
            return true;
        }
        return item.isFunction() && item.getIdentifier().getArity() == 1;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("fn:for-each is currently supported only in local execution mode.");
    }

    @Override
    public JSoundDataFrame getNativeDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("fn:for-each is currently supported only in local execution mode.");
    }

    private static final class ForEachLocalCursor extends AbstractLocalCursor<Item> {

        private final RuntimeIterator sequencePlan;
        private final RuntimeIterator actionPlan;
        private final DynamicContext context;
        private final RuntimeStaticContext staticContext;
        private LocalCursor<Item> sequenceCursor;
        private Item action;
        private Iterator<Item> currentResults;

        private ForEachLocalCursor(
                RuntimeIterator sequencePlan,
                RuntimeIterator actionPlan,
                DynamicContext context,
                RuntimeStaticContext staticContext
        ) {
            super(staticContext.getMetadata());
            this.sequencePlan = sequencePlan;
            this.actionPlan = actionPlan;
            this.context = context;
            this.staticContext = staticContext;
        }

        @Override
        protected void openLocal() {
            this.action = resolveAction(this.actionPlan, this.context, this.staticContext);
            this.sequenceCursor = this.sequencePlan.createLocalCursor(this.context);
            this.currentResults = Collections.emptyIterator();
        }

        @Override
        protected boolean hasNextLocal() {
            while (!this.currentResults.hasNext() && this.sequenceCursor.hasNext()) {
                this.currentResults = invokeAction(
                    this.action,
                    this.sequenceCursor.next(),
                    this.context,
                    this.staticContext
                ).iterator();
            }
            return this.currentResults.hasNext();
        }

        @Override
        protected Item nextLocal() {
            if (!hasNextLocal()) {
                throw invalidState("No more fn:for-each results are available.");
            }
            return this.currentResults.next();
        }

        @Override
        protected void closeLocal() {
            if (this.sequenceCursor != null) {
                this.sequenceCursor.close();
                this.sequenceCursor = null;
            }
            this.action = null;
            this.currentResults = null;
        }
    }
}
