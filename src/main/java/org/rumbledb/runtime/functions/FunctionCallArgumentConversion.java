/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.functions;

import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.rumbledb.api.Item;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.functions.sequences.general.DataFunctionIterator;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.typing.AtMostOneItemTypePromotionIterator;
import org.rumbledb.runtime.typing.TypePromotionIterator;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

/**
 * Shared argument arity checks and type-promotion wrapping for dynamic calls on
 * {@link org.rumbledb.items.FunctionItem}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FunctionCallArgumentConversion {

    public static void validateArity(
            Item functionItem, List<ItemRuntimePlan> functionArguments, ExceptionMetadata metadata) {
        if (functionItem.getParameterNames().size() != functionArguments.size()) {
            throw new UnexpectedTypeException(
                    "Dynamic function "
                            + functionItem.getIdentifier().getName()
                            + " invoked with incorrect number of arguments. Expected: "
                            + functionItem.getParameterNames().size()
                            + ", Found: "
                            + functionArguments.size(),
                    metadata);
        }
    }

    public static void wrapAccordingToSignature(
            Item functionItem, List<ItemRuntimePlan> functionArguments, RuntimeStaticContext callerStaticContext) {
        if (functionItem.getSignature().getParameterTypes() == null) {
            return;
        }
        String exceptionMessage =
                "Invalid argument for " + functionItem.getIdentifier().getName() + " function. ";
        for (int i = 0; i < functionArguments.size(); i++) {
            if (functionArguments.get(i) != null) {
                functionArguments.set(
                        i,
                        wrapArgument(
                                functionArguments.get(i),
                                functionItem.getSignature().getParameterTypes().get(i),
                                exceptionMessage,
                                callerStaticContext));
            }
        }
    }

    /**
     * Applies the function conversion rules for a parameter of the given type: atomization, casting of untyped
     * values, type promotion, and the final type check. Returns the argument unchanged when it already matches.
     */
    public static ItemRuntimePlan wrapArgument(
            ItemRuntimePlan argument,
            SequenceType parameterType,
            String exceptionMessage,
            RuntimeStaticContext callerStaticContext) {
        if (parameterType.equals(SequenceType.createSequenceType("item*"))
                || argument.getRuntimeStaticContext().getStaticType().isSubtypeOf(parameterType)) {
            return argument;
        }
        ExecutionMode executionMode = isAtMostOne(parameterType)
                ? ExecutionMode.LOCAL
                : argument.getRuntimeStaticContext().getExecutionMode();
        RuntimeStaticContext runtimeStaticContext = callerStaticContext.toBuilder()
                .staticType(parameterType)
                .executionMode(executionMode)
                .metadata(argument.getRuntimeStaticContext().getMetadata())
                .build();
        if (isAtMostOne(parameterType)) {
            return wrapAtMostOneForFunctionConversion(argument, parameterType, exceptionMessage, runtimeStaticContext);
        }
        return new TypePromotionIterator(
                wrapForFunctionConversion(argument, parameterType, exceptionMessage, runtimeStaticContext),
                parameterType,
                exceptionMessage,
                runtimeStaticContext);
    }

    public static ItemRuntimePlan wrapForFunctionConversion(
            ItemRuntimePlan argumentIterator,
            SequenceType sequenceType,
            String exceptionMessage,
            RuntimeStaticContext runtimeStaticContext) {
        ItemType targetItemType = sequenceType.getItemType();
        if (isGeneralizedAtomicType(targetItemType)
                && !argumentIterator
                        .getRuntimeStaticContext()
                        .getStaticType()
                        .getItemType()
                        .isAtomicItemType()) {
            argumentIterator =
                    new DataFunctionIterator(Collections.singletonList(argumentIterator), runtimeStaticContext);
        }
        if (isGeneralizedAtomicType(targetItemType)) {
            argumentIterator = new FunctionUntypedAtomicCastIterator(
                    argumentIterator, targetItemType, exceptionMessage, runtimeStaticContext);
        }
        return argumentIterator;
    }

    public static ItemRuntimePlan wrapAtMostOneForFunctionConversion(
            ItemRuntimePlan argumentIterator,
            SequenceType sequenceType,
            String exceptionMessage,
            RuntimeStaticContext runtimeStaticContext) {
        ItemType targetItemType = sequenceType.getItemType();
        if (isGeneralizedAtomicType(targetItemType)
                && !argumentIterator
                        .getRuntimeStaticContext()
                        .getStaticType()
                        .getItemType()
                        .isAtomicItemType()) {
            argumentIterator =
                    new DataFunctionIterator(Collections.singletonList(argumentIterator), runtimeStaticContext);
        }
        return new AtMostOneItemTypePromotionIterator(
                argumentIterator,
                sequenceType,
                exceptionMessage,
                runtimeStaticContext,
                isGeneralizedAtomicType(targetItemType) ? targetItemType : null);
    }

    /**
     * Checks if the type is a generalized atomic type, which includes atomic types and union types of atomic types.
     * See https://www.w3.org/TR/xquery-31/#dt-generalized-atomic-type
     */
    private static boolean isGeneralizedAtomicType(ItemType type) {
        return type.isAtomicItemType()
                || (type.isUnionType()
                        && type.getTypes().stream().allMatch(FunctionCallArgumentConversion::isGeneralizedAtomicType));
    }

    public static boolean isAtMostOne(SequenceType sequenceType) {
        return sequenceType.isEmptySequence()
                || sequenceType.getArity().equals(Arity.One)
                || sequenceType.getArity().equals(Arity.OneOrZero);
    }
}
