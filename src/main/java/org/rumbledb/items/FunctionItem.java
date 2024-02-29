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

package org.rumbledb.items;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.ml.Estimator;
import org.apache.spark.ml.Transformer;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import sparksoniq.spark.ml.ApplyEstimatorRuntimeIterator;
import sparksoniq.spark.ml.ApplyTransformerRuntimeIterator;

public class FunctionItem implements Item {

    private static final long serialVersionUID = 1L;
    private FunctionIdentifier identifier;
    private List<Name> parameterNames;

    // signature contains type information for all parameters and the return value
    private FunctionSignature signature;
    private RuntimeIterator bodyIterator;
    private DynamicContext dynamicModuleContext;
    private Map<Name, List<Item>> localVariablesInClosure;
    private Map<Name, JavaRDD<Item>> RDDVariablesInClosure;
    private Map<Name, JSoundDataFrame> dataFrameVariablesInClosure;

    protected FunctionItem() {
        super();
    }

    public FunctionItem(
            FunctionIdentifier identifier,
            List<Name> parameterNames,
            FunctionSignature signature,
            DynamicContext dynamicModuleContext,
            RuntimeIterator bodyIterator
    ) {
        this.identifier = identifier;
        this.parameterNames = parameterNames;
        this.signature = signature;
        this.bodyIterator = bodyIterator;
        this.dynamicModuleContext = dynamicModuleContext;
        this.localVariablesInClosure = new HashMap<>();
        this.RDDVariablesInClosure = new HashMap<>();
        this.dataFrameVariablesInClosure = new HashMap<>();
    }

    public FunctionItem(
            FunctionIdentifier identifier,
            List<Name> parameterNames,
            FunctionSignature signature,
            DynamicContext dynamicModuleContext,
            RuntimeIterator bodyIterator,
            Map<Name, List<Item>> localVariablesInClosure,
            Map<Name, JavaRDD<Item>> RDDVariablesInClosure,
            Map<Name, JSoundDataFrame> DFVariablesInClosure
    ) {
        this.identifier = identifier;
        this.parameterNames = parameterNames;
        this.signature = signature;
        this.bodyIterator = bodyIterator;
        this.dynamicModuleContext = dynamicModuleContext;
        this.localVariablesInClosure = localVariablesInClosure;
        this.RDDVariablesInClosure = RDDVariablesInClosure;
        this.dataFrameVariablesInClosure = DFVariablesInClosure;
    }

    public FunctionItem(
            Name name,
            Map<Name, SequenceType> paramNameToSequenceTypes,
            SequenceType returnType,
            DynamicContext dynamicModuleContext,
            RuntimeIterator bodyIterator
    ) {
        List<Name> paramNames = new ArrayList<>();
        List<SequenceType> parameters = new ArrayList<>();
        for (Map.Entry<Name, SequenceType> paramEntry : paramNameToSequenceTypes.entrySet()) {
            paramNames.add(paramEntry.getKey());
            parameters.add(paramEntry.getValue());
        }

        this.identifier = new FunctionIdentifier(name, paramNames.size());
        this.parameterNames = paramNames;
        this.signature = new FunctionSignature(parameters, returnType);
        this.bodyIterator = bodyIterator;
        this.dynamicModuleContext = dynamicModuleContext;
        this.localVariablesInClosure = new HashMap<>();
        this.RDDVariablesInClosure = new HashMap<>();
        this.dataFrameVariablesInClosure = new HashMap<>();
    }

    @Override
    public FunctionIdentifier getIdentifier() {
        return this.identifier;
    }

    @Override
    public List<Name> getParameterNames() {
        return this.parameterNames;
    }

    @Override
    public FunctionSignature getSignature() {
        return this.signature;
    }

    public DynamicContext getModuleDynamicContext() {
        return this.dynamicModuleContext;
    }

    public RuntimeIterator getBodyIterator() {
        return this.bodyIterator;
    }

    public Map<Name, List<Item>> getLocalVariablesInClosure() {
        return this.localVariablesInClosure;
    }

    public Map<Name, JavaRDD<Item>> getRDDVariablesInClosure() {
        return this.RDDVariablesInClosure;
    }

    public Map<Name, JSoundDataFrame> getDFVariablesInClosure() {
        return this.dataFrameVariablesInClosure;
    }

    @Override
    public boolean equals(Object other) {
        // functions can not be compared
        return false;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return false;
    }

    @Override
    public boolean isAtomic() {
        return false;
    }

