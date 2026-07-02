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

package org.rumbledb.api;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.rumbledb.config.model.ExecutionMode;

public class RumbleConfigurationTest {

    @Test
    public void getReturnsPlainJavaValues() {
        RumbleConfiguration configuration = configuration(
            org.rumbledb.config.RumbleConfiguration.builder()
                .executionMode(ExecutionMode.SERVE)
                .runtime(runtime -> runtime.resultsSizeCap(100))
                .debug(debug -> debug.showErrorInfo(true))
                .access(access -> access.allowedPrefixes(List.of("file:", "https:")))
                .build()
        );

        Assert.assertEquals("SERVE", configuration.get("executionMode"));
        Assert.assertEquals(100, configuration.get("runtime.resultsSizeCap"));
        Assert.assertEquals(true, configuration.get("debug.showErrorInfo"));
        Assert.assertEquals(List.of("file:", "https:"), configuration.get("access.allowedPrefixes"));
    }

    @Test
    public void getReturnsConfigurationSectionsAsMaps() {
        RumbleConfiguration configuration = configuration(
            org.rumbledb.config.RumbleConfiguration.builder()
                .server(server -> server.host("example.org").port(9000))
                .build()
        );

        Assert.assertEquals(
            Map.of("host", "example.org", "port", 9000),
            configuration.get("server")
        );
    }

    @Test
    public void getConvertsValueToRequestedType() {
        RumbleConfiguration configuration = configuration(
            org.rumbledb.config.RumbleConfiguration.builder()
                .executionMode(ExecutionMode.REPL)
                .build()
        );

        Assert.assertEquals(ExecutionMode.REPL, configuration.get("executionMode", ExecutionMode.class));
    }

    @Test
    public void getReturnsNullForConfiguredNullValue() {
        RumbleConfiguration configuration = new RumbleConfiguration();

        Assert.assertNull(configuration.get("input.queryPath"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getRejectsUnknownPath() {
        new RumbleConfiguration().get("runtime.unknownOption");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getRejectsInvalidPath() {
        new RumbleConfiguration().get("runtime..resultsSizeCap");
    }

    private static RumbleConfiguration configuration(
            org.rumbledb.config.RumbleConfiguration internalConfiguration
    ) {
        return new RumbleConfiguration(internalConfiguration);
    }
}
