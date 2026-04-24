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
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.exceptions.UnknownFunctionCallException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.BuiltinFunctionItemCallIterator;
import org.rumbledb.runtime.functions.FunctionItemCallIterator;
import org.rumbledb.runtime.functions.sequences.general.DataFunctionIterator;
import org.rumbledb.runtime.typing.AtMostOneItemTypePromotionIterator;
import org.rumbledb.runtime.typing.TypePromotionIterator;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class NamedFunctions implements Serializable, KryoSerializable {

    private static final long serialVersionUID = 1L;

    // two maps for User defined function are needed as execution mode is known at
    // static analysis phase
    // but functions items are fully known at runtimeIterator generation
    private HashMap<FunctionIdentifier, FunctionItem> userDefinedFunctions;

    public NamedFunctions() {
        this.userDefinedFunctions = new HashMap<>();
    }

    public void clearUserDefinedFunctions() {
        this.userDefinedFunctions.clear();
    }

    /**
     * Callee execution mode is taken from {@code callerRuntimeContext.getExecutionMode()} (same as
     * {@link org.rumbledb.runtime.RuntimeIterator#getHighestExecutionMode()} for iterators constructed with that
     * context).
     */
    public RuntimeIterator getUserDefinedFunctionCallIterator(
            FunctionIdentifier identifier,
            RuntimeStaticContext callerRuntimeContext,
            List<RuntimeIterator> arguments,
            boolean isTailOptimization
    ) {
        if (checkUserDefinedFunctionExists(identifier)) {
            return buildFunctionItemCallIterator(
                getUserDefinedFunction(identifier),
                callerRuntimeContext,
                callerRuntimeContext.getExecutionMode(),
                arguments,
                isTailOptimization
            );
        }
        throw new UnknownFunctionCallException(
                identifier.getName(),
                identifier.getArity(),
                callerRuntimeContext.getMetadata()
        );

    }

    /**
     * Builds a dynamic function-item call using configuration and metadata from {@code callerRuntimeContext} and the
     * callee's {@code executionModeForFunctionCall}
     */
    public static RuntimeIterator buildFunctionItemCallIterator(
            Item functionItem,
            RuntimeStaticContext callerRuntimeContext,
            ExecutionMode executionModeForFunctionCall,
            List<RuntimeIterator> arguments,
            boolean isTailOptimization
    ) {
        ExceptionMetadata metadata = callerRuntimeContext.getMetadata();
        SequenceType sequenceType = functionItem.getSignature().getReturnType();
        SequenceType innerSequenceType = functionItem.getBodyIterator().getStaticType();
        RuntimeStaticContext outerStaticContext = callerRuntimeContext.withStaticType(
            sequenceType
        )
            .withExecutionMode(
                executionModeForFunctionCall
            );
        RuntimeStaticContext innerStaticContext = callerRuntimeContext.withStaticType(
            innerSequenceType
        ).withExecutionMode(executionModeForFunctionCall);
        RuntimeIterator functionCallIterator;
        if (functionItem.isBuiltinFunction()) {
            if (arguments.stream().anyMatch(a -> a == null)) {
                throw new UnsupportedFeatureException(
                        "Partial application of builtin named function references is not supported yet.",
                        metadata
                );
            }
            functionCallIterator = new BuiltinFunctionItemCallIterator(
                    functionItem,
                    arguments,
                    innerStaticContext
            );
        } else {
            functionCallIterator = new FunctionItemCallIterator(
                    functionItem,
                    arguments,
                    innerStaticContext,
                    isTailOptimization
            );
        }
        if (sequenceType.equals(SequenceType.createSequenceType("item*"))) {
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
                    outerStaticContext
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
                    outerStaticContext
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
            RuntimeStaticContext callerStaticContext,
            boolean argumentsAlreadyCoerced
    ) {
        RumbleRuntimeConfiguration conf = callerStaticContext.getConfiguration();
        ExceptionMetadata metadata = callerStaticContext.getMetadata();
        boolean checkReturnTypesOfBuiltinFunctions = conf.isCheckReturnTypeOfBuiltinFunctions();
        BuiltinFunction builtinFunction = BuiltinFunctionCatalogue.getBuiltinFunction(identifier);
        if (builtinFunction == null) {
            throw new UnknownFunctionCallException(identifier.getName(), identifier.getArity(), metadata);
        }
        if (!argumentsAlreadyCoerced) {
            for (int i = 0; i < arguments.size(); i++) {
                if (
                    !builtinFunction.getSignature()
                        .getParameterTypes()
                        .get(i)
                        .equals(SequenceType.createSequenceType("item*"))
                ) {
                    SequenceType sequenceType = builtinFunction.getSignature().getParameterTypes().get(i);
                    RuntimeStaticContext argStaticContext =
                        callerStaticContext.withStaticType(sequenceType)
                            .withExecutionMode(arguments.get(i).getHighestExecutionMode())
                            .withMetadata(arguments.get(i).getMetadata());
                    RuntimeIterator argumentIterator = arguments.get(i);
                    if (
                        sequenceType.getItemType().isAtomicItemType()
                            && !argumentIterator.getStaticType().getItemType().isAtomicItemType()
                    ) {
                        argumentIterator = new DataFunctionIterator(
                                Collections.singletonList(argumentIterator),
                                argStaticContext
                        );
                    }
                    if (
                        sequenceType.isEmptySequence()
                            || sequenceType.getArity().equals(Arity.One)
                            || sequenceType.getArity().equals(Arity.OneOrZero)
                    ) {
                        arguments.set(
                            i,
                            new AtMostOneItemTypePromotionIterator(
                                    argumentIterator,
                                    sequenceType,
                                    "Invalid argument for function " + identifier.getName() + ". ",
                                    argStaticContext
                            )
                        );
                    } else {
                        arguments.set(
                            i,
                            new TypePromotionIterator(
                                    argumentIterator,
                                    sequenceType,
                                    "Invalid argument for function " + identifier.getName() + ". ",
                                    argStaticContext
                            )
                        );
                    }
                }
            }
        }

        SequenceType catalogueReturnType = builtinFunction.getSignature().getReturnType();

        RuntimeStaticContext delegateContext =
            callerStaticContext.withStaticType(catalogueReturnType)
                .withExecutionMode(callerStaticContext.getExecutionMode());

        if (!"format-number".equals(identifier.getName().getLocalName())) {
            delegateContext.dropDecimalFormats();
        }

        RuntimeIterator functionCallIterator;
        try {
            Constructor<? extends RuntimeIterator> constructor = builtinFunction.getFunctionIteratorClass()
                .getConstructor(List.class, RuntimeStaticContext.class);
            functionCallIterator = constructor.newInstance(arguments, delegateContext);
        } catch (ReflectiveOperationException ex) {
            RuntimeException e = new UnknownFunctionCallException(
                    identifier.getName(),
                    arguments.size(),
                    delegateContext.getMetadata()
            );
            e.initCause(ex);
            throw e;
        }

        if (catalogueReturnType.equals(SequenceType.createSequenceType("item*"))) {
            return functionCallIterator;
        }
        if (!checkReturnTypesOfBuiltinFunctions) {
            return functionCallIterator;
        }
        RuntimeStaticContext returnCheckContext = callerStaticContext.withStaticType(catalogueReturnType)
            .withExecutionMode(functionCallIterator.getHighestExecutionMode())
            .withMetadata(functionCallIterator.getMetadata());
        if (
            catalogueReturnType.isEmptySequence()
                || catalogueReturnType.getArity().equals(Arity.One)
                || catalogueReturnType.getArity().equals(Arity.OneOrZero)
        ) {
            return new AtMostOneItemTypePromotionIterator(
                    functionCallIterator,
                    catalogueReturnType,
                    "Invalid return type for function " + builtinFunction.getIdentifier().getName() + ". ",
                    returnCheckContext
            );
        }
        return new TypePromotionIterator(
                functionCallIterator,
                catalogueReturnType,
                "Invalid return type for function " + builtinFunction.getIdentifier().getName() + ". ",
                returnCheckContext
        );
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
