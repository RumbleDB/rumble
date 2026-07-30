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

package org.rumbledb.config;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rumbledb.bindings.DataFrameBinding;
import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.bindings.FileBinding;
import org.rumbledb.bindings.InputFormat;
import org.rumbledb.bindings.ItemSequenceBinding;
import org.rumbledb.bindings.LexicalBinding;
import org.rumbledb.bindings.StandardInputBinding;
import org.rumbledb.config.model.RumbleMode;
import org.rumbledb.context.Name;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.serialization.SerializationParameters;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer;

public class KryoFieldSerializationTest {

    @Test
    public void configurationRoundTripsWithFieldSerializer() {
        Kryo kryo = new Kryo();
        RumbleConfiguration configuration = RumbleConfiguration.builder()
            .mode(RumbleMode.REPL)
            .configureInput(input -> input.query("1 + 1"))
            .configureOutput(
                output -> output
                    .outputPath("output.json")
                    .allowOverwrite(true)
                    .serializationParameters(
                        SerializationParameterBuilder.build(
                            Map.of("indent", "yes", "compression", "gzip")
                        )
                    )
            )
            .configureRuntime(runtime -> runtime.resultsSizeCap(25).useNativeExecution(false))
            .configureDebug(debug -> debug.showErrorInfo(true))
            .configureAnalysis(analysis -> analysis.enableStaticTyping(true))
            .configureOptimization(optimization -> optimization.optimizeParentPointers(false))
            .configureSemantics(semantics -> semantics.queryLanguage("xquery31").xmlVersion("1.0"))
            .configureFormatting(formatting -> formatting.defaultFormattingPlace("Europe/Madrid"))
            .build();

        Assertions.assertInstanceOf(FieldSerializer.class, kryo.getSerializer(RumbleConfiguration.class));

        RumbleConfiguration copy = roundTrip(kryo, configuration, RumbleConfiguration.class);

        Assertions.assertEquals(configuration.mode(), copy.mode());
        Assertions.assertEquals(configuration.input(), copy.input());
        Assertions.assertEquals(configuration.runtime(), copy.runtime());
        Assertions.assertEquals(configuration.debug(), copy.debug());
        Assertions.assertEquals(configuration.analysis(), copy.analysis());
        Assertions.assertEquals(configuration.optimization(), copy.optimization());
        Assertions.assertEquals(configuration.semantics(), copy.semantics());
        Assertions.assertEquals(configuration.formatting(), copy.formatting());
        Assertions.assertEquals(configuration.output().outputPath(), copy.output().outputPath());
        Assertions.assertEquals(configuration.output().allowOverwrite(), copy.output().allowOverwrite());

        SerializationParameters serializationParameters = copy.output().serializationParameters();
        Assertions.assertNotNull(serializationParameters);
        Assertions.assertTrue(serializationParameters.getIndent());
        Assertions.assertEquals("gzip", serializationParameters.getSparkOptions().get("compression"));
    }

    @Test
    public void polymorphicBindingsRoundTripWithFieldSerializer() {
        Kryo kryo = new Kryo();
        ExternalBindings bindings = ExternalBindings.empty();
        bindings.bind(Name.createVariableInNoNamespace("lexical"), new LexicalBinding("42"));
        bindings.bind(
            Name.createVariableInNoNamespace("file"),
            new FileBinding("input.txt", InputFormat.TEXT)
        );
        bindings.bind(
            Name.createVariableInNoNamespace("stdin"),
            new StandardInputBinding(InputFormat.JSON)
        );
        bindings.bind(
            Name.createVariableInNoNamespace("items"),
            new ItemSequenceBinding(List.of(ItemFactory.getInstance().createStringItem("value")))
        );

        Assertions.assertInstanceOf(FieldSerializer.class, kryo.getSerializer(ExternalBindings.class));
        Assertions.assertInstanceOf(FieldSerializer.class, kryo.getSerializer(DataFrameBinding.class));

        ExternalBindings copy = roundTrip(kryo, bindings, ExternalBindings.class);

        Assertions.assertEquals(
            "42",
            copy.get(Name.createVariableInNoNamespace("lexical"), LexicalBinding.class)
                .orElseThrow()
                .getValue()
        );
        FileBinding file = copy.get(Name.createVariableInNoNamespace("file"), FileBinding.class)
            .orElseThrow();
        Assertions.assertEquals("input.txt", file.getLocation());
        Assertions.assertEquals(InputFormat.TEXT, file.getFormat());
        Assertions.assertEquals(
            InputFormat.JSON,
            copy.get(Name.createVariableInNoNamespace("stdin"), StandardInputBinding.class)
                .orElseThrow()
                .getFormat()
        );
        Assertions.assertEquals(
            "value",
            copy.get(Name.createVariableInNoNamespace("items"), ItemSequenceBinding.class)
                .orElseThrow()
                .getItems()
                .get(0)
                .getStringValue()
        );
    }

    private static <T> T roundTrip(Kryo kryo, T value, Class<T> valueClass) {
        Output output = new Output(4096, -1);
        kryo.writeObject(output, value);
        output.close();

        Input input = new Input(output.toBytes());
        T copy = kryo.readObject(input, valueClass);
        input.close();
        return copy;
    }
}