    @Override
    public boolean isFunction() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Function\n");
        sb.append("Identifier:" + this.identifier + "\n");
        sb.append("Parameters: ");
        for (Name param : this.parameterNames) {
            sb.append(param + " ");
        }
        sb.append("Signature: " + this.signature + "\n");
        sb.append("Body:\n" + this.bodyIterator + "\n");
        sb.append("Closure:\n");
        sb.append("  Local:\n");
        for (Name name : this.localVariablesInClosure.keySet()) {
            sb.append("    " + name + " (" + this.localVariablesInClosure.get(name).size() + " items)\n");
            if (this.localVariablesInClosure.get(name).size() == 1) {
                sb.append("      " + this.localVariablesInClosure.get(name).get(0).serialize() + "\n");
            }
        }
        sb.append("  RDD:\n");
        for (Name name : this.RDDVariablesInClosure.keySet()) {
            sb.append("    " + name + " (" + this.RDDVariablesInClosure.get(name).count() + " items)\n");
        }
        sb.append("  Data Frames:\n");
        for (Name name : this.dataFrameVariablesInClosure.keySet()) {
            sb.append("    " + name + " (" + this.dataFrameVariablesInClosure.get(name).count() + " items)\n");
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return this.identifier.hashCode()
            + String.join("", this.parameterNames.toString()).hashCode()
            + this.signature.hashCode();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.identifier);
        kryo.writeObject(output, this.parameterNames);
        kryo.writeObject(output, this.signature);
        kryo.writeObject(output, this.localVariablesInClosure);
        if (!this.RDDVariablesInClosure.isEmpty()) {
            throw new OurBadException(
                    "We do not support serializing RDDs in function closures."
            );
        }
        if (!this.dataFrameVariablesInClosure.isEmpty()) {
            throw new OurBadException(
                    "We do not support serializing DataFrames in function closures."
            );
        }
        // kryo.writeObject(output, this.dynamicModuleContext);

        // convert RuntimeIterator to byte[] data
        try {
            byte[] data = SerializationUtils.serialize(this.bodyIterator);
            output.writeInt(data.length);
            output.writeBytes(data);
        } catch (Exception e) {
            throw new OurBadException(
                    "Error converting functionItem-bodyRuntimeIterator to byte[]:" + e.getMessage()
            );
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.identifier = kryo.readObject(input, FunctionIdentifier.class);
        this.parameterNames = kryo.readObject(input, ArrayList.class);
        this.signature = kryo.readObject(input, FunctionSignature.class);
        this.localVariablesInClosure = kryo.readObject(input, HashMap.class);
        this.RDDVariablesInClosure = new HashMap<>();
        this.dataFrameVariablesInClosure = new HashMap<>();
        // this.dynamicModuleContext = kryo.readObject(input, DynamicContext.class);
        // this.bodyIterator = kryo.readObject(input, RuntimeIterator.class);

        try {
            int dataLength = input.readInt();
            byte[] data = input.readBytes(dataLength);
            this.bodyIterator = SerializationUtils.deserialize(data);
        } catch (Exception e) {
            throw new OurBadException(
                    "Error converting functionItem-bodyRuntimeIterator to functionItem:" + e.getMessage()
            );
        }
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.anyFunctionItem;
    }

    public FunctionItem deepCopy() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
            byte[] data = bos.toByteArray();
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (FunctionItem) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            RumbleException rumbleException = new OurBadException(
                    "Error while deep copying the function body runtimeIterator"
            );
            rumbleException.initCause(e);
            throw rumbleException;
        }
    }

    public void populateClosureFromDynamicContext(DynamicContext dynamicContext, ExceptionMetadata metadata) {
        for (Name variable : dynamicContext.getVariableValues().getLocalVariableNames()) {
            this.localVariablesInClosure.put(
                variable,
                dynamicContext.getVariableValues().getLocalVariableValue(variable, metadata)
            );
        }
        for (Name variable : dynamicContext.getVariableValues().getRDDVariableNames()) {
            this.RDDVariablesInClosure.put(
                variable,
                dynamicContext.getVariableValues().getRDDVariableValue(variable, metadata)
            );
        }
        for (Name variable : dynamicContext.getVariableValues().getDataFrameVariableNames()) {
            this.dataFrameVariablesInClosure.put(
                variable,
                dynamicContext.getVariableValues().getDataFrameVariableValue(variable, metadata)
            );
        }
    }

    @Override
    public boolean isEstimator() {
        return this.bodyIterator instanceof ApplyEstimatorRuntimeIterator;
    }

    @Override
    public Estimator<?> getEstimator() {
        if (!isEstimator()) {
            throw new OurBadException("This is not an estimator.", ExceptionMetadata.EMPTY_METADATA);
        }
        return ((ApplyEstimatorRuntimeIterator) this.bodyIterator).getEstimator();
    }

    @Override
    public boolean isTransformer() {
        return this.bodyIterator instanceof ApplyTransformerRuntimeIterator;
    }

    @Override
    public Transformer getTransformer() {
        if (!isTransformer()) {
            throw new OurBadException("This is not a transformer.", ExceptionMetadata.EMPTY_METADATA);
        }
        return ((ApplyTransformerRuntimeIterator) this.bodyIterator).getTransformer();
    }


}
