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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
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

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;

public class NamedFunctions implements Serializable, KryoSerializable {

    private static final long serialVersionUID = 1L;

    // two maps for User defined function are needed as execution mode is known at
    // static analysis phase
    // but functions items are fully known at runtimeIterator generation
    private HashMap<FunctionIdentifier, FunctionItem> userDefinedFunctions;
    private RumbleRuntimeConfiguration conf;

    public NamedFunctions(RumbleRuntimeConfiguration conf) {
        this.userDefinedFunctions = new HashMap<>();
        this.conf = conf;
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
                this.conf,
                executionMode,
                metadata,
                arguments
            );
        }
        throw new UnknownFunctionCallException(identifier.getName(), identifier.getArity(), metadata);

    }

    public static RuntimeIterator buildUserDefinedFunctionCallIterator(
            Item functionItem,
            RumbleRuntimeConfiguration conf,
            ExecutionMode executionMode,
            ExceptionMetadata metadata,
            List<RuntimeIterator> arguments
    ) {
        SequenceType sequenceType = functionItem.getSignature().getReturnType();
        SequenceType innerSequenceType = functionItem.getBodyIterator().getStaticType();
        RuntimeStaticContext staticContext = new RuntimeStaticContext(conf, sequenceType, executionMode, metadata);
        RuntimeStaticContext innerStaticContext = new RuntimeStaticContext(
                conf,
                innerSequenceType,
                executionMode,
                metadata
        );
        FunctionItemCallIterator functionCallIterator = new FunctionItemCallIterator(
                functionItem,
                arguments,
                innerStaticContext
        );
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
                    staticContext
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
                    staticContext
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
        FunctionItem copyFunctionItem = functionItem.deepCopy();
        copyFunctionItem.setModuleDynamicContext(functionItem.getModuleDynamicContext());
        return copyFunctionItem;
    }

    public static RuntimeIterator getBuiltInFunctionIterator(
            FunctionIdentifier identifier,
            List<RuntimeIterator> arguments,
            StaticContext staticContext,
            RumbleRuntimeConfiguration conf,
            ExecutionMode executionMode,
            ExceptionMetadata metadata
    ) {
        boolean checkReturnTypesOfBuiltinFunctions = conf.isCheckReturnTypeOfBuiltinFunctions();
        BuiltinFunction builtinFunction = BuiltinFunctionCatalogue.getBuiltinFunction(identifier);
        if (builtinFunction == null) {
            throw new UnknownFunctionCallException(identifier.getName(), identifier.getArity(), metadata);
        }
        for (int i = 0; i < arguments.size(); i++) {
            if (!builtinFunction.getSignature().getParameterTypes().get(i).equals(SequenceType.ITEM_STAR)) {
                SequenceType sequenceType = builtinFunction.getSignature().getParameterTypes().get(i);
                RuntimeStaticContext runtimeStaticContext = new RuntimeStaticContext(
                        conf,
                        sequenceType,
                        arguments.get(i).getHighestExecutionMode(),
                        arguments.get(i).getMetadata()
                );
                if (
                    sequenceType.isEmptySequence()
                        || sequenceType.getArity().equals(Arity.One)
                        || sequenceType.getArity().equals(Arity.OneOrZero)
                ) {
                    RuntimeIterator typePromotionIterator = new AtMostOneItemTypePromotionIterator(
                            arguments.get(i),
                            sequenceType,
                            "Invalid argument for function " + identifier.getName() + ". ",
                            runtimeStaticContext
                    );

                    arguments.set(i, typePromotionIterator);
                } else {
                    TypePromotionIterator typePromotionIterator = new TypePromotionIterator(
                            arguments.get(i),
                            sequenceType,
                            "Invalid argument for function " + identifier.getName() + ". ",
                            runtimeStaticContext
                    );

                    arguments.set(i, typePromotionIterator);
                }
            }
        }

        RuntimeIterator functionCallIterator;
        try {
            Constructor<? extends RuntimeIterator> constructor = builtinFunction.getFunctionIteratorClass()
                .getConstructor(List.class, RuntimeStaticContext.class);
            functionCallIterator = constructor.newInstance(
                arguments,
                new RuntimeStaticContext(
                        conf,
                        builtinFunction.getSignature().getReturnType(),
                        executionMode,
                        metadata
                )
            );
        } catch (ReflectiveOperationException ex) {
            RuntimeException e = new UnknownFunctionCallException(identifier.getName(), arguments.size(), metadata);
            e.initCause(ex);
            throw e;
        }

        if (!builtinFunction.getSignature().getReturnType().equals(SequenceType.ITEM_STAR)) {
            if (!checkReturnTypesOfBuiltinFunctions) {
                return functionCallIterator;
            }
            functionCallIterator.setStaticContext(staticContext);
            SequenceType sequenceType = builtinFunction.getSignature().getReturnType();
            RuntimeStaticContext runtimeStaticContext = new RuntimeStaticContext(
                    conf,
                    sequenceType,
                    functionCallIterator.getHighestExecutionMode(),
                    functionCallIterator.getMetadata()
            );
            if (
                sequenceType.isEmptySequence()
                    || sequenceType.getArity().equals(Arity.One)
                    || sequenceType.getArity().equals(Arity.OneOrZero)
            ) {
                return new AtMostOneItemTypePromotionIterator(
                        functionCallIterator,
                        sequenceType,
                        "Invalid return type for function " + identifier.getName() + ". ",
                        runtimeStaticContext
                );
            } else {
                return new TypePromotionIterator(
                        functionCallIterator,
                        sequenceType,
                        "Invalid return type for function " + identifier.getName() + ". ",
                        runtimeStaticContext
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
