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

package org.rumbledb.context;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.DuplicateFunctionIdentifierException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnknownFunctionCallException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.FunctionItemCallIterator;
import org.rumbledb.runtime.typing.AtMostOneItemTypePromotionIterator;
import org.rumbledb.runtime.typing.TypePromotionIterator;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;

public class NamedFunctions implements Serializable, KryoSerializable {

    private static final long serialVersionUID = 1L;

    // two maps for User defined function are needed as execution mode is known at static analysis phase
    // but functions items are fully known at runtimeIterator generation
    private HashMap<FunctionIdentifier, FunctionItem> userDefinedFunctions;

    public NamedFunctions() {
        this.userDefinedFunctions = new HashMap<>();
    }

    public void clearUserDefinedFunctions() {
        this.userDefinedFunctions.clear();
    }

    public RuntimeIterator getUserDefinedFunctionCallIterator(
            FunctionIdentifier identifier,
            ExecutionMode executionMode,
            ExceptionMetadata metadata,
            List<RuntimeIterator> arguments
    ) {
        if (checkUserDefinedFunctionExists(identifier)) {
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
            Item functionItem,
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
        SequenceType sequenceType = functionItem.getSignature().getReturnType();
        if (sequenceType.equals(SequenceType.ITEM_STAR)) {
            return functionCallIterator;
        }
        if (
            sequenceType.isEmptySequence()
                || sequenceType.getArity().equals(Arity.One)
                || sequenceType.getArity().equals(Arity.OneOrZero)
        ) {
            return new AtMostOneItemTypePromotionIterator(
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
        } else {
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

    }

    public void addUserDefinedFunction(Item function, ExceptionMetadata meta) {
        if (!function.isFunction()) {
            throw new OurBadException("Only a function item can be added as a user-defined function.");
        }
        FunctionIdentifier functionIdentifier = function.getIdentifier();
        if (
            BuiltinFunctionCatalogue.exists(functionIdentifier)
                || this.userDefinedFunctions.containsKey(functionIdentifier)
        ) {
            throw new DuplicateFunctionIdentifierException(functionIdentifier, meta);
        }
        this.userDefinedFunctions.put(functionIdentifier, (FunctionItem) function);
    }

    public boolean checkUserDefinedFunctionExists(FunctionIdentifier identifier) {
        return this.userDefinedFunctions.containsKey(identifier);
    }

    public FunctionItem getUserDefinedFunction(FunctionIdentifier identifier) {
        FunctionItem functionItem = this.userDefinedFunctions.get(identifier);
        return functionItem.deepCopy();
    }

    public static RuntimeIterator getBuiltInFunctionIterator(
            FunctionIdentifier identifier,
            List<RuntimeIterator> arguments,
            StaticContext staticContext,
            ExecutionMode executionMode,
            boolean checkReturnTypesOfBuiltinFunctions,
            ExceptionMetadata metadata
    ) {
        BuiltinFunction builtinFunction = BuiltinFunctionCatalogue.getBuiltinFunction(identifier);
        if (builtinFunction == null) {
            throw new UnknownFunctionCallException(identifier.getName(), identifier.getArity(), metadata);
        }
        for (int i = 0; i < arguments.size(); i++) {
            if (
                !builtinFunction.getSignature()
                    .getParameterTypes()
                    .get(i)
                    .equals(SequenceType.ITEM_STAR)
            ) {
                SequenceType sequenceType = builtinFunction.getSignature().getParameterTypes().get(i);
                if (
                    sequenceType.isEmptySequence()
                        || sequenceType.getArity().equals(Arity.One)
                        || sequenceType.getArity().equals(Arity.OneOrZero)
                ) {
                    RuntimeIterator typePromotionIterator = new AtMostOneItemTypePromotionIterator(
                            arguments.get(i),
                            sequenceType,
                            "Invalid argument for function " + identifier.getName() + ". ",
                            arguments.get(i).getHighestExecutionMode(),
                            arguments.get(i).getMetadata()
                    );

                    arguments.set(i, typePromotionIterator);
                } else {
                    TypePromotionIterator typePromotionIterator = new TypePromotionIterator(
                            arguments.get(i),
                            sequenceType,
                            "Invalid argument for function " + identifier.getName() + ". ",
                            arguments.get(i).getHighestExecutionMode(),
                            arguments.get(i).getMetadata()
                    );

                    arguments.set(i, typePromotionIterator);
                }
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

        if (!builtinFunction.getSignature().getReturnType().equals(SequenceType.ITEM_STAR)) {
            if (!checkReturnTypesOfBuiltinFunctions) {
                return functionCallIterator;
            }
            functionCallIterator.setStaticContext(staticContext);
            SequenceType sequenceType = builtinFunction.getSignature().getReturnType();
            if (
                sequenceType.isEmptySequence()
                    || sequenceType.getArity().equals(Arity.One)
                    || sequenceType.getArity().equals(Arity.OneOrZero)
            ) {
                return new AtMostOneItemTypePromotionIterator(
                        functionCallIterator,
                        sequenceType,
                        "Invalid return type for function " + identifier.getName() + ". ",
                        functionCallIterator.getHighestExecutionMode(),
                        functionCallIterator.getMetadata()
                );
            } else {
                return new TypePromotionIterator(
                        functionCallIterator,
                        sequenceType,
                        "Invalid return type for function " + identifier.getName() + ". ",
                        functionCallIterator.getHighestExecutionMode(),
                        functionCallIterator.getMetadata()
                );
            }
        }
        return functionCallIterator;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.userDefinedFunctions);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.userDefinedFunctions = kryo.readObject(input, HashMap.class);
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
