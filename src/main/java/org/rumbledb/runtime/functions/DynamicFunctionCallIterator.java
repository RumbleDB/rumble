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

package org.rumbledb.runtime.functions;

import java.io.Serial;
import java.util.List;
import java.util.stream.Stream;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.BuiltinFunction;
import org.rumbledb.context.BuiltinFunctionCatalogue;
import org.rumbledb.context.BuiltinFunctionExecutionModes;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExitStatementException;
import org.rumbledb.exceptions.InvalidRumbleMLParamException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.functions.arrays.ArrayFunctionCallIterator;
import org.rumbledb.runtime.functions.maps.MapFunctionCallIterator;
import org.rumbledb.types.SequenceType;

public class DynamicFunctionCallIterator extends HybridRuntimeIterator implements DataFrameRuntimePlan<Item> {
    // dynamic: functionIdentifier is not known at compile time
    // it is known only after evaluating postfix expression at runtime

    @Serial
    private static final long serialVersionUID = 1L;
    // parametrized fields
    private final RuntimeIterator functionItemIterator;
    private final List<RuntimeIterator> functionArguments;
    private final boolean isPartialApplication;

    public DynamicFunctionCallIterator(
            RuntimeIterator functionItemIterator,
            List<RuntimeIterator> functionArguments,
            RuntimeStaticContext staticContext
    ) {
        super(
            Stream.concat(
                functionArguments.stream().filter(arg -> arg != null),
                functionArguments.contains(functionItemIterator)
                    ? Stream.empty()
                    : Stream.of(functionItemIterator)
            ).toList(),
            staticContext
        );

        this.isPartialApplication = functionArguments.stream().anyMatch(arg -> arg == null);
        this.functionItemIterator = functionItemIterator;
        this.functionArguments = functionArguments;
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new DynamicCallLocalCursor(
                this.functionItemIterator,
                this.functionArguments,
                this.isPartialApplication,
                getHighestExecutionMode(),
                this.staticContext,
                context
        );
    }

    private FunctionCall resolveFunctionCall(DynamicContext context) {
        return resolveFunctionCall(
            this.functionItemIterator,
            this.functionArguments,
            this.isPartialApplication,
            getHighestExecutionMode(),
            this.staticContext,
            context
        );
    }

