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
 */

package org.rumbledb.config;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rumbledb.config.model.AccessConfig;
import org.rumbledb.config.model.RumbleMode;
import org.rumbledb.config.model.RuntimeConfig;

public class RumbleConfigurationTest {

    @Test
    public void lambdaCustomizersConfigureSections() {
        RumbleConfiguration configuration = RumbleConfiguration.builder()
            .configureRuntime(runtime -> runtime.resultsSizeCap(25).useNativeExecution(false))
            .configureOutput(output -> output.outputPath("output.json").allowOverwrite(true))
            .build();

        Assertions.assertEquals(25, configuration.runtime().resultsSizeCap());
        Assertions.assertFalse(configuration.runtime().useNativeExecution());
        Assertions.assertEquals("output.json", configuration.output().outputPath());
        Assertions.assertTrue(configuration.output().allowOverwrite());
    }

    @Test
    public void lambdaCustomizersPreserveExistingSectionValues() {
        RumbleConfiguration original = RumbleConfiguration.builder()
            .runtime(
                RuntimeConfig.builder()
                    .materializationCap(42)
                    .useParallelExecution(false)
                    .build()
            )
            .configureRuntime(runtime -> runtime.resultsSizeCap(25))
            .build();

        RumbleConfiguration updated = original.toBuilder()
            .configureRuntime(runtime -> runtime.useNativeExecution(false))
            .build();

        Assertions.assertEquals(25, updated.runtime().resultsSizeCap());
        Assertions.assertEquals(42, updated.runtime().materializationCap());
        Assertions.assertFalse(updated.runtime().useParallelExecution());
        Assertions.assertFalse(updated.runtime().useNativeExecution());
    }

    @Test
    public void withEntriesApplyNestedOverrides() {
        RumbleConfiguration configuration = RumbleConfiguration.builder()
            .configureRuntime(runtime -> runtime.materializationCap(42))
            .with("mode", "RUN")
            .with("input.queryPath", "queries/main.jq")
            .with("runtime.resultsSizeCap", 100)
            .with("debug.showErrorInfo", true)
            .build();

        Assertions.assertEquals(RumbleMode.RUN, configuration.mode());
        Assertions.assertEquals("queries/main.jq", configuration.input().queryPath());
        Assertions.assertEquals(100, configuration.runtime().resultsSizeCap());
        Assertions.assertEquals(42, configuration.runtime().materializationCap());
        Assertions.assertTrue(configuration.debug().showErrorInfo());
    }

    @Test
    public void withEntriesOverrideTypedBuilderValues() {
        RumbleConfiguration configuration = RumbleConfiguration.builder()
            .mode(RumbleMode.RUN)
            .configureRuntime(runtime -> runtime.resultsSizeCap(25))
            .with("mode", "REPL")
            .with("runtime.resultsSizeCap", 100)
            .build();

        Assertions.assertEquals(RumbleMode.REPL, configuration.mode());
        Assertions.assertEquals(100, configuration.runtime().resultsSizeCap());
    }

    @Test()
    public void withUnknownEntryFailsFast() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> RumbleConfiguration.builder()
                .with("runtime.unknownOption", true)
                .build()
        );
    }

    @Test
    public void accessConfigDefensivelyCopiesAllowedPrefixes() {
        List<String> prefixes = new ArrayList<>(List.of("file:"));
        AccessConfig configuration = AccessConfig.builder()
            .allowedPrefixes(prefixes)
            .build();

        prefixes.add("https:");

        Assertions.assertEquals(List.of("file:"), configuration.allowedPrefixes());
    }

    @Test
    public void accessConfigAllowedPrefixesCannotBeMutatedThroughAccessor() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            AccessConfig.builder()
                .allowedPrefixes(List.of("file:"))
                .build()
                .allowedPrefixes()
                .add("https:");
        });
    }
}
