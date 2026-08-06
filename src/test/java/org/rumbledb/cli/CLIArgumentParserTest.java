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

package org.rumbledb.cli;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.bindings.FileBinding;
import org.rumbledb.bindings.InputFormat;
import org.rumbledb.bindings.LexicalBinding;
import org.rumbledb.bindings.StandardInputBinding;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.config.model.RumbleMode;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.CliException;
import org.rumbledb.serialization.SerializationParameters;

import picocli.CommandLine;

public class CLIArgumentParserTest {

    @Test
    public void runUsesTypedConfigurationDefaults() {
        CLIInvocation invocation = CLIArgumentParser.parse("run", "--query", "1");
        RumbleConfiguration configuration = invocation.configuration();
        RumbleConfiguration defaults = RumbleConfiguration.defaultConfiguration();

        Assertions.assertEquals(RumbleMode.RUN, configuration.mode());
        Assertions.assertEquals(defaults.runtime(), configuration.runtime());
        Assertions.assertEquals(defaults.debug(), configuration.debug());
        Assertions.assertEquals(defaults.analysis(), configuration.analysis());
        Assertions.assertEquals(defaults.optimization(), configuration.optimization());
        Assertions.assertEquals(defaults.semantics(), configuration.semantics());
        Assertions.assertEquals(defaults.formatting(), configuration.formatting());
        Assertions.assertEquals("1", configuration.input().query());
        Assertions.assertNull(configuration.input().queryPath());
        Assertions.assertNull(configuration.output().serializationParameters());
        Assertions.assertTrue(invocation.bindings().names().isEmpty());
    }

    @Test
    public void runRequiresAQuerySource() {
        Assertions.assertThrows(
            CommandLine.MissingParameterException.class,
            () -> CLIArgumentParser.parse("run")
        );
    }

