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

import org.junit.Assert;
import org.junit.Test;
import org.rumbledb.config.model.RuntimeConfig;

public class RumbleConfigurationTest {

    @Test
    public void lambdaCustomizersConfigureSections() {
        RumbleConfiguration configuration = RumbleConfiguration.builder()
            .configureRuntime(runtime -> runtime.resultsSizeCap(25).useNativeExecution(false))
            .configureOutput(output -> output.outputPath("output.json").allowOverwrite(true))
            .configureServer(server -> server.host("example.org").port(9000))
            .build();

        Assert.assertEquals(25, configuration.runtime().resultsSizeCap());
        Assert.assertFalse(configuration.runtime().useNativeExecution());
        Assert.assertEquals("output.json", configuration.output().outputPath());
        Assert.assertTrue(configuration.output().allowOverwrite());
        Assert.assertEquals("example.org", configuration.server().host());
        Assert.assertEquals(9000, configuration.server().port());
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
}
