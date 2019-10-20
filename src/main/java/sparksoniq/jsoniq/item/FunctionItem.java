/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import org.rumbledb.api.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionIdentifier;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;

import javax.sound.midi.Sequence;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionItem extends Item {

    private static final long serialVersionUID = 1L;
    private FunctionIdentifier identifier;
    private List<String> parameterNames;

    // signature contains sequence types of all parameters and the return type
    private List<SequenceType> signature;

    // Implementation is the RuntimeIterator of the comma expression located in function's body
    private RuntimeIterator implementation;
    private Map<String, Item> nonLocalVariableBindings;

    protected FunctionItem() {
        super();
    }

    public FunctionItem(FunctionIdentifier identifier, List<String> parameterNames, List<SequenceType> signature, RuntimeIterator implementation, Map<String, Item> nonLocalVariableBindings) {
        this.identifier = identifier;
        this.parameterNames = parameterNames;
        this.signature = signature;
        this.implementation = implementation;
        this.nonLocalVariableBindings = nonLocalVariableBindings;
    }

    public FunctionIdentifier getIdentifier() {
        return identifier;
    }

    public List<String> getParameterNames() {
        return parameterNames;
    }

    public List<SequenceType> getSignature() {
        return signature;
    }

    public RuntimeIterator getImplementation() {
        return implementation;
    }

    public Map<String, Item> getNonLocalVariableBindings() {
        return nonLocalVariableBindings;
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
    public String serialize() {
        throw new RuntimeException("Functions cannot be serialized into output");
    }

    @Override
    public int hashCode() {
        return this.identifier.hashCode() + String.join("", this.parameterNames).hashCode();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.identifier);
        kryo.writeObject(output, this.parameterNames);
        kryo.writeObject(output, this.signature);
        kryo.writeObject(output, this.implementation);
        kryo.writeObject(output, this.nonLocalVariableBindings);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.identifier = kryo.readObject(input, FunctionIdentifier.class);
        this.parameterNames = kryo.readObject(input, ArrayList.class);
        this.signature = kryo.readObject(input, ArrayList.class);
        this.implementation = kryo.readObject(input, RuntimeIterator.class);
        this.nonLocalVariableBindings = kryo.readObject(input, HashMap.class);
    }
}