    private static FunctionCall resolveFunctionCall(
            RuntimeIterator functionItemPlan,
            List<RuntimeIterator> functionArguments,
            boolean partialApplication,
            ExecutionMode callerExecutionMode,
            RuntimeStaticContext staticContext,
            DynamicContext context
    ) {
        Item functionItem;
        try {
            functionItem = functionItemPlan.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "A dynamic function call can not be performed on a sequence of more than one item.",
                    staticContext.getMetadata()
            );
        }
        if (functionItem == null) {
            throw new UnexpectedTypeException(
                    "Dynamic function calls can only be performed on functions, arrays, or maps.",
                    staticContext.getMetadata()
            );
        }
        if (functionItem.isArray()) {
            if (partialApplication) {
                throw new UnexpectedTypeException(
                        "Partial application is not supported when calling arrays as functions.",
                        staticContext.getMetadata()
                );
            }
            if (functionArguments.size() != 1 || functionArguments.get(0) == null) {
                throw new UnexpectedTypeException(
                        "Array function calls must have exactly one argument.",
                        staticContext.getMetadata()
                );
            }
            RuntimeIterator keyIterator = functionArguments.get(0);
            RuntimeStaticContext callStaticContext = RuntimeStaticContext.builder()
                .configuration(staticContext.getConfiguration())
                .staticType(SequenceType.createSequenceType("item*"))
                .executionMode(ExecutionMode.LOCAL)
                .metadata(staticContext.getMetadata())
                .build();
            return new FunctionCall(
                    functionItem,
                    new ArrayFunctionCallIterator(
                            functionItem,
                            keyIterator,
                            callStaticContext
                    )
            );
        }
        if (functionItem.isMap()) {
            if (partialApplication) {
                throw new UnexpectedTypeException(
                        "Partial application is not supported when calling maps as functions.",
                        staticContext.getMetadata()
                );
            }
            if (functionArguments.size() != 1 || functionArguments.get(0) == null) {
                throw new UnexpectedTypeException(
                        "Map function calls must have exactly one argument.",
                        staticContext.getMetadata()
                );
            }
            RuntimeIterator keyIterator = functionArguments.get(0);
            RuntimeStaticContext callStaticContext = RuntimeStaticContext.builder()
                .configuration(staticContext.getConfiguration())
                .staticType(SequenceType.createSequenceType("item*"))
                .executionMode(ExecutionMode.LOCAL)
                .metadata(staticContext.getMetadata())
                .build();
            return new FunctionCall(
                    functionItem,
                    new MapFunctionCallIterator(
                            functionItem,
                            keyIterator,
                            callStaticContext
                    )
            );
        }
        if (!functionItem.isFunction()) {
            throw new UnexpectedTypeException(
                    "Dynamic function calls can only be performed on functions, arrays, or maps.",
                    staticContext.getMetadata()
            );
        }
        ExecutionMode calleeExecutionMode = getCalleeExecutionModeForFunctionItemCall(
            functionItem,
            functionArguments,
            partialApplication,
            staticContext
        );
        if (
            calleeExecutionMode.equals(ExecutionMode.LOCAL)
                && callerExecutionMode.equals(ExecutionMode.DATAFRAME)
        ) {
            throw new OurBadException(
                    "Execution mode mismatch in dynamic function call. At this point, Rumble only supports higher-order functions "
                        + "that are either machine learning models or estimators (which process validated and structured sequences of objects at any scale), or that take and return just one item at a time.",
                    staticContext.getMetadata()
            );
        }
        return new FunctionCall(
                functionItem,
                NamedFunctions.buildFunctionItemCallIterator(
                    functionItem,
                    staticContext,
                    partialApplication ? ExecutionMode.LOCAL : calleeExecutionMode,
                    functionArguments,
                    false
                )
        );
    }

    private static ExecutionMode getCalleeExecutionModeForFunctionItemCall(
            Item functionItem,
            List<RuntimeIterator> functionArguments,
            boolean partialApplication,
            RuntimeStaticContext staticContext
    ) {
        if (partialApplication) {
            return ExecutionMode.LOCAL;
        }
        if (functionItem.isBuiltinFunction()) {
            BuiltinFunction builtin =
                BuiltinFunctionCatalogue.getBuiltinFunction(
                    functionItem.getIdentifier(),
                    staticContext.getQueryLanguage()
                );
            // assume that the passed builtin function is valid
            ExecutionMode firstArgumentMode = ExecutionMode.LOCAL;
            for (RuntimeIterator arg : functionArguments) {
                if (arg != null) {
                    firstArgumentMode = arg.getHighestExecutionMode();
                    break;
                }
            }
            return BuiltinFunctionExecutionModes.resolve(
                builtin,
                firstArgumentMode,
                staticContext.getConfiguration()
            );
        }
        if (functionItem.getBodyIterator() instanceof FunctionCoercionRuntimeIterator coercionRuntimeIterator) {
            return coercionRuntimeIterator.getWrappedCallableExecutionMode();
        }
        return functionItem.getBodyIterator().getHighestExecutionMode();
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        try {
            return resolveFunctionCall(dynamicContext).iterator.getRDD(dynamicContext);
        } catch (ExitStatementException exitStatementException) {
            return exitStatementException.getRddResult();
        }
    }

    @Override
    public HomogeneousItemDataFrame getNativeDataFrame(DynamicContext dynamicContext) {
        try {
            return resolveFunctionCall(dynamicContext).iterator.getDataFrame(dynamicContext);
        } catch (InvalidRumbleMLParamException e) {
            String m = e.getMLMessage();
            throw new InvalidRumbleMLParamException(m, getMetadata());
        } catch (ExitStatementException exitStatementException) {
            return exitStatementException.getDataFrameResult();
        }
    }

    private static final class FunctionCall {
        private final Item functionItem;
        private final RuntimeIterator iterator;

        private FunctionCall(Item functionItem, RuntimeIterator iterator) {
            this.functionItem = functionItem;
            this.iterator = iterator;
        }
    }

    private static final class DynamicCallLocalCursor extends AbstractLocalCursor<Item> {

        private final RuntimeIterator functionItemPlan;
        private final List<RuntimeIterator> functionArguments;
        private final boolean partialApplication;
        private final ExecutionMode callerExecutionMode;
        private final RuntimeStaticContext staticContext;
        private final DynamicContext context;
        private FunctionCall call;
        private Cursor<Item> delegate;
        private List<Item> exitResults;
        private int exitIndex;

        private DynamicCallLocalCursor(
                RuntimeIterator functionItemPlan,
                List<RuntimeIterator> functionArguments,
                boolean partialApplication,
                ExecutionMode callerExecutionMode,
                RuntimeStaticContext staticContext,
                DynamicContext context
        ) {
            super(staticContext.getMetadata());
            this.functionItemPlan = functionItemPlan;
            this.functionArguments = functionArguments;
            this.partialApplication = partialApplication;
            this.callerExecutionMode = callerExecutionMode;
            this.staticContext = staticContext;
            this.context = context;
        }

        @Override
        protected void openLocal() {
            this.call = resolveFunctionCall(
                this.functionItemPlan,
                this.functionArguments,
                this.partialApplication,
                this.callerExecutionMode,
                this.staticContext,
                this.context
            );
            this.delegate = this.call.iterator.getCursor(this.context);
        }

        @Override
        protected boolean hasNextLocal() {
            if (this.exitResults != null) {
                return this.exitIndex < this.exitResults.size();
            }
            try {
                return this.delegate.hasNext();
            } catch (InvalidRumbleMLParamException e) {
                throw new InvalidRumbleMLParamException(e.getMLMessage(), this.staticContext.getMetadata());
            } catch (ExitStatementException e) {
                this.exitResults = e.getLocalResult();
                return this.exitIndex < this.exitResults.size();
            }
        }

        @Override
        protected Item nextLocal() {
            if (this.exitResults != null) {
                if (this.exitIndex >= this.exitResults.size()) {
                    throw invalidState("No more dynamic function results are available.");
                }
                return this.exitResults.get(this.exitIndex++);
            }
            try {
                return this.delegate.next();
            } catch (InvalidRumbleMLParamException e) {
                throw new InvalidRumbleMLParamException(e.getMLMessage(), this.staticContext.getMetadata());
            } catch (ExitStatementException e) {
                this.exitResults = e.getLocalResult();
                return nextLocal();
            }
        }

        @Override
        protected void closeLocal() {
            if (this.delegate != null) {
                this.delegate.close();
            }
            this.delegate = null;
            this.call = null;
            this.exitResults = null;
            this.exitIndex = 0;
        }
    }
}
