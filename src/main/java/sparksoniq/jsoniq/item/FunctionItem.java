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
import sparksoniq.exceptions.FunctionsNonSerializableException;
import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionIdentifier;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionItem extends Item {

    private static final long serialVersionUID = 1L;
    private FunctionIdentifier identifier;
    private List<String> parameterNames;

    // signature contains type information for all parameters and the return value
    private List<SequenceType> signature;

    private Expression bodyExpression;
    private Map<String, List<Item>> nonLocalVariableBindings;

    protected FunctionItem() {
        super();
    }

    public FunctionItem(FunctionIdentifier identifier, List<String> parameterNames, List<SequenceType> signature, Expression bodyExpression, Map<String, List<Item>> nonLocalVariableBindings) {
        this.identifier = identifier;
        this.parameterNames = parameterNames;
        this.signature = signature;
        this.bodyExpression = bodyExpression;
        this.nonLocalVariableBindings = nonLocalVariableBindings;
    }

    public FunctionItem(String name, Map<String, SequenceType> paramNameToSequenceTypes, SequenceType returnType, Expression bodyExpression) {
        List<String> paramNames = new ArrayList<>();
        List<SequenceType> signature = new ArrayList<>();
        for (Map.Entry<String, SequenceType> paramEntry : paramNameToSequenceTypes.entrySet()) {
            paramNames.add(paramEntry.getKey());
            signature.add(paramEntry.getValue());
        }
        signature.add(returnType);

        this.identifier = new FunctionIdentifier(name, paramNames.size());
        this.parameterNames = paramNames;
        this.signature = signature;
        this.bodyExpression = bodyExpression;
        this.nonLocalVariableBindings = new HashMap<>();
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

    public Expression getBodyExpression() {
        return bodyExpression;
    }

    public Map<String, List<Item>> getNonLocalVariableBindings() {
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
        throw new FunctionsNonSerializableException();
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
        kryo.writeObject(output, this.bodyExpression);
        kryo.writeObject(output, this.nonLocalVariableBindings);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.identifier = kryo.readObject(input, FunctionIdentifier.class);
        this.parameterNames = kryo.readObject(input, ArrayList.class);
        this.signature = kryo.readObject(input, ArrayList.class);
        this.bodyExpression = kryo.readObject(input, Expression.class);
        this.nonLocalVariableBindings = kryo.readObject(input, HashMap.class);
    }
}