    @Test
    public void runMapsConfigurationArguments() {
        CLIInvocation invocation = CLIArgumentParser.parse(
            "run",
            "--query",
            "1 + 1",
            "--result-size",
            "25",
            "--materialization-cap",
            "50",
            "--no-native-sql-predicates",
            "--no-data-frame-execution-mode-detection",
            "--no-parallel-execution",
            "--no-data-frame-execution",
            "--no-native-execution",
            "--apply-updates",
            "--print-iterator-tree",
            "--show-error-info",
            "--debug",
            "--static-typing",
            "--print-inferred-types",
            "--check-return-types-of-builtin-functions",
            "--no-function-inlining",
            "--no-tail-call-optimization",
            "--no-optimize-general-comparison-to-value-comparison",
            "--no-optimize-steps",
            "--optimize-steps-experimental",
            "--no-optimize-parent-pointers",
            "--default-language",
            "xquery31",
            "--xml-version",
            "1.0",
            "--dates-with-timezone",
            "--no-lax-json-null-validation",
            "--static-base-uri",
            "https://example.com/base/",
            "--default-formatting-place",
            "Europe/Madrid",
            "--default-formatting-calendar",
            "ISO",
            "--default-formatting-language",
            "en",
            "--output-path",
            "output.json",
            "--output-format",
            "json",
            "--log-path",
            "execution.log",
            "--overwrite",
            "--number-of-output-partitions",
            "4",
            "--shell-filter",
            "jq .",
            "--output-format-option",
            "indent=yes",
            "--output-format-option",
            "indent-spaces=2",
            "--output-format-option",
            "compression=gzip"
        );
        RumbleConfiguration configuration = invocation.configuration();

        Assertions.assertEquals("1 + 1", configuration.input().query());

        Assertions.assertEquals(25, configuration.runtime().resultsSizeCap());
        Assertions.assertEquals(50, configuration.runtime().materializationCap());
        Assertions.assertFalse(configuration.runtime().useNativeSQLPredicates());
        Assertions.assertFalse(configuration.runtime().detectDataFrameExecutionMode());
        Assertions.assertFalse(configuration.runtime().useParallelExecution());
        Assertions.assertFalse(configuration.runtime().useDataFrameExecution());
        Assertions.assertFalse(configuration.runtime().useNativeExecution());
        Assertions.assertTrue(configuration.runtime().shouldApplyUpdates());

        Assertions.assertTrue(configuration.debug().printIteratorTree());
        Assertions.assertTrue(configuration.debug().showErrorInfo());
        Assertions.assertTrue(configuration.debug().logging());

        Assertions.assertTrue(configuration.analysis().enableStaticTyping());
        Assertions.assertTrue(configuration.analysis().printInferredTypes());
        Assertions.assertTrue(configuration.analysis().checkReturnTypeOfBuiltinFunctions());

        Assertions.assertFalse(configuration.optimization().useFunctionInlining());
        Assertions.assertFalse(configuration.optimization().useTailCallOptimization());
        Assertions.assertFalse(configuration.optimization().optimizeGeneralComparisonToValueComparison());
        Assertions.assertFalse(configuration.optimization().optimizeSteps());
        Assertions.assertTrue(configuration.optimization().optimizeStepsExperimental());
        Assertions.assertFalse(configuration.optimization().optimizeParentPointers());

        Assertions.assertEquals("xquery31", configuration.semantics().queryLanguage());
        Assertions.assertEquals("1.0", configuration.semantics().xmlVersion());
        Assertions.assertTrue(configuration.semantics().datesWithTimeZone());
        Assertions.assertFalse(configuration.semantics().laxJSONNullValidation());
        Assertions.assertEquals("https://example.com/base/", configuration.semantics().staticBaseUri());

        Assertions.assertEquals("Europe/Madrid", configuration.formatting().defaultFormattingPlace());
        Assertions.assertEquals("ISO", configuration.formatting().defaultFormattingCalendar());
        Assertions.assertEquals("en", configuration.formatting().defaultFormattingLanguage());

        Assertions.assertEquals("output.json", configuration.output().outputPath());
        Assertions.assertEquals("json", configuration.output().outputFormat());
        Assertions.assertEquals("execution.log", configuration.output().logPath());
        Assertions.assertTrue(configuration.output().allowOverwrite());
        Assertions.assertEquals(4, configuration.output().numberOfOutputPartitions());
        Assertions.assertEquals("jq .", configuration.output().shellFilter());

        SerializationParameters serializationParameters = configuration.output().serializationParameters();
        Assertions.assertNotNull(serializationParameters);
        Assertions.assertTrue(serializationParameters.getIndent());
        Assertions.assertEquals(2, serializationParameters.getIndentSpaces());
        Assertions.assertEquals("gzip", serializationParameters.getSparkOptions().get("compression"));
    }

    @Test
    public void shortOptionsAndPositionalQueryPathAreSupported() {
        CLIInvocation invocation = CLIArgumentParser.parse(
            "run",
            "-c",
            "75",
            "-t",
            "-v",
            "-o",
            "output.json",
            "-f",
            "json",
            "-O",
            "-P",
            "3",
            "queries/main.jq"
        );
        RumbleConfiguration configuration = invocation.configuration();

        Assertions.assertEquals("queries/main.jq", configuration.input().queryPath());
        Assertions.assertEquals(75, configuration.runtime().materializationCap());
        Assertions.assertTrue(configuration.analysis().enableStaticTyping());
        Assertions.assertTrue(configuration.debug().showErrorInfo());
        Assertions.assertEquals("output.json", configuration.output().outputPath());
        Assertions.assertEquals("json", configuration.output().outputFormat());
        Assertions.assertTrue(configuration.output().allowOverwrite());
        Assertions.assertEquals(3, configuration.output().numberOfOutputPartitions());
    }

