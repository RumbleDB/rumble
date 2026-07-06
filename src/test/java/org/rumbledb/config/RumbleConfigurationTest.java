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

import org.junit.Assert;
import org.junit.Test;
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

        Assert.assertEquals(25, configuration.runtime().resultsSizeCap());
        Assert.assertFalse(configuration.runtime().useNativeExecution());
        Assert.assertEquals("output.json", configuration.output().outputPath());
        Assert.assertTrue(configuration.output().allowOverwrite());
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

        Assert.assertEquals(25, updated.runtime().resultsSizeCap());
        Assert.assertEquals(42, updated.runtime().materializationCap());
        Assert.assertFalse(updated.runtime().useParallelExecution());
        Assert.assertFalse(updated.runtime().useNativeExecution());
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

        Assert.assertEquals(RumbleMode.RUN, configuration.mode());
        Assert.assertEquals("queries/main.jq", configuration.input().queryPath());
        Assert.assertEquals(100, configuration.runtime().resultsSizeCap());
        Assert.assertEquals(42, configuration.runtime().materializationCap());
        Assert.assertTrue(configuration.debug().showErrorInfo());
    }

    @Test
    public void withEntriesOverrideTypedBuilderValues() {
        RumbleConfiguration configuration = RumbleConfiguration.builder()
            .mode(RumbleMode.RUN)
            .configureRuntime(runtime -> runtime.resultsSizeCap(25))
            .with("mode", "REPL")
            .with("runtime.resultsSizeCap", 100)
            .build();

        Assert.assertEquals(RumbleMode.REPL, configuration.mode());
        Assert.assertEquals(100, configuration.runtime().resultsSizeCap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void withUnknownEntryFailsFast() {
        RumbleConfiguration.builder()
            .with("runtime.unknownOption", true)
            .build();
    }

    @Test
    public void accessConfigDefensivelyCopiesAllowedPrefixes() {
        List<String> prefixes = new ArrayList<>(List.of("file:"));
        AccessConfig configuration = AccessConfig.builder()
            .allowedPrefixes(prefixes)
            .build();

        prefixes.add("https:");

        Assert.assertEquals(List.of("file:"), configuration.allowedPrefixes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void accessConfigAllowedPrefixesCannotBeMutatedThroughAccessor() {
        AccessConfig.builder()
            .allowedPrefixes(List.of("file:"))
            .build()
            .allowedPrefixes()
            .add("https:");
    }
}
