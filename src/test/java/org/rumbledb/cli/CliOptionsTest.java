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

package org.rumbledb.cli;

import org.junit.Test;
import org.rumbledb.config.ExecutionMode;
import org.rumbledb.config.InputOptions;
import org.rumbledb.config.OutputOptions;
import org.rumbledb.config.RumbleConfiguration;

import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CliOptionsTest {

    @Test
    public void noSubcommandShouldGiveError() {
        ///
        try {
            CliOptions.parse();
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Missing required subcommand"));
        }
    }

    @Test
    public void sharedOptionsAreAcceptedBeforeSubcommand() {
        RumbleConfiguration configuration = CliOptions.parse(
            "--debug",
            "--no-native-execution",
            "--default-formatting-place",
            "Europe/Madrid",
            "serve",
            "--host",
            "127.0.0.1",
            "--port",
            "9000"
        );

        assertEquals(ExecutionMode.SERVE, configuration.getMode());
        assertEquals("127.0.0.1", configuration.getServer().getHost());
        assertEquals(9000, configuration.getServer().getPort());
        assertTrue(configuration.getDiagnostics().isDebug());
        assertFalse(configuration.getExecution().isNativeExecution());
        assertEquals(ZoneId.of("Europe/Madrid"), configuration.getFormatting().getDefaultFormattingPlace());
    }

    @Test
    public void sharedOptionsAreAcceptedAfterSubcommand() {
        RumbleConfiguration configuration = CliOptions.parse(
            "repl",
            "--debug",
            "--result-size",
            "12"
        );

        assertEquals(ExecutionMode.REPL, configuration.getMode());
        assertTrue(configuration.getDiagnostics().isDebug());
        assertEquals(12, configuration.getLimits().getResultsSizeCap());
    }

    @Test
    public void positiveFormOfTrueDefaultOptionKeepsItEnabled() {
        RumbleConfiguration defaults = CliOptions.parse("run");
        RumbleConfiguration explicitlyEnabled = CliOptions.parse("run", "--native-execution");
        RumbleConfiguration explicitlyDisabled = CliOptions.parse("run", "--no-native-execution");

        assertTrue(defaults.getExecution().isNativeExecution());
        assertTrue(explicitlyEnabled.getExecution().isNativeExecution());
        assertFalse(explicitlyDisabled.getExecution().isNativeExecution());
        assertEquals("localhost", defaults.getServer().getHost());
        assertEquals(8001, defaults.getServer().getPort());
        assertEquals(InputOptions.DEFAULT_INPUT_FORMAT, defaults.getInput().getInputFormat());
        assertEquals(
            OutputOptions.DEFAULT_NUMBER_OF_OUTPUT_PARTITIONS,
            defaults.getOutput().getNumberOfOutputPartitions()
        );
        assertEquals(ZoneId.of("UTC"), defaults.getFormatting().getDefaultFormattingPlace());
    }

    @Test
    public void runRetainsModeSpecificInputAndOutputOptions() {
        RumbleConfiguration configuration = CliOptions.parse(
            "run",
            "--query",
            "1 + 1",
            "--output-path",
            "result.json"
        );

        assertEquals(ExecutionMode.RUN, configuration.getMode());
        assertEquals("1 + 1", configuration.getInput().getQuery());
        assertEquals("result.json", configuration.getOutput().getOutputPath());
    }
}
