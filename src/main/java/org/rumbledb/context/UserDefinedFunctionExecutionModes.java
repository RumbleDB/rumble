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
import org.rumbledb.exceptions.DuplicateFunctionIdentifierException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnknownFunctionCallException;
import org.rumbledb.expressions.ExecutionMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class UserDefinedFunctionExecutionModes implements Serializable, KryoSerializable {

    private static final long serialVersionUID = 1L;

    // two maps for User defined function are needed as execution mode is known at static analysis phase
    // but functions items are fully known at runtimeIterator generation
    private HashMap<FunctionIdentifier, ExecutionMode> userDefinedFunctionsExecutionMode;
    private HashMap<FunctionIdentifier, List<ExecutionMode>> userDefinedFunctionsParametersStorageMode;
    private List<FunctionIdentifier> userDefinedFunctionIdentifiersWithUnsetExecutionModes;

    public UserDefinedFunctionExecutionModes() {
        this.userDefinedFunctionsExecutionMode = new HashMap<>();
        this.userDefinedFunctionsParametersStorageMode = new HashMap<>();
        this.userDefinedFunctionIdentifiersWithUnsetExecutionModes = new ArrayList<>();
    }

    public boolean exists(FunctionIdentifier identifier) {
        return this.userDefinedFunctionsExecutionMode.containsKey(identifier);
    }

    public ExecutionMode getExecutionMode(
            FunctionIdentifier identifier,
            ExceptionMetadata metadata
    ) {
        if (exists(identifier)) {
            return this.userDefinedFunctionsExecutionMode.get(identifier);
        }
        throw new UnknownFunctionCallException(
                identifier.getName(),
                identifier.getArity(),
                metadata
        );

    }

    public void setExecutionMode(
            FunctionIdentifier functionIdentifier,
            ExecutionMode executionMode,
            boolean suppressErrorsForFunctionSignatureCollision,
            ExceptionMetadata metadata
    ) {
        if (
            BuiltinFunctionCatalogue.exists(functionIdentifier)
                ||
                (!suppressErrorsForFunctionSignatureCollision
                    && this.userDefinedFunctionsExecutionMode.containsKey(functionIdentifier))
        ) {
            throw new DuplicateFunctionIdentifierException(functionIdentifier, metadata);
        }

        if (isAddingNewUnsetUserDefinedFunction(functionIdentifier, executionMode)) {
            this.userDefinedFunctionIdentifiersWithUnsetExecutionModes.add(functionIdentifier);
        } else if (isUpdatingUnsetUserDefinedFunctionToNonUnset(functionIdentifier, executionMode)) {
            this.userDefinedFunctionIdentifiersWithUnsetExecutionModes.remove(functionIdentifier);
        }
        this.userDefinedFunctionsExecutionMode.put(functionIdentifier, executionMode);
    }

    public List<ExecutionMode> getParameterExecutionMode(
            FunctionIdentifier identifier,
            ExceptionMetadata metadata
    ) {
        if (exists(identifier)) {
            return this.userDefinedFunctionsParametersStorageMode.get(identifier);
        }
        List<ExecutionMode> newModes = new ArrayList<>();
        for (int i = 0; i < identifier.getArity(); ++i) {
            newModes.add(ExecutionMode.UNSET);
        }
        this.userDefinedFunctionsParametersStorageMode.put(identifier, newModes);
        return newModes;
    }

    public void setParameterExecutionMode(
            FunctionIdentifier functionIdentifier,
            List<ExecutionMode> executionModes,
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
                newModes.add(oldMode);
                continue;
            }
            if (oldMode == updatedMode) {
                newModes.add(oldMode);
                continue;
            }
            throw new OurBadException(
                    "Conflicting execution modes in user-defined function parameters for function: "
                        + functionIdentifier.getName()
                        + " with arity: "
                        + functionIdentifier.getArity()
                        + ". This happens when the same function is used in a setting with big sequences and another with small sequences, which is an unsupported feature. If you need this, please let us know so we can prioritize."
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



    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Execution modes: \n");
        for (FunctionIdentifier fi : this.userDefinedFunctionsExecutionMode.keySet()) {
            stringBuilder.append(fi + ": " + this.userDefinedFunctionsExecutionMode.get(fi));
            stringBuilder.append("\n");
        }
        for (FunctionIdentifier fi : this.userDefinedFunctionsParametersStorageMode.keySet()) {
            List<ExecutionMode> modes = this.userDefinedFunctionsParametersStorageMode.get(fi);
            List<String> modeStrings = modes.stream().map(x -> x.toString()).collect(Collectors.toList());
            stringBuilder.append(fi + " parameters: " + String.join(", ", modeStrings));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
