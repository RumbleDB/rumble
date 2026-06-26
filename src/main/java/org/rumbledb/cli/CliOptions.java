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

import org.rumbledb.config.AnalysisOptions;
import org.rumbledb.config.DiagnosticsOptions;
import org.rumbledb.config.ExecutionMode;
import org.rumbledb.config.ExecutionOptions;
import org.rumbledb.config.ExternalVariableBindings;
import org.rumbledb.config.FormattingOptions;
import org.rumbledb.config.IOOptions;
import org.rumbledb.config.LanguageOptions;
import org.rumbledb.config.OptimizationOptions;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.config.RuntimeLimits;
import org.rumbledb.config.ServerOptions;
import org.rumbledb.context.Name;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@Command(
    name = "rumbledb",
    description = "RumbleDB command line interface.",
    mixinStandardHelpOptions = false,
    subcommands = {
        CliOptions.Run.class,
        CliOptions.Serve.class,
        CliOptions.Repl.class
    }
)
public final class CliOptions {
    private interface ConfigurationSource {
        RumbleConfiguration toRumbleConfiguration(CliOptions root);
    }

    private static CommandLine commandLine() {
        return new picocli.CommandLine(new CliOptions());
    }

    public static CommandLine.ParseResult parseArgs(String... args) {
        return commandLine().parseArgs(normalizeLegacyDynamicOptions(args));
    }

    public static RumbleConfiguration toRumbleConfiguration(String... args) {
        return toRumbleConfiguration(parseArgs(args));
    }

    public static RumbleConfiguration toRumbleConfiguration(CommandLine.ParseResult parseResult) {
        CliOptions root = (CliOptions) parseResult.commandSpec().userObject();
        CommandLine.ParseResult current = parseResult;
        while (current.subcommand() != null) {
            current = current.subcommand();
        }
        Object activeCommand = current.commandSpec().userObject();
        if (activeCommand instanceof ConfigurationSource source) {
            return source.toRumbleConfiguration(root);
        }
        return root.toRumbleConfiguration();
    }

    private RumbleConfiguration toRumbleConfiguration() {
        Input effectiveInput = this.input;
        Output effectiveOutput = this.output;
        String effectiveQueryPath = this.queryPath;
        if (effectiveInput != null && effectiveInput.queryPath != null) {
            effectiveQueryPath = effectiveInput.queryPath;
        }
        return buildConfiguration(
            ExecutionMode.RUN,
            this.server,
            effectiveInput,
            effectiveOutput,
            this.limits,
            this.diagnostics,
            this.execution,
            this.optimization,
            this.language,
            this.formatting,
            this.dynamicOptions,
            effectiveQueryPath
        );
    }

