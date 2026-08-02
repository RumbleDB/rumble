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
package org.rumbledb.runtime.functions;

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.rumbledb.api.Item;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.typing.AtMostOneItemTypePromotionIterator;
import org.rumbledb.runtime.typing.TypePromotionIterator;
import org.rumbledb.runtime.functions.sequences.general.DataFunctionIterator;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

import java.util.Collections;
import java.util.List;

/**
 * Shared argument arity checks and type-promotion wrapping for dynamic calls on
 * {@link org.rumbledb.items.FunctionItem}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FunctionCallArgumentConversion {


    public static void validateArity(
            Item functionItem,
            List<ItemRuntimePlan> functionArguments,
            ExceptionMetadata metadata
    ) {
        if (functionItem.getParameterNames().size() != functionArguments.size()) {
            throw new UnexpectedTypeException(
                    "Dynamic function "
                        + functionItem.getIdentifier().getName()
                        + " invoked with incorrect number of arguments. Expected: "
                        + functionItem.getParameterNames().size()
                        + ", Found: "
                        + functionArguments.size(),
                    metadata
            );
        }
    }

    public static void wrapAccordingToSignature(
            Item functionItem,
            List<ItemRuntimePlan> functionArguments,
            RuntimeStaticContext callerStaticContext
    ) {
        if (functionItem.getSignature().getParameterTypes() == null) {
            return;
        }
        for (int i = 0; i < functionArguments.size(); i++) {
            if (
                functionArguments.get(i) != null
                    && !functionItem.getSignature()
                        .getParameterTypes()
                        .get(i)
                        .equals(SequenceType.createSequenceType("item*"))
            ) {
                SequenceType sequenceType = functionItem.getSignature().getParameterTypes().get(i);
                if (functionArguments.get(i).getRuntimeStaticContext().getStaticType().isSubtypeOf(sequenceType)) {
                    continue;
                }
                ExecutionMode executionMode = functionArguments.get(i).getRuntimeStaticContext().getExecutionMode();
                if (isAtMostOne(sequenceType)) {
                    executionMode = ExecutionMode.LOCAL;
                }
                RuntimeStaticContext runtimeStaticContext = callerStaticContext
                    .toBuilder()
                    .staticType(sequenceType)
                    .executionMode(executionMode)
                    .metadata(functionArguments.get(i).getRuntimeStaticContext().getMetadata())
                    .build();
                String exceptionMessage = "Invalid argument for "
                    + functionItem.getIdentifier().getName()
                    + " function. ";
                if (isAtMostOne(sequenceType)) {
                    functionArguments.set(
                        i,
                        wrapAtMostOneForFunctionConversion(
                            functionArguments.get(i),
                            sequenceType,
                            exceptionMessage,
                            runtimeStaticContext
                        )
                    );
                } else {
                    ItemRuntimePlan argumentIterator = wrapForFunctionConversion(
                        functionArguments.get(i),
                        sequenceType,
                        exceptionMessage,
                        runtimeStaticContext
                    );
                    ItemRuntimePlan typePromotionIterator =
                        new TypePromotionIterator(
                                argumentIterator,
                                sequenceType,
                                exceptionMessage,
                                runtimeStaticContext
                        );
                    functionArguments.set(i, typePromotionIterator);
                }
            }
        }
    }

    public static ItemRuntimePlan wrapForFunctionConversion(
            ItemRuntimePlan argumentIterator,
            SequenceType sequenceType,
            String exceptionMessage,
            RuntimeStaticContext runtimeStaticContext
    ) {
        ItemType targetItemType = sequenceType.getItemType();
        if (
            targetItemType.isAtomicItemType()
                && !argumentIterator.getRuntimeStaticContext().getStaticType().getItemType().isAtomicItemType()
        ) {
            argumentIterator = new DataFunctionIterator(
                    Collections.singletonList(argumentIterator),
                    runtimeStaticContext
            );
        }
        if (targetItemType.isAtomicItemType()) {
            argumentIterator = new FunctionUntypedAtomicCastIterator(
                    argumentIterator,
                    targetItemType,
                    exceptionMessage,
                    runtimeStaticContext
            );
        }
        return argumentIterator;
    }

    public static ItemRuntimePlan wrapAtMostOneForFunctionConversion(
            ItemRuntimePlan argumentIterator,
            SequenceType sequenceType,
            String exceptionMessage,
            RuntimeStaticContext runtimeStaticContext
    ) {
        ItemType targetItemType = sequenceType.getItemType();
        if (
            targetItemType.isAtomicItemType()
                && !argumentIterator.getRuntimeStaticContext().getStaticType().getItemType().isAtomicItemType()
        ) {
            argumentIterator = new DataFunctionIterator(
                    Collections.singletonList(argumentIterator),
                    runtimeStaticContext
            );
        }
        return new AtMostOneItemTypePromotionIterator(
                argumentIterator,
                sequenceType,
                exceptionMessage,
                runtimeStaticContext,
                targetItemType.isAtomicItemType() ? targetItemType : null
        );
    }

    public static boolean isAtMostOne(SequenceType sequenceType) {
        return sequenceType.isEmptySequence()
            || sequenceType.getArity().equals(Arity.One)
            || sequenceType.getArity().equals(Arity.OneOrZero);
    }
}
