/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.cli;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import picocli.CommandLine;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.bindings.FileBinding;
import org.rumbledb.bindings.InputFormat;
import org.rumbledb.bindings.LexicalBinding;
import org.rumbledb.bindings.StandardInputBinding;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.config.RumbleConfigurationResolver;
import org.rumbledb.config.model.RumbleMode;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.CliException;
import org.rumbledb.serialization.SerializationParameters;

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
        Assertions.assertThrows(CommandLine.MissingParameterException.class, () -> CLIArgumentParser.parse("run"));
    }

    // Small named tables make the documented CLI-to-API mappings reviewable.
    // Every boolean is exercised in both directions in both supported commands.
    @ParameterizedTest(name = "runtime: --{0} maps to {1}")
    @MethodSource("runtimeBooleans")
    void runtimeBooleanOptions(String option, String key) {
        assertBooleanOption(option, key);
    }

    static Stream<String[]> runtimeBooleans() {
        return Stream.of(
                new String[] {"native-sql-predicates", "runtime.useNativeSQLPredicates"},
                new String[] {"data-frame-execution-mode-detection", "runtime.detectDataFrameExecutionMode"},
                new String[] {"parallel-execution", "runtime.useParallelExecution"},
                new String[] {"data-frame-execution", "runtime.useDataFrameExecution"},
                new String[] {"native-execution", "runtime.useNativeExecution"},
                new String[] {"apply-updates", "runtime.shouldApplyUpdates"});
    }

    @ParameterizedTest(name = "analysis: --{0} maps to {1}")
    @MethodSource("analysisBooleans")
    void analysisBooleanOptions(String option, String key) {
        assertBooleanOption(option, key);
    }

    static Stream<String[]> analysisBooleans() {
        return Stream.of(
                new String[] {"static-typing", "analysis.enableStaticTyping"},
                new String[] {"print-inferred-types", "analysis.printInferredTypes"},
                new String[] {"check-return-types-of-builtin-functions", "analysis.checkReturnTypeOfBuiltinFunctions"});
    }

    @ParameterizedTest(name = "optimization: --{0} maps to {1}")
    @MethodSource("optimizationBooleans")
    void optimizationBooleanOptions(String option, String key) {
        assertBooleanOption(option, key);
    }

    static Stream<String[]> optimizationBooleans() {
        return Stream.of(
                new String[] {"function-inlining", "optimization.useFunctionInlining"},
                new String[] {"tail-call-optimization", "optimization.useTailCallOptimization"},
                new String[] {
                    "optimize-general-comparison-to-value-comparison",
                    "optimization.optimizeGeneralComparisonToValueComparison"
                },
                new String[] {"optimize-steps", "optimization.optimizeSteps"},
                new String[] {"optimize-steps-experimental", "optimization.optimizeStepsExperimental"},
                new String[] {"optimize-parent-pointers", "optimization.optimizeParentPointers"});
    }

    @ParameterizedTest(name = "diagnostics/output: --{0} maps to {1}")
    @MethodSource("diagnosticAndOutputBooleans")
    void diagnosticAndOutputBooleanOptions(String option, String key) {
        assertBooleanOption(option, key);
    }

    static Stream<String[]> diagnosticAndOutputBooleans() {
        return Stream.of(
                new String[] {"print-iterator-tree", "debug.printIteratorTree"},
                new String[] {"show-error-info", "debug.showErrorInfo"},
                new String[] {"debug", "debug.logging"},
                new String[] {"overwrite", "output.allowOverwrite"});
    }

    @ParameterizedTest(name = "semantics: --{0} maps to {1}")
    @MethodSource("semanticsBooleans")
    void semanticsBooleanOptions(String option, String key) {
        assertBooleanOption(option, key);
    }

    static Stream<String[]> semanticsBooleans() {
        return Stream.of(
                new String[] {"dates-with-timezone", "semantics.datesWithTimeZone"},
                new String[] {"lax-json-null-validation", "semantics.laxJSONNullValidation"},
                new String[] {"lax-json-null-valication", "semantics.laxJSONNullValidation"});
    }

    private static void assertBooleanOption(String option, String key) {
        for (String command : List.of("run", "repl")) {
            for (boolean enabled : List.of(true, false)) {
                String flag = (enabled ? "--" : "--no-") + option;
                CLIInvocation invocation = command.equals("run")
                        ? CLIArgumentParser.parse(command, "-q", "1", flag)
                        : CLIArgumentParser.parse(command, flag);
                Assertions.assertEquals(
                        enabled,
                        RumbleConfigurationResolver.get(invocation.configuration(), key),
                        command + " " + flag + " should set " + key);
            }
        }
    }

    @ParameterizedTest(name = "--{0} maps to {1} = {3}")
    @MethodSource("valueOptions")
    void valueOptionsMapToDocumentedPaths(String option, String key, String value, Object expected) {
        for (String command : List.of("run", "repl")) {
            CLIInvocation invocation = command.equals("run")
                    ? CLIArgumentParser.parse(command, "-q", "1", "--" + option, value)
                    : CLIArgumentParser.parse(command, "--" + option, value);
            Assertions.assertEquals(expected, RumbleConfigurationResolver.get(invocation.configuration(), key));
        }
    }

    static Stream<Object[]> valueOptions() {
        return Stream.of(
                new Object[] {"result-size", "runtime.resultsSizeCap", "25", 25},
                new Object[] {"materialization-cap", "runtime.materializationCap", "50", 50},
                new Object[] {"output-path", "output.outputPath", "output with spaces", "output with spaces"},
                new Object[] {"output-format", "output.outputFormat", "parquet", "parquet"},
                new Object[] {"number-of-output-partitions", "output.numberOfOutputPartitions", "4", 4},
                new Object[] {"log-path", "output.logPath", "execution.log", "execution.log"},
                new Object[] {"shell-filter", "output.shellFilter", "tr a b", "tr a b"},
                new Object[] {"log-level", "debug.logLevel", "trace", "trace"},
                new Object[] {"spark-log-level", "debug.sparkLogLevel", "warn", "warn"},
                new Object[] {"default-language", "semantics.queryLanguage", "xquery31", "xquery31"},
                new Object[] {"xml-version", "semantics.xmlVersion", "1.0", "1.0"},
                new Object[] {
                    "static-base-uri",
                    "semantics.staticBaseUri",
                    "https://example.com/base/",
                    "https://example.com/base/"
                },
                new Object[] {
                    "default-formatting-place", "formatting.defaultFormattingPlace", "Europe/Zurich", "Europe/Zurich"
                },
                new Object[] {"default-formatting-calendar", "formatting.defaultFormattingCalendar", "ISO", "ISO"},
                new Object[] {"default-formatting-language", "formatting.defaultFormattingLanguage", "de", "de"});
    }

    @Test
    void outputFormatAndSerializationMethodRemainIndependent() {
        CLIInvocation invocation = CLIArgumentParser.parse(
                "run",
                "-q",
                "1",
                "--output-format",
                "csv",
                "--output-format-option",
                "method=xml",
                "--output-format-option",
                "indent=yes",
                "--output-format-option",
                "indent-spaces=4",
                "--output-format-option",
                "compression=gzip");
        SerializationParameters parameters = invocation.configuration().output().serializationParameters();
        Assertions.assertEquals("csv", invocation.configuration().output().outputFormat());
        Assertions.assertEquals("xml", parameters.getMethod());
        Assertions.assertTrue(parameters.getIndent());
        Assertions.assertEquals(4, parameters.getIndentSpaces());
        Assertions.assertEquals("gzip", parameters.getSparkOptions().get("compression"));
    }

    @Test
    void serializationOptionsPreserveTheDefaultQueryLanguageMethod() {
        CLIInvocation invocation = CLIArgumentParser.parse(
                "run",
                "-q",
                "1",
                "--default-language",
                "xquery31",
                "--output-format",
                "serialize",
                "--output-format-option",
                "indent=yes");
        Assertions.assertEquals(
                "xml",
                invocation.configuration().output().serializationParameters().getMethod());
    }

    @Test
    public void shortOptionsAndPositionalQueryPathAreSupported() {
        CLIInvocation invocation = CLIArgumentParser.parse(
                "run", "-c", "75", "-t", "-v", "-o", "output.json", "-f", "json", "-O", "-P", "3", "queries/main.jq");
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
                () -> CLIArgumentParser.parse("run", "--query-path", "queries/named.jq", "queries/positional.jq"));
        Assertions.assertThrows(
                CommandLine.ParameterException.class,
                () -> CLIArgumentParser.parse("run", "--query", "1 + 1", "--query-path", "queries/main.jq"));
        Assertions.assertThrows(
                CommandLine.ParameterException.class,
                () -> CLIArgumentParser.parse("run", "--query", "1 + 1", "queries/main.jq"));
    }

    @Test
    public void replSetsModeAndMapsSharedOptions() {
        CLIInvocation invocation = CLIArgumentParser.parse(
                "repl",
                "--result-size",
                "20",
                "--no-optimize-parent-pointers",
                "--output-format-option",
                "item-separator=|");

        Assertions.assertEquals(RumbleMode.REPL, invocation.configuration().mode());
        Assertions.assertEquals(20, invocation.configuration().runtime().resultsSizeCap());
        Assertions.assertFalse(invocation.configuration().optimization().optimizeParentPointers());
        Assertions.assertEquals(
                "|",
                invocation.configuration().output().serializationParameters().getItemSeparator());
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
                "text");
        ExternalBindings bindings = invocation.bindings();

        LexicalBinding answer = bindings.get(Name.createVariableInNoNamespace("answer"), LexicalBinding.class)
                .orElseThrow();
        FileBinding payload = bindings.get(Name.createVariableInNoNamespace("payload"), FileBinding.class)
                .orElseThrow();
        StandardInputBinding contextItem =
                bindings.get(Name.CONTEXT_ITEM, StandardInputBinding.class).orElseThrow();

        Assertions.assertEquals("42", answer.getValue());
        Assertions.assertEquals("data.json", payload.getLocation());
        Assertions.assertEquals(InputFormat.JSON, payload.getFormat());
        Assertions.assertEquals(InputFormat.TEXT, contextItem.getFormat());
    }

    @Test
    public void literalContextItemIsSupported() {
        CLIInvocation invocation = CLIArgumentParser.parse("run", "--query", "1", "--context-item", "{\"a\":1}");

        LexicalBinding contextItem = invocation
                .bindings()
                .get(Name.CONTEXT_ITEM, LexicalBinding.class)
                .orElseThrow();
        Assertions.assertEquals("{\"a\":1}", contextItem.getValue());
    }

    @Test
    public void duplicateAndMutuallyExclusiveBindingsAreRejected() {
        Assertions.assertThrows(
                CliException.class,
                () -> CLIArgumentParser.parse(
                        "run", "--query", "1", "--variable", "value=1", "--variable-from-file", "value=data.json"));
        Assertions.assertThrows(
                CommandLine.ParameterException.class,
                () -> CLIArgumentParser.parse(
                        "run", "--query", "1", "--context-item", "1", "--context-item-input", "data.json"));
    }

    @Test
    public void invalidTypedValuesAreRejected() {
        Assertions.assertThrows(
                CliException.class, () -> CLIArgumentParser.parse("run", "--query", "1", "--xml-version", "2.0"));
        Assertions.assertThrows(
                CliException.class,
                () -> CLIArgumentParser.parse("run", "--query", "1", "--default-formatting-place", "Not/AZone"));
        Assertions.assertThrows(
                CliException.class,
                () -> CLIArgumentParser.parse(
                        "run", "--query", "1", "--context-item-input", "-", "--context-item-input-format", "yaml"));
    }
}
