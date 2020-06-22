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

package org.rumbledb.runtime.functions.base;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.DuplicateFunctionIdentifierException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnknownFunctionCallException;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.FunctionItemCallIterator;
import org.rumbledb.runtime.operational.TypePromotionIterator;
import org.rumbledb.types.SequenceType;

import sparksoniq.jsoniq.ExecutionMode;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Functions {

    // two maps for User defined function are needed as execution mode is known at static analysis phase
    // but functions items are fully known at runtimeIterator generation
    private static HashMap<FunctionIdentifier, ExecutionMode> userDefinedFunctionsExecutionMode;
    private static HashMap<FunctionIdentifier, List<ExecutionMode>> userDefinedFunctionsParametersStorageMode;
    private static HashMap<FunctionIdentifier, FunctionItem> userDefinedFunctions;

    private static List<FunctionIdentifier> userDefinedFunctionIdentifiersWithUnsetExecutionModes;



    static {
        userDefinedFunctionsExecutionMode = new HashMap<>();
        userDefinedFunctionsParametersStorageMode = new HashMap<>();
        userDefinedFunctions = new HashMap<>();
        userDefinedFunctionIdentifiersWithUnsetExecutionModes = new ArrayList<>();
    }

    public static void clearUserDefinedFunctions() {
        userDefinedFunctionsExecutionMode.clear();
        userDefinedFunctionsParametersStorageMode.clear();
        userDefinedFunctions.clear();
    }

    public static boolean checkUserDefinedFunctionExecutionModeExists(FunctionIdentifier identifier) {
        return userDefinedFunctionsExecutionMode.containsKey(identifier);
    }

    public static ExecutionMode getUserDefinedFunctionExecutionMode(
            FunctionIdentifier identifier,
            ExceptionMetadata metadata
    ) {
        if (checkUserDefinedFunctionExecutionModeExists(identifier)) {
            return userDefinedFunctionsExecutionMode.get(identifier);
        }
        throw new UnknownFunctionCallException(
                identifier.getName(),
                identifier.getArity(),
                metadata
        );

    }

    public static void addUserDefinedFunctionExecutionMode(
            FunctionIdentifier functionIdentifier,
            ExecutionMode executionMode,
            boolean suppressErrorsForFunctionSignatureCollision,
            ExceptionMetadata meta
    ) {
        if (
            BuiltinFunctionCatalogue.exists(functionIdentifier)
                ||
                (!suppressErrorsForFunctionSignatureCollision
                    && userDefinedFunctionsExecutionMode.containsKey(functionIdentifier))
        ) {
            throw new DuplicateFunctionIdentifierException(functionIdentifier, meta);
        }

        if (isAddingNewUnsetUserDefinedFunction(functionIdentifier, executionMode)) {
            userDefinedFunctionIdentifiersWithUnsetExecutionModes.add(functionIdentifier);
        } else if (isUpdatingUnsetUserDefinedFunctionToNonUnset(functionIdentifier, executionMode)) {
            userDefinedFunctionIdentifiersWithUnsetExecutionModes.remove(functionIdentifier);
        }
        userDefinedFunctionsExecutionMode.put(functionIdentifier, executionMode);
    }

    public static List<ExecutionMode> getUserDefinedFunctionParametersStorageMode(
            FunctionIdentifier identifier,
            ExceptionMetadata metadata
    ) {
        if (checkUserDefinedFunctionExecutionModeExists(identifier)) {
            return userDefinedFunctionsParametersStorageMode.get(identifier);
        }
        List<ExecutionMode> newModes = new ArrayList<>();
        for (int i = 0; i < identifier.getArity(); ++i) {
            newModes.add(ExecutionMode.UNSET);
        }
        userDefinedFunctionsParametersStorageMode.put(identifier, newModes);
        return newModes;
    }

    public static void addUserDefinedFunctionParametersStorageMode(
            FunctionIdentifier functionIdentifier,
            List<ExecutionMode> executionModes,
            boolean suppressErrorsForFunctionSignatureCollision,
            ExceptionMetadata meta
    ) {
        List<ExecutionMode> newModes = new ArrayList<>();
        if (!userDefinedFunctionsParametersStorageMode.containsKey(functionIdentifier)) {
            userDefinedFunctionsParametersStorageMode.put(functionIdentifier, executionModes);
        }
        Iterator<ExecutionMode> oldModes = userDefinedFunctionsParametersStorageMode.get(functionIdentifier).iterator();
        Iterator<ExecutionMode> updatedModes = executionModes.iterator();
        while (oldModes.hasNext() && updatedModes.hasNext()) {
            ExecutionMode oldMode = oldModes.next();
            ExecutionMode updatedMode = updatedModes.next();
            if (oldMode == ExecutionMode.UNSET) {
                newModes.add(updatedMode);
                continue;
            }
            if (updatedMode == ExecutionMode.UNSET) {
                throw new OurBadException("Trying to unset an execution mode that was already set.");
            }
            if (oldMode == updatedMode) {
                newModes.add(oldMode);
                continue;
            }
            throw new OurBadException(
                    "Conflicting execution modes in user-defined function parameters. This happens when the same function is used in a setting with big sequences and another with small sequences, which is an unsupported feature. If you need this, please let us know so we can prioritize."
            );
        }
        if (oldModes.hasNext() || updatedModes.hasNext()) {
            throw new OurBadException("Inconsistent parameter execution modes.");
        }
        userDefinedFunctionsParametersStorageMode.put(functionIdentifier, newModes);
    }

    private static boolean isAddingNewUnsetUserDefinedFunction(
            FunctionIdentifier functionIdentifier,
            ExecutionMode executionMode
    ) {
        return !userDefinedFunctionsExecutionMode.containsKey(functionIdentifier)
            && executionMode == ExecutionMode.UNSET;
    }

    private static boolean isUpdatingUnsetUserDefinedFunctionToNonUnset(
            FunctionIdentifier functionIdentifier,
            ExecutionMode executionMode
    ) {
        return userDefinedFunctionsExecutionMode.containsKey(functionIdentifier)
            && userDefinedFunctionsExecutionMode.get(functionIdentifier) == ExecutionMode.UNSET
            && executionMode != ExecutionMode.UNSET;
    }

    public static List<FunctionIdentifier> getUserDefinedFunctionIdentifiersWithUnsetExecutionModes() {
        return userDefinedFunctionIdentifiersWithUnsetExecutionModes;
    }

    public static RuntimeIterator getUserDefinedFunctionCallIterator(
            FunctionIdentifier identifier,
            ExecutionMode executionMode,
            ExceptionMetadata metadata,
            List<RuntimeIterator> arguments
    ) {
        if (Functions.checkUserDefinedFunctionExists(identifier)) {
            return buildUserDefinedFunctionCallIterator(
                getUserDefinedFunction(identifier),
                executionMode,
                metadata,
                arguments
            );
        }
        throw new UnknownFunctionCallException(
                identifier.getName(),
                identifier.getArity(),
                metadata
        );

    }

    public static RuntimeIterator buildUserDefinedFunctionCallIterator(
            FunctionItem functionItem,
            ExecutionMode executionMode,
            ExceptionMetadata metadata,
            List<RuntimeIterator> arguments
    ) {
        FunctionItemCallIterator functionCallIterator = new FunctionItemCallIterator(
                functionItem,
                arguments,
                executionMode,
                metadata
        );
        if (!functionItem.getSignature().getReturnType().equals(SequenceType.MOST_GENERAL_SEQUENCE_TYPE)) {
            return new TypePromotionIterator(
                    functionCallIterator,
                    functionItem.getSignature().getReturnType(),
                    "Invalid return type for "
                        + ((functionItem.getIdentifier().getName() == null)
                            ? ""
                            : (functionItem.getIdentifier().getName()) + " ")
                        + "function. ",
                    executionMode,
                    metadata
            );
        }
        return functionCallIterator;
    }

    public static void addUserDefinedFunction(Item function, ExceptionMetadata meta) {
        if (!function.isFunction()) {
            throw new OurBadException("Only a function item can be added as a user-defined function.");
        }
        FunctionIdentifier functionIdentifier = function.getIdentifier();
        if (
            BuiltinFunctionCatalogue.exists(functionIdentifier)
                || userDefinedFunctions.containsKey(functionIdentifier)
        ) {
            throw new DuplicateFunctionIdentifierException(functionIdentifier, meta);
        }
        userDefinedFunctions.put(functionIdentifier, (FunctionItem) function);
    }

    public static boolean checkUserDefinedFunctionExists(FunctionIdentifier identifier) {
        return userDefinedFunctions.containsKey(identifier);
    }

    public static FunctionItem getUserDefinedFunction(FunctionIdentifier identifier) {
        FunctionItem functionItem = userDefinedFunctions.get(identifier);
        return functionItem.deepCopy();
    }

    public static RuntimeIterator getBuiltInFunctionIterator(
            FunctionIdentifier identifier,
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata metadata
    ) {
        BuiltinFunction builtinFunction = BuiltinFunctionCatalogue.getBuiltinFunction(identifier);

        for (int i = 0; i < arguments.size(); i++) {
            if (
                !builtinFunction.getSignature()
                    .getParameterTypes()
                    .get(i)
                    .equals(SequenceType.MOST_GENERAL_SEQUENCE_TYPE)
            ) {
                TypePromotionIterator typePromotionIterator = new TypePromotionIterator(
                        arguments.get(i),
                        builtinFunction.getSignature().getParameterTypes().get(i),
                        "Invalid argument for function " + identifier.getName() + ". ",
                        arguments.get(i).getHighestExecutionMode(),
                        arguments.get(i).getMetadata()
                );

                arguments.set(i, typePromotionIterator);
            }
        }

        RuntimeIterator functionCallIterator;
        try {
            Constructor<? extends RuntimeIterator> constructor = builtinFunction.getFunctionIteratorClass()
                .getConstructor(
                    List.class,
                    ExecutionMode.class,
                    ExceptionMetadata.class
                );
            functionCallIterator = constructor.newInstance(arguments, executionMode, metadata);
        } catch (ReflectiveOperationException ex) {
            throw new UnknownFunctionCallException(
                    identifier.getName(),
                    arguments.size(),
                    metadata
            );
        }

        if (!builtinFunction.getSignature().getReturnType().equals(SequenceType.MOST_GENERAL_SEQUENCE_TYPE)) {
            return new TypePromotionIterator(
                    functionCallIterator,
                    builtinFunction.getSignature().getReturnType(),
                    "Invalid return type for function " + identifier.getName() + ". ",
                    functionCallIterator.getHighestExecutionMode(),
                    functionCallIterator.getMetadata()
            );
        }
        return functionCallIterator;
    }



    static final class BuiltinFunctions {

    }
}
