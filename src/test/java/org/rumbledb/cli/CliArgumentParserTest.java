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
import org.rumbledb.context.Name;
import org.rumbledb.config.ExecutionMode;
import org.rumbledb.config.OutputOptions;
import org.rumbledb.config.RumbleConfiguration;

import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CliArgumentParserTest {

    @Test
    public void noSubcommandShouldGiveError() {
        try {
            CLIArgumentParser.parse();
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Missing required subcommand"));
        }
    }

    @Test
    public void sharedOptionsAreAcceptedBeforeSubcommand() {
        RumbleConfiguration configuration = CLIArgumentParser.parse(

            "serve",
            "--host",
            "127.0.0.1",
            "--port",
            "9000",
            "--debug",
            "--no-native-execution",
            "--default-formatting-place",
            "Europe/Madrid"
        );

        assertEquals(ExecutionMode.SERVE, configuration.executionMode());
        assertEquals("127.0.0.1", configuration.server().host());
        assertEquals(9000, configuration.server().port());
        assertTrue(configuration.debug().logging());
        assertFalse(configuration.runtime().useNativeExecution());
        assertEquals(ZoneId.of("Europe/Madrid"), configuration.formatting().defaultFormattingPlace());
    }

    @Test
    public void sharedOptionsAreAcceptedAfterSubcommand() {
        RumbleConfiguration configuration = CLIArgumentParser.parse(
            "repl",
            "--debug",
            "--result-size",
            "12"
        );

        assertEquals(ExecutionMode.REPL, configuration.executionMode());
        assertTrue(configuration.debug().logging());
        assertEquals(12, configuration.runtime().resultsSizeCap());
    }

    @Test
    public void positiveFormOfTrueDefaultOptionKeepsItEnabled() {
        RumbleConfiguration defaults = CLIArgumentParser.parse("run");
        RumbleConfiguration explicitlyEnabled = CLIArgumentParser.parse("run", "--native-execution");
        RumbleConfiguration explicitlyDisabled = CLIArgumentParser.parse("run", "--no-native-execution");

        assertTrue(defaults.runtime().useNativeExecution());
        assertTrue(explicitlyEnabled.runtime().useNativeExecution());
        assertFalse(explicitlyDisabled.runtime().useNativeExecution());
        assertEquals("localhost", defaults.server().host());
        assertEquals(8001, defaults.server().port());
        assertEquals(null, defaults.input().query());
        assertEquals(null, defaults.input().queryPath());
        assertEquals(
            OutputOptions.DEFAULT_NUMBER_OF_OUTPUT_PARTITIONS,
            defaults.output().numberOfOutputPartitions()
        );
        assertEquals(ZoneId.of("UTC"), defaults.formatting().defaultFormattingPlace());
    }

    @Test
    public void runRetainsModeSpecificInputAndOutputOptions() {
        RumbleConfiguration configuration = CLIArgumentParser.parse(
            "run",
            "--query",
            "1 + 1",
            "--output-path",
            "result.json"
        );

        assertEquals(ExecutionMode.RUN, configuration.executionMode());
        assertEquals("1 + 1", configuration.input().query());
        assertEquals("result.json", configuration.output().outputPath());
    }

    @Test
    public void contextItemAndContextItemInputAreMutuallyExclusive() {
        assertThrows(
            Exception.class,
            () -> CLIArgumentParser.parse(
                "run",
                "--context-item",
                "1",
                "--context-item-input",
                "-"
            )
        );
    }

    @Test
    public void contextItemInputDefaultsToJsonFormat() {
        RumbleConfiguration configuration = CLIArgumentParser.parse(
            "run",
            "--context-item-input",
            "-"
        );

        assertEquals(
            "json",
            configuration.bindings().getInputFormat(Name.CONTEXT_ITEM)
        );
    }

    @Test
    public void invalidXmlVersionIsRejected() {
        try {
            CLIArgumentParser.parse(
                "run",
                "--xml-version",
                "1.2"
            );
            fail("Expected invalid xml version to be rejected.");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("--xml-version"));
        }
    }

    @Test
    public void formattingCalendarAndLanguageAreNormalized() {
        RumbleConfiguration configuration = CLIArgumentParser.parse(
            "run",
            "--default-formatting-calendar",
            "default",
            "--default-formatting-language",
            "EN_us"
        );

        assertEquals("ISO", configuration.formatting().defaultFormattingCalendar());
        assertEquals("en", configuration.formatting().defaultFormattingLanguage());
    }

    @Test
    public void invalidFormattingCalendarIsRejected() {
        try {
            CLIArgumentParser.parse(
                "run",
                "--default-formatting-calendar",
                "julian"
            );
            fail("Expected invalid formatting calendar to be rejected.");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("default-formatting-calendar"));
            assertTrue(e.getMessage().contains("julian"));
        }
    }

    @Test
    public void invalidFormattingLanguageIsRejected() {
        try {
            CLIArgumentParser.parse(
                "run",
                "--default-formatting-language",
                "fr"
            );
            fail("Expected invalid formatting language to be rejected.");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("default-formatting-language"));
            assertTrue(e.getMessage().contains("fr"));
        }
    }
}