    private static RumbleConfiguration buildConfiguration(
            ExecutionMode mode,
            Server server,
            Input input,
            Output output,
            Limits limits,
            Diagnostics diagnostics,
            Execution execution,
            Optimization optimization,
            Language language,
            Formatting formatting,
            DynamicOptions dynamicOptions,
            String queryPath
    ) {
        Map<Name, String> unparsedExternalVariableValues = new HashMap<>();
        if (dynamicOptions.variables != null) {
            dynamicOptions.variables.forEach(
                (name, value) -> unparsedExternalVariableValues.put(Name.createVariableInNoNamespace(name), value)
            );
        }
        if (input != null && input.contextItem != null) {
            unparsedExternalVariableValues.put(Name.CONTEXT_ITEM, input.contextItem);
        }

        Map<Name, String> externalVariableValuesReadFromFiles = new HashMap<>();
        if (dynamicOptions.variablesFromFiles != null) {
            dynamicOptions.variablesFromFiles.forEach(
                (name, value) -> externalVariableValuesReadFromFiles.put(Name.createVariableInNoNamespace(name), value)
            );
        }

        Set<Name> externalVariablesReadFromStandardInput = new HashSet<>();
        if (input != null && input.contextItemInput != null) {
            if ("-".equals(input.contextItemInput)) {
                externalVariablesReadFromStandardInput.add(Name.CONTEXT_ITEM);
            } else {
                externalVariableValuesReadFromFiles.put(Name.CONTEXT_ITEM, input.contextItemInput);
            }
        }

        Map<Name, String> externalVariablesInputFormats = new HashMap<>();
        if (input != null && input.contextItemInputFormat != null) {
            externalVariablesInputFormats.put(Name.CONTEXT_ITEM, input.contextItemInputFormat);
        }

        FormattingOptions.FormattingOptionsBuilder formattingBuilder = FormattingOptions.builder();
        applyIfPresent(
            formatting.defaultFormattingPlace,
            value -> formattingBuilder.defaultFormattingPlace(ZoneId.of(value))
        );
        applyIfPresent(formatting.defaultFormattingCalendar, formattingBuilder::defaultFormattingCalendar);
        applyIfPresent(formatting.defaultFormattingLanguage, formattingBuilder::defaultFormattingLanguage);

        LanguageOptions.LanguageOptionsBuilder languageBuilder = LanguageOptions.builder()
            .datesWithTimeZone(language.datesWithTimezone)
            .laxJSONNullValidation(language.laxJSONNullValidation);
        applyIfPresent(language.xmlVersion, languageBuilder::xmlVersion);

        ServerOptions.ServerOptionsBuilder serverBuilder = ServerOptions.builder().mode(mode);
        applyIfPresent(server.host, serverBuilder::host);
        applyIfPresent(server.port, serverBuilder::port);

        IOOptions.IOOptionsBuilder ioBuilder = IOOptions.builder();
        applyIfPresent(input == null ? null : input.inputFormat, ioBuilder::inputFormat);
        applyIfPresent(output == null ? null : output.numberOfOutputPartitions, ioBuilder::numberOfOutputPartitions);
        applyIfPresent(queryPath, ioBuilder::queryPath);
        applyIfPresent(output == null ? null : output.outputPath, ioBuilder::outputPath);
        applyIfPresent(output == null ? null : output.logPath, ioBuilder::logPath);
        applyIfPresent(input == null ? null : input.query, ioBuilder::query);
        applyIfPresent(output == null ? null : output.shellFilter, ioBuilder::shell);

        RuntimeLimits.RuntimeLimitsBuilder runtimeLimitsBuilder = RuntimeLimits.builder();
        applyIfPresent(
            limits.resultSize == null ? null : limits.resultSize.intValue(),
            runtimeLimitsBuilder::resultsSizeCap
        );
        applyIfPresent(limits.materializationCap, runtimeLimitsBuilder::materializationCap);

        ExternalVariableBindings.ExternalVariableBindingsBuilder externalVariableBindingsBuilder =
            ExternalVariableBindings.builder();
        applyIfNotEmpty(
            unparsedExternalVariableValues,
            externalVariableBindingsBuilder::unparsedExternalVariableValues
        );
        applyIfNotEmpty(
            externalVariableValuesReadFromFiles,
            externalVariableBindingsBuilder::externalVariableValuesReadFromFiles
        );
        applyIfNotEmpty(
            externalVariablesReadFromStandardInput,
            externalVariableBindingsBuilder::externalVariablesReadFromStandardInput
        );
        applyIfNotEmpty(
            externalVariablesInputFormats,
            externalVariableBindingsBuilder::externalVariablesInputFormats
        );

        applyIfPresent(language.defaultLanguage, languageBuilder::queryLanguage);
        applyIfPresent(language.staticBaseUri, languageBuilder::staticBaseUri);

        return RumbleConfiguration.builder()
            .server(serverBuilder.build())
            .io(ioBuilder.build())
            .limits(runtimeLimitsBuilder.build())
            .diagnostics(
                DiagnosticsOptions.builder()
                    .printIteratorTree(diagnostics.printIteratorTree)
                    .showErrorInfo(diagnostics.showErrorInfo)
                    .checkReturnTypeOfBuiltinFunctions(diagnostics.checkReturnTypesOfBuiltinFunctions)
                    .debug(diagnostics.debug)
                    .build()
            )
            .analysis(
                AnalysisOptions.builder()
                    .staticTyping(diagnostics.staticTyping)
                    .printInferredTypes(diagnostics.printInferredTypes)
                    .build()
            )
            .execution(
                ExecutionOptions.builder()
                    .nativeSQLPredicates(execution.nativeSQLPredicates)
                    .dataFrameExecutionModeDetection(execution.dataFrameExecutionModeDetection)
                    .parallelExecution(execution.parallelExecution)
                    .dataFrameExecution(execution.dataFrameExecution)
                    .nativeExecution(execution.nativeExecution)
                    .tailCallOptimization(execution.tailCallOptimization)
                    .functionInlining(execution.functionInlining)
                    .applyUpdates(execution.applyUpdates)
                    .build()
            )
            .optimization(
                OptimizationOptions.builder()
                    .optimizeGeneralComparisonToValueComparison(optimization.optimizeGeneralComparisonToValueComparison)
                    .optimizeSteps(optimization.optimizeSteps)
                    .optimizeStepsExperimental(optimization.optimizeStepsExperimental)
                    .optimizeParentPointers(optimization.optimizeParentPointers)
                    .build()
            )
            .language(languageBuilder.build())
            .formatting(formattingBuilder.build())
            .externalVariableBindings(externalVariableBindingsBuilder.build())
            .build();
    }

