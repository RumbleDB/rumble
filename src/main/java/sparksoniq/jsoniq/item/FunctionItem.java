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

package sparksoniq.jsoniq.item;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.FunctionsNonSerializableException;
import org.rumbledb.exceptions.OurBadException;

import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionIdentifier;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionSignature;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionItem extends Item {

    private static final long serialVersionUID = 1L;
    private FunctionIdentifier identifier;
    private List<String> parameterNames;

    // signature contains type information for all parameters and the return value
    private FunctionSignature signature;
    private RuntimeIterator bodyIterator;
    private Map<String, List<Item>> _localVariablesInClosure;
    private Map<String, JavaRDD<Item>> _RDDVariablesInClosure;
    private Map<String, Dataset<Row>> _DFVariablesInClosure;

    protected FunctionItem() {
        super();
    }

    public FunctionItem(
            FunctionIdentifier identifier,
            List<String> parameterNames,
            FunctionSignature signature,
            RuntimeIterator bodyIterator
    ) {
        this.identifier = identifier;
        this.parameterNames = parameterNames;
        this.signature = signature;
        this.bodyIterator = bodyIterator;
        this._localVariablesInClosure = new HashMap<>();
        this._RDDVariablesInClosure = new HashMap<>();
        this._DFVariablesInClosure = new HashMap<>();
    }

    public FunctionItem(
            FunctionIdentifier identifier,
            List<String> parameterNames,
            FunctionSignature signature,
            RuntimeIterator bodyIterator,
            Map<String, List<Item>> localVariablesInClosure,
            Map<String, JavaRDD<Item>> RDDVariablesInClosure,
            Map<String, Dataset<Row>> DFVariablesInClosure
    ) {
        this.identifier = identifier;
        this.parameterNames = parameterNames;
        this.signature = signature;
        this.bodyIterator = bodyIterator;
        this._localVariablesInClosure = localVariablesInClosure;
        this._RDDVariablesInClosure = RDDVariablesInClosure;
        this._DFVariablesInClosure = DFVariablesInClosure;
    }

    public FunctionItem(
            String name,
            Map<String, SequenceType> paramNameToSequenceTypes,
            SequenceType returnType,
            RuntimeIterator bodyIterator
    ) {
        List<String> paramNames = new ArrayList<>();
        List<SequenceType> parameters = new ArrayList<>();
        for (Map.Entry<String, SequenceType> paramEntry : paramNameToSequenceTypes.entrySet()) {
            paramNames.add(paramEntry.getKey());
            parameters.add(paramEntry.getValue());
        }

        this.identifier = new FunctionIdentifier(name, paramNames.size());
        this.parameterNames = paramNames;
        this.signature = new FunctionSignature(parameters, returnType);
        this.bodyIterator = bodyIterator;
        this._localVariablesInClosure = new HashMap<>();
        this._RDDVariablesInClosure = new HashMap<>();
        this._DFVariablesInClosure = new HashMap<>();
    }

    public FunctionIdentifier getIdentifier() {
        return identifier;
    }

    public List<String> getParameterNames() {
        return parameterNames;
    }

    public FunctionSignature getSignature() {
        return signature;
    }

    public RuntimeIterator getBodyIterator() {
        return bodyIterator;
    }

    public Map<String, List<Item>> getLocalVariablesInClosure() {
        return _localVariablesInClosure;
    }

    public Map<String, JavaRDD<Item>> getRDDVariablesInClosure() {
        return _RDDVariablesInClosure;
    }

    public Map<String, Dataset<Row>> getDFVariablesInClosure() {
        return _DFVariablesInClosure;
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
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.FunctionItem) || type.getType().equals(ItemTypes.Item);
    }

    @Override
    public boolean isFunction() {
        return true;
    }

    @Override
    public String serialize() {
        throw new FunctionsNonSerializableException();
    }

    @Override
    public int hashCode() {
        return this.identifier.hashCode() + String.join("", this.parameterNames).hashCode() + this.signature.hashCode();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.identifier);
        kryo.writeObject(output, this.parameterNames);
        kryo.writeObject(output, this.signature.getParameterTypes());
        kryo.writeObject(output, this.signature.getReturnType());
        // kryo.writeObject(output, this.bodyIterator);
        kryo.writeObject(output, this._localVariablesInClosure);
        kryo.writeObject(output, this._RDDVariablesInClosure);
        kryo.writeObject(output, this._DFVariablesInClosure);

        // convert RuntimeIterator to byte[] data
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this.bodyIterator);
            oos.flush();
            byte[] data = bos.toByteArray();
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
        List<SequenceType> parameters = kryo.readObject(input, ArrayList.class);
        SequenceType returnType = kryo.readObject(input, SequenceType.class);
        this.signature = new FunctionSignature(parameters, returnType);
        // this.bodyIterator = kryo.readObject(input, RuntimeIterator.class);
        this._localVariablesInClosure = kryo.readObject(input, HashMap.class);
        this._RDDVariablesInClosure = kryo.readObject(input, HashMap.class);
        this._DFVariablesInClosure = kryo.readObject(input, HashMap.class);

        try {
            int dataLength = input.readInt();
            byte[] data = input.readBytes(dataLength);
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bis);
            this.bodyIterator = (RuntimeIterator) ois.readObject();
        } catch (Exception e) {
            throw new OurBadException(
                    "Error converting functionItem-bodyRuntimeIterator to functionItem:" + e.getMessage()
            );
        }
    }
}
