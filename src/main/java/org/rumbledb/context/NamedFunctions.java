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
package org.rumbledb.context;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.DuplicateFunctionIdentifierException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnknownFunctionCallException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.runtime.functions.ConstructorFunctionIterator;
import org.rumbledb.runtime.functions.FunctionCallArgumentConversion;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.typing.AtMostOneItemTypePromotionIterator;
import org.rumbledb.runtime.typing.TypePromotionIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

public class NamedFunctions implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // two maps for User defined function are needed as execution mode is known at
    // static analysis phase
    // but functions items are fully known at runtimeIterator generation
    private final HashMap<FunctionIdentifier, FunctionItem> userDefinedFunctions;

    public NamedFunctions() {
        this.userDefinedFunctions = new HashMap<>();
    }

    public void addUserDefinedFunction(Item function, ExceptionMetadata meta) {
        if (!function.isFunction()) {
            throw new OurBadException("Only a function item can be added as a user-defined function.");
        }
        FunctionIdentifier functionIdentifier = function.getIdentifier();
        String queryLanguage = function.getModuleDynamicContext()
                .getRumbleConfiguration()
                .semantics()
                .queryLanguage();
        if (BuiltinFunctionCatalogue.exists(functionIdentifier, queryLanguage)
                || this.userDefinedFunctions.containsKey(functionIdentifier)) {
            throw new DuplicateFunctionIdentifierException(functionIdentifier, meta);
        }
        this.userDefinedFunctions.put(functionIdentifier, (FunctionItem) function);
    }

    public boolean checkUserDefinedFunctionExists(FunctionIdentifier identifier) {
        return this.userDefinedFunctions.containsKey(identifier);
    }

    /**
     * Returns the registered function item itself, shared by all calls, which must not change it. Unlike
     * {@link #getUserDefinedFunction}, which copies it for function references, it is used for calls, which only read
     * the function item.
     */
    public FunctionItem getSharedUserDefinedFunction(FunctionIdentifier identifier, ExceptionMetadata metadata) {
        FunctionItem functionItem = this.userDefinedFunctions.get(identifier);
        if (functionItem == null) {
            throw new UnknownFunctionCallException(identifier.getName(), identifier.getArity(), metadata);
        }
        return functionItem;
    }

    public FunctionItem getUserDefinedFunction(FunctionIdentifier identifier) {
        FunctionItem functionItem = this.userDefinedFunctions.get(identifier);
        return functionItem.copyForLookup();
    }

    public static ItemRuntimePlan getBuiltInFunctionIterator(
            FunctionIdentifier identifier,
            List<? extends ItemRuntimePlan> inputArguments,
            RuntimeStaticContext callerStaticContext,
            boolean argumentsAlreadyCoerced) {
        List<ItemRuntimePlan> arguments = new ArrayList<>(inputArguments);
        RumbleConfiguration conf = callerStaticContext.getConfiguration();
        ExceptionMetadata metadata = callerStaticContext.getMetadata();
        boolean checkReturnTypesOfBuiltinFunctions = conf.analysis().checkReturnTypeOfBuiltinFunctions();
        var resolvedConstructor = ConstructorFunctionResolver.resolve(identifier, callerStaticContext);
        BuiltinFunction builtinFunction = resolvedConstructor == null
                ? BuiltinFunctionCatalogue.getBuiltinFunction(identifier, callerStaticContext.getQueryLanguage())
                : resolvedConstructor.asBuiltinFunction();
        if (builtinFunction == null) {
            throw new UnknownFunctionCallException(identifier.getName(), identifier.getArity(), metadata);
        }
        if (!argumentsAlreadyCoerced) {
            for (int i = 0; i < arguments.size(); i++) {
                if (!builtinFunction
                        .getSignature()
                        .getParameterTypes()
                        .get(i)
                        .equals(SequenceType.createSequenceType("item*"))) {
                    SequenceType sequenceType =
                            builtinFunction.getSignature().getParameterTypes().get(i);
                    if (conversionIsIdentity(arguments.get(i).getStaticType(), sequenceType)) {
                        continue;
                    }

                    ExecutionMode executionMode =
                            arguments.get(i).getRuntimeStaticContext().getExecutionMode();
                    if (FunctionCallArgumentConversion.isAtMostOne(sequenceType)) {
                        executionMode = ExecutionMode.LOCAL;
                    }

                    RuntimeStaticContext argStaticContext = callerStaticContext.toBuilder()
                            .staticType(sequenceType)
                            .executionMode(executionMode)
                            .metadata(arguments.get(i).getRuntimeStaticContext().getMetadata())
                            .build();
                    String exceptionMessage = "Invalid argument for function " + identifier.getName() + ". ";
                    if (FunctionCallArgumentConversion.isAtMostOne(sequenceType)) {
                        arguments.set(
                                i,
                                FunctionCallArgumentConversion.wrapAtMostOneForFunctionConversion(
                                        arguments.get(i), sequenceType, exceptionMessage, argStaticContext));
                    } else {
                        ItemRuntimePlan argumentIterator = FunctionCallArgumentConversion.wrapForFunctionConversion(
                                arguments.get(i), sequenceType, exceptionMessage, argStaticContext);
                        arguments.set(
                                i,
                                new TypePromotionIterator(
                                        argumentIterator, sequenceType, exceptionMessage, argStaticContext));
                    }
                }
            }
        }

        SequenceType catalogueReturnType = builtinFunction.getSignature().getReturnType();

        RuntimeStaticContext delegateContext = callerStaticContext.toBuilder()
                .staticType(catalogueReturnType)
                .executionMode(callerStaticContext.getExecutionMode())
                .build();

        if (!"format-number".equals(identifier.getName().getLocalName())
                && !"function-lookup".equals(identifier.getName().getLocalName())) {
            // function-lookup must retain decimal formats for references to format-number.
            // Other builtins do not need these definitions.
            delegateContext = delegateContext.toBuilder()
                    .decimalFormats(null)
                    .defaultDecimalFormat(null)
                    .build();
        }

        ItemRuntimePlan functionCallIterator;
        try {
            if (resolvedConstructor != null) {
                functionCallIterator = new ConstructorFunctionIterator(resolvedConstructor, arguments, delegateContext);
            } else {
                Constructor<? extends ItemRuntimePlan> constructor = builtinFunction
                        .getFunctionIteratorClass()
                        .getConstructor(List.class, RuntimeStaticContext.class);
                functionCallIterator = constructor.newInstance(arguments, delegateContext);
            }
        } catch (ReflectiveOperationException ex) {
            RuntimeException e = new UnknownFunctionCallException(
                    identifier.getName(), arguments.size(), delegateContext.getMetadata());
            e.initCause(ex);
            throw e;
        }

        if (catalogueReturnType.equals(SequenceType.createSequenceType("item*"))) {
            return functionCallIterator;
        }
        if (!checkReturnTypesOfBuiltinFunctions) {
            return functionCallIterator;
        }
        RuntimeStaticContext returnCheckContext = callerStaticContext.toBuilder()
                .staticType(catalogueReturnType)
                .executionMode(functionCallIterator.getRuntimeStaticContext().getExecutionMode())
                .metadata(functionCallIterator.getRuntimeStaticContext().getMetadata())
                .build();
        if (catalogueReturnType.isEmptySequence()
                || catalogueReturnType.getArity().equals(Arity.One)
                || catalogueReturnType.getArity().equals(Arity.OneOrZero)) {
            return new AtMostOneItemTypePromotionIterator(
                    functionCallIterator,
                    catalogueReturnType,
                    "Invalid return type for function "
                            + builtinFunction.getIdentifier().getName()
                            + ". ",
                    returnCheckContext);
        }
        return new TypePromotionIterator(
                functionCallIterator,
                catalogueReturnType,
                "Invalid return type for function "
                        + builtinFunction.getIdentifier().getName()
                        + ". ",
                returnCheckContext);
    }

    private static boolean conversionIsIdentity(SequenceType argumentType, SequenceType expectedType) {
        if (argumentType == null || !argumentType.isResolved() || !expectedType.isResolved()) {
            return false;
        }
        if (argumentType.isEmptySequence()) {
            return false;
        }
        ItemType argumentItemType = argumentType.getItemType();
        if (!argumentItemType.isAtomicItemType()) {
            return false;
        }
        if (BuiltinTypesCatalogue.untypedAtomicItem.isSubtypeOf(argumentItemType)) {
            return false;
        }
        return argumentType.isSubtypeOf(expectedType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (FunctionIdentifier i : this.userDefinedFunctions.keySet()) {
            sb.append("       " + i.getNameWithArity() + "\n");
        }
        return sb.toString();
    }
}
