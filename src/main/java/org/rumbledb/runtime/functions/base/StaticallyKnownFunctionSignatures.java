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

import org.rumbledb.exceptions.DuplicateFunctionIdentifierException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnknownFunctionCallException;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.FunctionItemCallIterator;
import org.rumbledb.runtime.operational.TypePromotionIterator;
import org.rumbledb.types.SequenceType;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import sparksoniq.jsoniq.ExecutionMode;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class StaticallyKnownFunctionSignatures implements Serializable, KryoSerializable {

    // two maps for User defined function are needed as execution mode is known at static analysis phase
    // but functions items are fully known at runtimeIterator generation
    private HashMap<FunctionIdentifier, ExecutionMode> userDefinedFunctionsExecutionMode;
    private HashMap<FunctionIdentifier, List<ExecutionMode>> userDefinedFunctionsParametersStorageMode;
    private List<FunctionIdentifier> userDefinedFunctionIdentifiersWithUnsetExecutionModes;

    public StaticallyKnownFunctionSignatures() {
        this.userDefinedFunctionsExecutionMode = new HashMap<>();
        this.userDefinedFunctionsParametersStorageMode = new HashMap<>();
        this.userDefinedFunctionIdentifiersWithUnsetExecutionModes = new ArrayList<>();
    }

    public void clearUserDefinedFunctions() {
        this.userDefinedFunctionsExecutionMode.clear();
        this.userDefinedFunctionsParametersStorageMode.clear();
    }

    public boolean checkUserDefinedFunctionExecutionModeExists(FunctionIdentifier identifier) {
        return this.userDefinedFunctionsExecutionMode.containsKey(identifier);
    }

    public ExecutionMode getUserDefinedFunctionExecutionMode(
            FunctionIdentifier identifier,
            ExceptionMetadata metadata
    ) {
        if (checkUserDefinedFunctionExecutionModeExists(identifier)) {
            return this.userDefinedFunctionsExecutionMode.get(identifier);
        }
        throw new UnknownFunctionCallException(
                identifier.getName(),
                identifier.getArity(),
                metadata
        );

    }

    public void addUserDefinedFunctionExecutionMode(
            FunctionIdentifier functionIdentifier,
            ExecutionMode executionMode,
            boolean suppressErrorsForFunctionSignatureCollision,
            ExceptionMetadata meta
    ) {
        if (
            BuiltinFunctionCatalogue.exists(functionIdentifier)
                ||
                (!suppressErrorsForFunctionSignatureCollision
                    && this.userDefinedFunctionsExecutionMode.containsKey(functionIdentifier))
        ) {
            throw new DuplicateFunctionIdentifierException(functionIdentifier, meta);
        }

        if (isAddingNewUnsetUserDefinedFunction(functionIdentifier, executionMode)) {
            this.userDefinedFunctionIdentifiersWithUnsetExecutionModes.add(functionIdentifier);
        } else if (isUpdatingUnsetUserDefinedFunctionToNonUnset(functionIdentifier, executionMode)) {
            this.userDefinedFunctionIdentifiersWithUnsetExecutionModes.remove(functionIdentifier);
        }
        this.userDefinedFunctionsExecutionMode.put(functionIdentifier, executionMode);
    }

    public List<ExecutionMode> getUserDefinedFunctionParametersStorageMode(
            FunctionIdentifier identifier,
            ExceptionMetadata metadata
    ) {
        if (checkUserDefinedFunctionExecutionModeExists(identifier)) {
            return this.userDefinedFunctionsParametersStorageMode.get(identifier);
        }
        List<ExecutionMode> newModes = new ArrayList<>();
        for (int i = 0; i < identifier.getArity(); ++i) {
            newModes.add(ExecutionMode.UNSET);
        }
        this.userDefinedFunctionsParametersStorageMode.put(identifier, newModes);
        return newModes;
    }

    public void addUserDefinedFunctionParametersStorageMode(
            FunctionIdentifier functionIdentifier,
            List<ExecutionMode> executionModes,
            boolean suppressErrorsForFunctionSignatureCollision,
            ExceptionMetadata meta
    ) {
        List<ExecutionMode> newModes = new ArrayList<>();
        if (!this.userDefinedFunctionsParametersStorageMode.containsKey(functionIdentifier)) {
            this.userDefinedFunctionsParametersStorageMode.put(functionIdentifier, executionModes);
        }
        Iterator<ExecutionMode> oldModes = this.userDefinedFunctionsParametersStorageMode.get(functionIdentifier)
            .iterator();
        Iterator<ExecutionMode> updatedModes = executionModes.iterator();
        while (oldModes.hasNext() && updatedModes.hasNext()) {
            ExecutionMode oldMode = oldModes.next();
            ExecutionMode updatedMode = updatedModes.next();
            if (oldMode == ExecutionMode.UNSET) {
                newModes.add(updatedMode);
                continue;
            }
            if (updatedMode == ExecutionMode.UNSET) {
                throw new OurBadException(
                        "Trying to unset an execution mode that was already set to "
                            + oldMode
                            + " in function "
                            + functionIdentifier
                );
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
        this.userDefinedFunctionsParametersStorageMode.put(functionIdentifier, newModes);
    }

    private boolean isAddingNewUnsetUserDefinedFunction(
            FunctionIdentifier functionIdentifier,
            ExecutionMode executionMode
    ) {
        return !this.userDefinedFunctionsExecutionMode.containsKey(functionIdentifier)
            && executionMode == ExecutionMode.UNSET;
    }

    private boolean isUpdatingUnsetUserDefinedFunctionToNonUnset(
            FunctionIdentifier functionIdentifier,
            ExecutionMode executionMode
    ) {
        return this.userDefinedFunctionsExecutionMode.containsKey(functionIdentifier)
            && this.userDefinedFunctionsExecutionMode.get(functionIdentifier) == ExecutionMode.UNSET
            && executionMode != ExecutionMode.UNSET;
    }

    public List<FunctionIdentifier> getUserDefinedFunctionIdentifiersWithUnsetExecutionModes() {
        return this.userDefinedFunctionIdentifiersWithUnsetExecutionModes;
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



    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.userDefinedFunctionsExecutionMode);
        kryo.writeObject(output, this.userDefinedFunctionIdentifiersWithUnsetExecutionModes);
        kryo.writeObject(output, this.userDefinedFunctionsParametersStorageMode);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.userDefinedFunctionsExecutionMode = kryo.readObject(input, HashMap.class);
        this.userDefinedFunctionIdentifiersWithUnsetExecutionModes = kryo.readObject(input, List.class);
        this.userDefinedFunctionsParametersStorageMode = kryo.readObject(input, HashMap.class);
    }
}