    private static String[] normalizeLegacyDynamicOptions(String[] args) {
        List<String> normalizedArguments = new ArrayList<>(args.length);
        for (int i = 0; i < args.length; i++) {
            String argument = args[i];
            String normalizedOptionName = getNormalizedDynamicOptionName(argument);
            if (normalizedOptionName == null) {
                normalizedArguments.add(argument);
                continue;
            }
            if (i + 1 >= args.length) {
                normalizedArguments.add(argument);
                continue;
            }
            String value = args[++i];
            normalizedArguments.add(normalizedOptionName);
            normalizedArguments.add(getDynamicOptionSuffix(argument) + "=" + value);
        }
        return normalizedArguments.toArray(new String[0]);
    }

    private static String getNormalizedDynamicOptionName(String argument) {
        if (argument.startsWith("--variable-from-file:")) {
            return "--variable-from-file";
        }
        if (argument.startsWith("--variable:")) {
            return "--variable";
        }
        if (argument.startsWith("--output-format-option:")) {
            return "--output-format-option";
        }
        return null;
    }

    private static String getDynamicOptionSuffix(String argument) {
        return argument.substring(argument.indexOf(':') + 1);
    }

    private static <T> void applyIfPresent(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private static <T, C extends Collection<T>> void applyIfNotEmpty(C value, Consumer<C> setter) {
        if (value != null && !value.isEmpty()) {
            setter.accept(value);
        }
    }

    private static <K, V> void applyIfNotEmpty(Map<K, V> value, Consumer<Map<K, V>> setter) {
        if (value != null && !value.isEmpty()) {
            setter.accept(value);
        }
    }

    @Mixin
    private Server server;

    @Mixin
    private Input input;

    @Mixin
    private Output output;

    @Mixin
    private Limits limits;

    @Mixin
    private Diagnostics diagnostics;

    @Mixin
    private Execution execution;

    @Mixin
    private Optimization optimization;

    @Mixin
    private Language language;

    @Mixin
    private Formatting formatting;

    @Mixin
    private DynamicOptions dynamicOptions;

    @Parameters(
        index = "0",
        arity = "0..1",
        paramLabel = "query-file",
        description = "A JSONiq query file to read from (from any file system, even the Web!)."
    )
    private String queryPath;

    @Command(name = "run", description = "Executes a query.", mixinStandardHelpOptions = false)
    public static final class Run implements ConfigurationSource {
        @Mixin
        private Input input;

        @Mixin
        private Output output;

        @Mixin
        private Limits limits;

        @Mixin
        private Diagnostics diagnostics;

        @Mixin
        private Execution execution;

        @Mixin
        private Optimization optimization;

        @Mixin
        private Language language;

        @Mixin
        private Formatting formatting;

        @Mixin
        private DynamicOptions dynamicOptions;

        @Parameters(
            index = "0",
            arity = "0..1",
            paramLabel = "query-file",
            description = "A JSONiq query file to read from (from any file system, even the Web!)."
        )
        private String queryPath;

        @Override
        public RumbleConfiguration toRumbleConfiguration(CliOptions root) {
            String effectiveQueryPath = this.input.queryPath != null ? this.input.queryPath : this.queryPath;
            return buildConfiguration(
                ExecutionMode.RUN,
                root.server,
                this.input,
                this.output,
                this.limits,
                this.diagnostics,
                this.execution,
                this.optimization,
                this.language,
                this.formatting,
                this.dynamicOptions,
                effectiveQueryPath
            );
        }
    }

    @Command(name = "serve", description = "Runs RumbleDB as a server on port 8001.", mixinStandardHelpOptions = false)
    public static final class Serve implements ConfigurationSource {
        @Mixin
        private Server server;

        @Mixin
        private Limits limits;

        @Mixin
        private Diagnostics diagnostics;

        @Mixin
        private Execution execution;

        @Mixin
        private Optimization optimization;

        @Mixin
        private Language language;

        @Mixin
        private Formatting formatting;

        @Mixin
        private DynamicOptions dynamicOptions;

        @Override
        public RumbleConfiguration toRumbleConfiguration(CliOptions root) {
            return buildConfiguration(
                ExecutionMode.SERVE,
                this.server,
                null,
                null,
                this.limits,
                this.diagnostics,
                this.execution,
                this.optimization,
                this.language,
                this.formatting,
                this.dynamicOptions,
                null
            );
        }
    }

    @Command(name = "repl", description = "Runs the interactive shell.", mixinStandardHelpOptions = false)
    public static final class Repl implements ConfigurationSource {
        @Mixin
        private Output output;

        @Mixin
        private Limits limits;

        @Mixin
        private Diagnostics diagnostics;

        @Mixin
        private Execution execution;

        @Mixin
        private Optimization optimization;

        @Mixin
        private Language language;

        @Mixin
        private Formatting formatting;

        @Mixin
        private DynamicOptions dynamicOptions;

        @Override
        public RumbleConfiguration toRumbleConfiguration(CliOptions root) {
            return buildConfiguration(
                ExecutionMode.REPL,
                root.server,
                null,
                this.output,
                this.limits,
                this.diagnostics,
                this.execution,
                this.optimization,
                this.language,
                this.formatting,
                this.dynamicOptions,
                null
            );
        }
    }

    public static final class Server {
        @Option(
            names = { "-h", "--host" },
            paramLabel = "host",
            description = "Changes the host of the RumbleDB HTTP server to any of your liking."
        )
        private String host;

        @Option(
            names = { "-p", "--port" },
            paramLabel = "port",
            description = "Changes the port of the RumbleDB HTTP server to any of your liking."
        )
        private Integer port;
    }

    public static final class Input {
        @Option(
            names = { "-q", "--query" },
            paramLabel = "query",
            description = "A JSONiq query directly provided as a string."
        )
        private String query;

        @Option(
            names = "--query-path",
            paramLabel = "path",
            description = "A JSONiq query file to read from (from any file system, even the Web!)."
        )
        private String queryPath;

        @Option(names = "--input-format", paramLabel = "format")
        private String inputFormat;

        @Option(
            names = { "-I", "--context-item" },
            paramLabel = "value",
            description = {
                "Initializes the global context item $$ to the supplied value.",
                "The query must contain the corresponding global variable declaration, e.g. "
                    + "\"declare context item external;\""
            }
        )
        private String contextItem;

        @Option(
            names = { "-i", "--context-item-input" },
            paramLabel = "path",
            description = "Reads the context item value from the standard input."
        )
        private String contextItemInput;

        @Option(
            names = "--context-item-input-format",
            paramLabel = "format",
            description = "Sets the input format to use for parsing the standard input (as text or as a serialized json value)."
        )
        private String contextItemInputFormat;
    }

    public static final class Output {
        @Option(
            names = { "-o", "--output-path" },
            paramLabel = "path",
            description = {
                "Where to output to.",
                "If the output is large, it will create a sharded directory, otherwise it will create a file."
            }
        )
        private String outputPath;

        @Option(
            names = { "-f", "--output-format" },
            paramLabel = "format",
            description = {
                "An output format to use for the output.",
                "Formats other than json can only be output if the query outputs a highly structured sequence of objects."
            }
        )
        private String outputFormat;

        @Option(names = "--log-path", paramLabel = "path", description = "Where to output log information.")
        private String logPath;

        @Option(
            names = { "-O", "--overwrite" },
            negatable = true,
            defaultValue = "false",
            description = "Whether to overwrite to --output-path. No throws an error if the output file/folder exists."
        )
        private boolean overwrite;

        @Option(
            names = { "-P", "--number-of-output-partitions" },
            paramLabel = "count",
            description = "How many partitions to create in the output, i.e., the number of files that will be created in the output path directory."
        )
        private Integer numberOfOutputPartitions;

        @Option(
            names = "--shell-filter",
            paramLabel = "command",
            description = "Post-processes the output of JSONiq queries on the shell with the specified command."
        )
        private String shellFilter;

        @Option(names = "-o:", paramLabel = "name=value")
        private Map<String, String> shortSerializationParameters;
    }

    public static final class DynamicOptions {
        @Option(
            names = "--variable",
            paramLabel = "name=value",
            description = {
                "Initializes a global variable to the supplied value.",
                "The query must contain the corresponding global variable declaration, e.g. "
                    + "\"declare variable $foo external;\""
            }
        )
        private Map<String, String> variables;

        @Option(
            names = "--variable-from-file",
            paramLabel = "name=path",
            description = "Initializes a global variable with a value read from the supplied file."
        )
        private Map<String, String> variablesFromFiles;

        @Option(
            names = "--output-format-option",
            paramLabel = "name=value",
            description = "Options to further specify the output format, for example a separator character for CSV or a compression format."
        )
        private Map<String, String> outputFormatOptions;
    }

    public static final class Limits {
        @Option(
            names = "--result-size",
            paramLabel = "count",
            description = "A cap on the maximum number of items to output on the screen or to a local list."
        )
        private Integer resultSize;

        @Option(
            names = { "-c", "--materialization-cap" },
            paramLabel = "count",
            description = "A cap on the maximum number of items to materialize during the query execution for large sequences within a query."
        )
        private Integer materializationCap;
    }

    public static final class Diagnostics {
        @Option(
            names = "--print-iterator-tree",
            negatable = true,
            defaultValue = "false",
            description = "For debugging purposes, prints out the expression tree and runtime interator tree."
        )
        private boolean printIteratorTree;

        @Option(
            names = { "-v", "--show-error-info" },
            negatable = true,
            defaultValue = "false",
            description = {
                "For debugging purposes.",
                "If you want to report a bug, you can use this to get the full exception stack."
            }
        )
        private boolean showErrorInfo;

        @Option(
            names = "--check-return-types-of-builtin-functions",
            negatable = true,
            defaultValue = "false",
            description = "Checks return types of built-in functions."
        )
        private boolean checkReturnTypesOfBuiltinFunctions;

        @Option(
            names = { "-t", "--static-typing" },
            negatable = true,
            defaultValue = "false",
            description = {
                "Activates static type analysis, which annotates the expression tree with inferred types at compile time.",
                "Enables more optimizations (experimental). Deactivated by default."
            }
        )
        private boolean staticTyping;

        @Option(
            names = "--print-inferred-types",
            negatable = true,
            defaultValue = "false",
            description = "Prints inferred types."
        )
        private boolean printInferredTypes;

        @Option(names = "--debug", negatable = true, defaultValue = "false", description = "Enables debug output.")
        private boolean debug;
    }

    public static final class Execution {
        @Option(
            names = "--native-sql-predicates",
            negatable = true,
            defaultValue = "true",
            description = "Activates native SQL predicates when possible."
        )
        private boolean nativeSQLPredicates;

        @Option(
            names = "--data-frame-execution-mode-detection",
            negatable = true,
            defaultValue = "true",
            description = "Activates DataFrame execution mode detection for higher-order functions."
        )
        private boolean dataFrameExecutionModeDetection;

        @Option(
            names = "--parallel-execution",
            negatable = true,
            defaultValue = "true",
            description = "Activates parallel execution when possible (activated by default)."
        )
        private boolean parallelExecution;

        @Option(
            names = "--data-frame-execution",
            negatable = true,
            defaultValue = "true",
            description = "Activates DataFrame execution when possible."
        )
        private boolean dataFrameExecution;

        @Option(
            names = "--native-execution",
            negatable = true,
            defaultValue = "true",
            description = "Activates native (Spark SQL) execution when possible (activated by default)."
        )
        private boolean nativeExecution;

        @Option(
            names = "--function-inlining",
            negatable = true,
            defaultValue = "true",
            description = "Activates function inlining for non-recursive functions (activated by default)."
        )
        private boolean functionInlining;

        @Option(
            names = "--tail-call-optimization",
            negatable = true,
            defaultValue = "true",
            description = "Activates tail call optimization."
        )
        private boolean tailCallOptimization;

        @Option(
            names = "--apply-updates",
            negatable = true,
            defaultValue = "false",
            description = "Applies the pending update list returned by the query."
        )
        private boolean applyUpdates;
    }

    public static final class Optimization {
        @Option(
            names = "--optimize-general-comparison-to-value-comparison",
            negatable = true,
            defaultValue = "true",
            description = "Activates automatic conversion of general comparisons to value comparisons when applicable (activated by default)."
        )
        private boolean optimizeGeneralComparisonToValueComparison;

        @Option(
            names = "--optimize-steps",
            negatable = true,
            defaultValue = "true",
            description = "Allows RumbleDB to optimize steps, might violate stability of document order (activated by default)."
        )
        private boolean optimizeSteps;

        @Option(
            names = "--optimize-steps-experimental",
            negatable = true,
            defaultValue = "false",
            description = "Experimentally optimizes steps more by skipping uniqueness and sorting in some cases. Correctness is not yet verified (disabled by default)."
        )
        private boolean optimizeStepsExperimental;

        @Option(
            names = "--optimize-parent-pointers",
            negatable = true,
            defaultValue = "true",
            description = "Allows RumbleDB to remove parent pointers from items if no steps requiring parent pointers are detected statically (activated by default)."
        )
        private boolean optimizeParentPointers;
    }

    public static final class Language {
        @Option(
            names = "--default-language",
            paramLabel = "language",
            description = "Specifies the query language to be used."
        )
        private String defaultLanguage;

        @Option(
            names = "--static-base-uri",
            paramLabel = "uri",
            description = "Sets the static base uri for the execution. This option overwrites module location but is overwritten by declaration inside query."
        )
        private String staticBaseUri;

        @Option(names = "--xml-version", paramLabel = "version", description = "Sets the XML version to use.")
        private String xmlVersion;

        @Option(
            names = "--dates-with-timezone",
            negatable = true,
            defaultValue = "false",
            description = "Activates timezone support for the type xs:date (deactivated by default)."
        )
        private boolean datesWithTimezone;

        @Option(
            names = { "--lax-json-null-validation", "--lax-json-null-valication" },
            negatable = true,
            defaultValue = "true",
            description = "Allows conflating JSON nulls with absent values when validating nillable object fields for more flexibility (activated by default)."
        )
        private boolean laxJSONNullValidation;
    }

    public static final class Formatting {
        @Option(
            names = "--default-formatting-place",
            paramLabel = "timezone",
            description = "Sets the default place used for formatting date and time values."
        )
        private String defaultFormattingPlace;

        @Option(
            names = "--default-formatting-calendar",
            paramLabel = "calendar",
            description = "Sets the default calendar used for formatting date and time values."
        )
        private String defaultFormattingCalendar;

        @Option(
            names = "--default-formatting-language",
            paramLabel = "language",
            description = "Sets the default language used for formatting date and time values."
        )
        private String defaultFormattingLanguage;
    }
}