    @Test
    public void conflictingQuerySourcesAreRejected() {
        Assertions.assertThrows(
            CommandLine.ParameterException.class,
            () -> CLIArgumentParser.parse(
                "run",
                "--query-path",
                "queries/named.jq",
                "queries/positional.jq"
            )
        );
        Assertions.assertThrows(
            CommandLine.ParameterException.class,
            () -> CLIArgumentParser.parse(
                "run",
                "--query",
                "1 + 1",
                "--query-path",
                "queries/main.jq"
            )
        );
        Assertions.assertThrows(
            CommandLine.ParameterException.class,
            () -> CLIArgumentParser.parse(
                "run",
                "--query",
                "1 + 1",
                "queries/main.jq"
            )
        );
    }

    @Test
    public void replSetsModeAndMapsSharedOptions() {
        CLIInvocation invocation = CLIArgumentParser.parse(
            "repl",
            "--result-size",
            "20",
            "--no-optimize-parent-pointers",
            "--output-format-option",
            "item-separator=|"
        );

        Assertions.assertEquals(RumbleMode.REPL, invocation.configuration().mode());
        Assertions.assertEquals(20, invocation.configuration().runtime().resultsSizeCap());
        Assertions.assertFalse(invocation.configuration().optimization().optimizeParentPointers());
        Assertions.assertEquals(
            "|",
            invocation.configuration().output().serializationParameters().getItemSeparator()
        );
    }

    @Test
    public void bindingsRemainSeparateFromConfiguration() {
        CLIInvocation invocation = CLIArgumentParser.parse(
            "run",
            "--query",
            "1",
            "--variable",
            "answer=42",
            "--variable-from-file",
            "payload=data.json",
            "--context-item-input",
            "-",
            "--context-item-input-format",
            "text"
        );
        ExternalBindings bindings = invocation.bindings();

        LexicalBinding answer = bindings
            .get(Name.createVariableInNoNamespace("answer"), LexicalBinding.class)
            .orElseThrow();
        FileBinding payload = bindings
            .get(Name.createVariableInNoNamespace("payload"), FileBinding.class)
            .orElseThrow();
        StandardInputBinding contextItem = bindings
            .get(Name.CONTEXT_ITEM, StandardInputBinding.class)
            .orElseThrow();

        Assertions.assertEquals("42", answer.getValue());
        Assertions.assertEquals("data.json", payload.getLocation());
        Assertions.assertEquals(InputFormat.JSON, payload.getFormat());
        Assertions.assertEquals(InputFormat.TEXT, contextItem.getFormat());
    }

    @Test
    public void literalContextItemIsSupported() {
        CLIInvocation invocation = CLIArgumentParser.parse(
            "run",
            "--query",
            "1",
            "--context-item",
            "{\"a\":1}"
        );

        LexicalBinding contextItem = invocation.bindings()
            .get(Name.CONTEXT_ITEM, LexicalBinding.class)
            .orElseThrow();
        Assertions.assertEquals("{\"a\":1}", contextItem.getValue());
    }

    @Test
    public void duplicateAndMutuallyExclusiveBindingsAreRejected() {
        Assertions.assertThrows(
            CliException.class,
            () -> CLIArgumentParser.parse(
                "run",
                "--query",
                "1",
                "--variable",
                "value=1",
                "--variable-from-file",
                "value=data.json"
            )
        );
        Assertions.assertThrows(
            CommandLine.ParameterException.class,
            () -> CLIArgumentParser.parse(
                "run",
                "--query",
                "1",
                "--context-item",
                "1",
                "--context-item-input",
                "data.json"
            )
        );
    }

    @Test
    public void invalidTypedValuesAreRejected() {
        Assertions.assertThrows(
            CliException.class,
            () -> CLIArgumentParser.parse("run", "--query", "1", "--xml-version", "2.0")
        );
        Assertions.assertThrows(
            CliException.class,
            () -> CLIArgumentParser.parse(
                "run",
                "--query",
                "1",
                "--default-formatting-place",
                "Not/AZone"
            )
        );
        Assertions.assertThrows(
            CliException.class,
            () -> CLIArgumentParser.parse(
                "run",
                "--query",
                "1",
                "--context-item-input",
                "-",
                "--context-item-input-format",
                "yaml"
            )
        );
    }
}
