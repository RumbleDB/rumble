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
 */

package org.rumbledb.runtime.functions.arrays;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.CommaExpressionIterator;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayFoldLeftFunctionIterator extends HybridRuntimeIterator {

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(() -> computeResult(context).iterator(), getMetadata());
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator arrayIterator;
    private final RuntimeIterator zeroIterator;
    private final RuntimeIterator functionIterator;

    public ArrayFoldLeftFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 3) {
            throw new OurBadException("array:fold-left must have exactly three arguments.");
        }
        this.arrayIterator = arguments.get(0);
        this.zeroIterator = arguments.get(1);
        this.functionIterator = arguments.get(2);
    }

    private List<Item> computeResult(DynamicContext context) {
        Item arrayItem;
        try {
            arrayItem = LocalCursorUtils.materializeAtMostOne(this.arrayIterator, context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:fold-left expects exactly one array argument.",
                    getMetadata()
            );
        }
        if (arrayItem == null) {
            return Collections.emptyList();
        }

        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; argument to array:fold-left must be an array.",
                    getMetadata()
            );
        }

        List<List<Item>> memberSequences = arrayItem.getSequenceMembers();

        List<Item> accumulator = LocalCursorUtils.materialize(this.zeroIterator, context);

        List<Item> functionItems = LocalCursorUtils.materialize(this.functionIterator, context);
        if (functionItems.isEmpty()) {
            throw new UnexpectedTypeException(
                    "Type error; third argument to array:fold-left must be a function item.",
                    getMetadata()
            );
        }
        if (functionItems.size() != 1 || !functionItems.get(0).isFunction()) {
            throw new UnexpectedTypeException(
                    "Type error; third argument to array:fold-left must be a single function item.",
                    getMetadata()
            );
        }

        FunctionItem functionItem = (FunctionItem) functionItems.get(0);

        for (List<Item> memberSequence : memberSequences) {
            accumulator = applyFunction(functionItem, accumulator, memberSequence, context);
        }

        return accumulator;
    }

    private RuntimeIterator createSequenceIterator(List<Item> items) {
        if (items.isEmpty()) {
            RuntimeStaticContext staticContext = RuntimeStaticContext.builder()
                .configuration(getConfiguration())
                .staticType(SequenceType.createSequenceType("item*"))
                .executionMode(ExecutionMode.LOCAL)
                .metadata(getMetadata())
                .build();
            return new CommaExpressionIterator(Collections.emptyList(), staticContext);
        }

        List<RuntimeIterator> childIterators = new ArrayList<>(items.size());
        for (Item item : items) {
            RuntimeStaticContext childStaticContext = RuntimeStaticContext.builder()
                .configuration(getConfiguration())
                .staticType(SequenceType.createSequenceType("item*"))
                .executionMode(ExecutionMode.LOCAL)
                .metadata(getMetadata())
                .build();
            childIterators.add(new ConstantRuntimeIterator(item, childStaticContext));
        }

        RuntimeStaticContext staticContext = RuntimeStaticContext.builder()
            .configuration(getConfiguration())
            .staticType(SequenceType.createSequenceType("item*"))
            .executionMode(ExecutionMode.LOCAL)
            .metadata(getMetadata())
            .build();
        return new CommaExpressionIterator(childIterators, staticContext);
    }

    private List<Item> applyFunction(
            FunctionItem functionItem,
            List<Item> accumulator,
            List<Item> memberSequence,
            DynamicContext context
    ) {
        RuntimeIterator accIterator = createSequenceIterator(accumulator);
        RuntimeIterator memberIterator = createSequenceIterator(memberSequence);

        List<RuntimeIterator> arguments = new ArrayList<>(2);
        arguments.add(accIterator);
        arguments.add(memberIterator);

        RuntimeIterator functionCall = NamedFunctions.buildFunctionItemCallIterator(
            functionItem,
            this.staticContext,
            ExecutionMode.LOCAL,
            arguments,
            false
        );
        return LocalCursorUtils.materialize(functionCall, context);
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:fold-left is currently supported only in local execution mode."
        );
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:fold-left is currently supported only in local execution mode."
        );
    }
}
