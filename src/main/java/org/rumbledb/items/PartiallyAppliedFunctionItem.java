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
 */
package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.SequenceType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Function item produced by partial application.
 *
 * It stores the original target and an argument binding for every target parameter. Invocation expands
 * these bindings and delegates to the ordinary function-item dispatch path.
 */
public class PartiallyAppliedFunctionItem extends FunctionItem {

    private static final long serialVersionUID = 1L;

    public sealed interface ArgumentBinding extends Serializable
            permits PlaceholderBinding, LocalBinding, RddBinding, DataFrameBinding {

        SequenceType sequenceType();
    }

    public record PlaceholderBinding(SequenceType sequenceType) implements ArgumentBinding {

        private static final long serialVersionUID = 1L;
    }

    public record LocalBinding(SequenceType sequenceType, List<Item> value) implements ArgumentBinding {

        private static final long serialVersionUID = 1L;

        public LocalBinding {
            value = List.copyOf(value);
        }
    }

    public record RddBinding(SequenceType sequenceType, JavaRDD<Item> value) implements ArgumentBinding {

        private static final long serialVersionUID = 1L;
    }

    public record DataFrameBinding(SequenceType sequenceType, JSoundDataFrame value) implements ArgumentBinding {

        private static final long serialVersionUID = 1L;
    }

    private Item targetFunction;
    private List<ArgumentBinding> argumentBindings;

    public PartiallyAppliedFunctionItem() {
        super();
    }

    public PartiallyAppliedFunctionItem(
            FunctionIdentifier identifier,
            List<Name> parameterNames,
            FunctionSignature signature,
            DynamicContext dynamicModuleContext,
            Item targetFunction,
            List<ArgumentBinding> argumentBindings
    ) {
        super(
            identifier,
            parameterNames,
            signature,
            dynamicModuleContext,
            getTargetBody(targetFunction),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>(),
            false
        );
        validateBindings(identifier, targetFunction, argumentBindings);
        this.targetFunction = targetFunction;
        this.argumentBindings = List.copyOf(argumentBindings);
    }

    private static org.rumbledb.runtime.RuntimeIterator getTargetBody(Item targetFunction) {
        if (targetFunction == null || !targetFunction.isFunction()) {
            throw new OurBadException("A partially applied function must have a function target.");
        }
        return targetFunction.getBodyIterator();
    }

    public Item getTargetFunction() {
        return this.targetFunction;
    }

    public List<ArgumentBinding> getArgumentBindings() {
        return this.argumentBindings;
    }

    private static void validateBindings(
            FunctionIdentifier identifier,
            Item targetFunction,
            List<ArgumentBinding> argumentBindings
    ) {
        if (argumentBindings.size() != targetFunction.getIdentifier().getArity()) {
            throw new OurBadException("Partial-function bindings do not match the target arity.");
        }
        long placeholderCount = argumentBindings.stream().filter(PlaceholderBinding.class::isInstance).count();
        if (placeholderCount != identifier.getArity()) {
            throw new OurBadException("Partial-function placeholders do not match the partial signature.");
        }
    }

    @Override
    public void write(Kryo kryo, Output output) {
        super.write(kryo, output);
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream objects = new ObjectOutputStream(bytes);
            objects.writeObject(this.targetFunction);
            objects.writeObject(this.argumentBindings);
            objects.flush();
            byte[] serialized = bytes.toByteArray();
            output.writeInt(serialized.length);
            output.writeBytes(serialized);
        } catch (IOException e) {
            throw new IllegalStateException("Could not serialize partially applied function metadata.", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        super.read(kryo, input);
        try {
            int length = input.readInt();
            byte[] serialized = input.readBytes(length);
            ObjectInputStream objects = new ObjectInputStream(new ByteArrayInputStream(serialized));
            this.targetFunction = (Item) objects.readObject();
            this.argumentBindings = List.copyOf((List<ArgumentBinding>) objects.readObject());
            validateBindings(getIdentifier(), this.targetFunction, this.argumentBindings);
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Could not deserialize partially applied function metadata.", e);
        }
    }
}
