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
import org.junit.Assert;
import org.junit.Test;
import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.BuiltinFunction;
import org.rumbledb.context.BuiltinFunctionCatalogue;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.PartiallyAppliedFunctionItem.ArgumentBinding;
import org.rumbledb.items.PartiallyAppliedFunctionItem.LocalBinding;
import org.rumbledb.items.PartiallyAppliedFunctionItem.PlaceholderBinding;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

public class PartiallyAppliedFunctionItemTest {

    @Test
    public void localBindingsAndBindingListAreImmutable() {
        List<Item> capturedValue = new ArrayList<>();
        capturedValue.add(ItemFactory.getInstance().createIntItem(1));
        LocalBinding binding = new LocalBinding(parameterType(), capturedValue);
        capturedValue.add(ItemFactory.getInstance().createIntItem(2));
        Assert.assertEquals(1, binding.value().size());

        List<ArgumentBinding> bindings = new ArrayList<>();
        bindings.add(binding);
        PartiallyAppliedFunctionItem partial = createPartial(builtinTarget(), bindings, 0);
        bindings.clear();
        Assert.assertEquals(1, partial.getArgumentBindings().size());
    }

    @Test
    public void deepCopyPreservesNestedPartialFunctionType() {
        PartiallyAppliedFunctionItem inner = createPartial(
            builtinTarget(),
            List.of(new PlaceholderBinding(parameterType())),
            1
        );
        PartiallyAppliedFunctionItem outer = createPartial(
            inner,
            List.of(new PlaceholderBinding(parameterType())),
            1
        );

        FunctionItem copy = outer.deepCopy();
        Assert.assertTrue(copy instanceof PartiallyAppliedFunctionItem);
        Item copiedTarget = ((PartiallyAppliedFunctionItem) copy).getTargetFunction();
        Assert.assertTrue(copiedTarget instanceof PartiallyAppliedFunctionItem);
    }

    @Test
    public void kryoRoundTripPreservesLocalBindings() {
        PartiallyAppliedFunctionItem partial = createPartial(
            builtinTarget(),
            List.of(
                new LocalBinding(
                        parameterType(),
                        List.of(ItemFactory.getInstance().createIntItem(7))
                )
            ),
            0
        );
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        Output output = new Output(4096, -1);
        kryo.writeClassAndObject(output, partial);
        Input input = new Input(output.toBytes());

        Object deserialized = kryo.readClassAndObject(input);
        Assert.assertTrue(deserialized instanceof PartiallyAppliedFunctionItem);
        ArgumentBinding binding = ((PartiallyAppliedFunctionItem) deserialized).getArgumentBindings().get(0);
        Assert.assertTrue(binding instanceof LocalBinding);
        Assert.assertEquals(7, ((LocalBinding) binding).value().get(0).getIntValue());
    }

    private static PartiallyAppliedFunctionItem createPartial(
            Item target,
            List<ArgumentBinding> bindings,
            int arity
    ) {
        List<Name> parameterNames = arity == 0
            ? List.of()
            : List.of(Name.createVariableInNoNamespace("$p0"));
        List<SequenceType> parameterTypes = arity == 0 ? List.of() : List.of(parameterType());
        return new PartiallyAppliedFunctionItem(
                new FunctionIdentifier(null, arity),
                parameterNames,
                new FunctionSignature(parameterTypes, target.getSignature().getReturnType(), false),
                target.getModuleDynamicContext(),
                target,
                bindings
        );
    }

    private static FunctionItem builtinTarget() {
        RumbleRuntimeConfiguration configuration = new RumbleRuntimeConfiguration(new String[] {});
        DynamicContext context = new DynamicContext(configuration);
        FunctionIdentifier identifier = new FunctionIdentifier(
                Name.createVariableInDefaultFunctionNamespace("max"),
                1
        );
        BuiltinFunction builtin = BuiltinFunctionCatalogue.getBuiltinFunction(identifier);
        return FunctionItemFactory.createBuiltinNamedReference(
            identifier,
            context,
            configuration,
            ExceptionMetadata.EMPTY_METADATA,
            builtin
        );
    }

    private static SequenceType parameterType() {
        FunctionIdentifier identifier = new FunctionIdentifier(
                Name.createVariableInDefaultFunctionNamespace("max"),
                1
        );
        return BuiltinFunctionCatalogue.getBuiltinFunction(identifier).getSignature().getParameterTypes().get(0);
    }
}
