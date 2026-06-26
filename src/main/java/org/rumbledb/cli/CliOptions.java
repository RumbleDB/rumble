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
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ScopeType;

import java.time.ZoneId;
import java.util.ArrayList;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class CliOptions {
    private interface ConfigurationSource {
        ExecutionMode mode();

        default Server server(CliOptions root) {
            return root.server;
        }

        default Input input() {
            return null;
        }

        default Output output() {
            return null;
        }

        default String queryPath() {
            return null;
        }

        default RumbleConfiguration toRumbleConfiguration(CliOptions root) {
            return buildConfiguration(
                mode(),
                server(root),
                input(),
                output(),
                root.common,
                queryPath()
            );
        }
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
            this.common,
            effectiveQueryPath
        );
    }

    private static RumbleConfiguration buildConfiguration(
            ExecutionMode mode,
            Server server,
            Input input,
            Output output,
            CommonOptions common,
            String queryPath
    ) {
        Limits limits = common.limits;
        Diagnostics diagnostics = common.diagnostics;
        Execution execution = common.execution;
        Optimization optimization = common.optimization;
        Language language = common.language;
        Formatting formatting = common.formatting;
        DynamicOptions dynamicOptions = common.dynamicOptions;

        Map<Name, String> unparsedExternalVariableValues = new HashMap<>();
        dynamicOptions.variables.forEach(
            (name, value) -> unparsedExternalVariableValues.put(Name.createVariableInNoNamespace(name), value)
        );
        if (input != null && input.contextItem != null) {
            unparsedExternalVariableValues.put(Name.CONTEXT_ITEM, input.contextItem);
        }

        Map<Name, String> externalVariableValuesReadFromFiles = new HashMap<>();
        dynamicOptions.variablesFromFiles.forEach(
            (name, value) -> externalVariableValuesReadFromFiles.put(Name.createVariableInNoNamespace(name), value)
        );

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
        applyIfPresent(formatting.defaultFormattingPlace, formattingBuilder::defaultFormattingPlace);
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
            .externalVariableBindings(
                ExternalVariableBindings.builder()
                    .unparsedExternalVariableValues(unparsedExternalVariableValues)
                    .externalVariableValuesReadFromFiles(externalVariableValuesReadFromFiles)
                    .externalVariablesReadFromStandardInput(externalVariablesReadFromStandardInput)
                    .externalVariablesInputFormats(externalVariablesInputFormats)
                    .build()
            )
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

    @Mixin
    Server server;

    @Mixin
    Input input;

    @Mixin
    Output output;

    @Mixin
    CommonOptions common;

    @Parameters(
        index = "0",
        arity = "0..1",
        paramLabel = "query-file",
        description = "A JSONiq query file to read from (from any file system, even the Web!)."
    )
    String queryPath;

    @Command(name = "run", description = "Executes a query.", mixinStandardHelpOptions = false)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static final class Run implements ConfigurationSource {
        @Mixin
        Input input;

        @Mixin
        Output output;

        @Parameters(
            index = "0",
            arity = "0..1",
            paramLabel = "query-file",
            description = "A JSONiq query file to read from (from any file system, even the Web!)."
        )
        String queryPath;

        @Override
        public ExecutionMode mode() {
            return ExecutionMode.RUN;
        }

        @Override
        public Input input() {
            return this.input;
        }

        @Override
        public Output output() {
            return this.output;
        }

        @Override
        public String queryPath() {
            return this.input.queryPath != null ? this.input.queryPath : this.queryPath;
        }
    }

    @Command(name = "serve", description = "Runs RumbleDB as a server on port 8001.", mixinStandardHelpOptions = false)
    public static final class Serve implements ConfigurationSource {
        @Override
        public ExecutionMode mode() {
            return ExecutionMode.SERVE;
        }
    }

    @Command(name = "repl", description = "Runs the interactive shell.", mixinStandardHelpOptions = false)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static final class Repl implements ConfigurationSource {
        @Mixin
        Output output;

        @Override
        public ExecutionMode mode() {
            return ExecutionMode.REPL;
        }

        @Override
        public Output output() {
            return this.output;
        }
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static final class CommonOptions {
        @Mixin
        Limits limits;

        @Mixin
        Diagnostics diagnostics;

        @Mixin
        Execution execution;

        @Mixin
        Optimization optimization;

        @Mixin
        Language language;

        @Mixin
        Formatting formatting;

        @Mixin
        DynamicOptions dynamicOptions;
    }

    public static final class Server {
        @Option(
            names = { "-h", "--host" },
            scope = ScopeType.INHERIT,
            paramLabel = "host",
            description = "Changes the host of the RumbleDB HTTP server to any of your liking."
        )
        private String host;

        @Option(
            names = { "-p", "--port" },
            scope = ScopeType.INHERIT,
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
            scope = ScopeType.INHERIT,
            paramLabel = "name=value",
            description = {
                "Initializes a global variable to the supplied value.",
                "The query must contain the corresponding global variable declaration, e.g. "
                    + "\"declare variable $foo external;\""
            }
        )
        private Map<String, String> variables = new HashMap<>();

        @Option(
            names = "--variable-from-file",
            scope = ScopeType.INHERIT,
            paramLabel = "name=path",
            description = "Initializes a global variable with a value read from the supplied file."
        )
        private Map<String, String> variablesFromFiles = new HashMap<>();

        @Option(
            names = "--output-format-option",
            scope = ScopeType.INHERIT,
            paramLabel = "name=value",
            description = "Options to further specify the output format, for example a separator character for CSV or a compression format."
        )
        private Map<String, String> outputFormatOptions = new HashMap<>();
    }

    public static final class Limits {
        @Option(
            names = "--result-size",
            scope = ScopeType.INHERIT,
            paramLabel = "count",
            description = "A cap on the maximum number of items to output on the screen or to a local list."
        )
        private Integer resultSize;

        @Option(
            names = { "-c", "--materialization-cap" },
            scope = ScopeType.INHERIT,
            paramLabel = "count",
            description = "A cap on the maximum number of items to materialize during the query execution for large sequences within a query."
        )
        private Integer materializationCap;
    }

    public static final class Diagnostics {
        @Option(
            names = "--print-iterator-tree",
            scope = ScopeType.INHERIT,
            negatable = true,
            description = "For debugging purposes, prints out the expression tree and runtime interator tree."
        )
        private boolean printIteratorTree;

        @Option(
            names = { "-v", "--show-error-info" },
            scope = ScopeType.INHERIT,
            negatable = true,
            description = {
                "For debugging purposes.",
                "If you want to report a bug, you can use this to get the full exception stack."
            }
        )
        private boolean showErrorInfo;

        @Option(
            names = "--check-return-types-of-builtin-functions",
            scope = ScopeType.INHERIT,
            negatable = true,
            description = "Checks return types of built-in functions."
        )
        private boolean checkReturnTypesOfBuiltinFunctions;

        @Option(
            names = { "-t", "--static-typing" },
            scope = ScopeType.INHERIT,
            negatable = true,
            description = {
                "Activates static type analysis, which annotates the expression tree with inferred types at compile time.",
                "Enables more optimizations (experimental). Deactivated by default."
            }
        )
        private boolean staticTyping;

        @Option(
            names = "--print-inferred-types",
            scope = ScopeType.INHERIT,
            negatable = true,
            description = "Prints inferred types."
        )
        private boolean printInferredTypes;

        @Option(
            names = "--debug",
            scope = ScopeType.INHERIT,
            negatable = true,
            description = "Enables debug output."
        )
        private boolean debug;
    }

    public static final class Execution {
        @Option(
            names = "--native-sql-predicates",
            scope = ScopeType.INHERIT,
            negatable = true,
            defaultValue = "true",
            fallbackValue = "true",
            description = "Activates native SQL predicates when possible."
        )
        private boolean nativeSQLPredicates;

        @Option(
            names = "--data-frame-execution-mode-detection",
            scope = ScopeType.INHERIT,
            negatable = true,
            defaultValue = "true",
            fallbackValue = "true",
            description = "Activates DataFrame execution mode detection for higher-order functions."
        )
        private boolean dataFrameExecutionModeDetection;

        @Option(
            names = "--parallel-execution",
            scope = ScopeType.INHERIT,
            negatable = true,
            defaultValue = "true",
            fallbackValue = "true",
            description = "Activates parallel execution when possible (activated by default)."
        )
        private boolean parallelExecution;

        @Option(
            names = "--data-frame-execution",
            scope = ScopeType.INHERIT,
            negatable = true,
            defaultValue = "true",
            fallbackValue = "true",
            description = "Activates DataFrame execution when possible."
        )
        private boolean dataFrameExecution;

        @Option(
            names = "--native-execution",
            scope = ScopeType.INHERIT,
            negatable = true,
            defaultValue = "true",
            fallbackValue = "true",
            description = "Activates native (Spark SQL) execution when possible (activated by default)."
        )
        private boolean nativeExecution;

        @Option(
            names = "--function-inlining",
            scope = ScopeType.INHERIT,
            negatable = true,
            defaultValue = "true",
            fallbackValue = "true",
            description = "Activates function inlining for non-recursive functions (activated by default)."
        )
        private boolean functionInlining;

        @Option(
            names = "--tail-call-optimization",
            scope = ScopeType.INHERIT,
            negatable = true,
            defaultValue = "true",
            fallbackValue = "true",
            description = "Activates tail call optimization."
        )
        private boolean tailCallOptimization;

        @Option(
            names = "--apply-updates",
            scope = ScopeType.INHERIT,
            negatable = true,
            description = "Applies the pending update list returned by the query."
        )
        private boolean applyUpdates;
    }

    public static final class Optimization {
        @Option(
            names = "--optimize-general-comparison-to-value-comparison",
            scope = ScopeType.INHERIT,
            negatable = true,
            defaultValue = "true",
            fallbackValue = "true",
            description = "Activates automatic conversion of general comparisons to value comparisons when applicable (activated by default)."
        )
        private boolean optimizeGeneralComparisonToValueComparison;

        @Option(
            names = "--optimize-steps",
            scope = ScopeType.INHERIT,
            negatable = true,
            defaultValue = "true",
            fallbackValue = "true",
            description = "Allows RumbleDB to optimize steps, might violate stability of document order (activated by default)."
        )
        private boolean optimizeSteps;

        @Option(
            names = "--optimize-steps-experimental",
            scope = ScopeType.INHERIT,
            negatable = true,
            description = "Experimentally optimizes steps more by skipping uniqueness and sorting in some cases. Correctness is not yet verified (disabled by default)."
        )
        private boolean optimizeStepsExperimental;

        @Option(
            names = "--optimize-parent-pointers",
            scope = ScopeType.INHERIT,
            negatable = true,
            defaultValue = "true",
            fallbackValue = "true",
            description = "Allows RumbleDB to remove parent pointers from items if no steps requiring parent pointers are detected statically (activated by default)."
        )
        private boolean optimizeParentPointers;
    }

    public static final class Language {
        @Option(
            names = "--default-language",
            scope = ScopeType.INHERIT,
            paramLabel = "language",
            description = "Specifies the query language to be used."
        )
        private String defaultLanguage;

        @Option(
            names = "--static-base-uri",
            scope = ScopeType.INHERIT,
            paramLabel = "uri",
            description = "Sets the static base uri for the execution. This option overwrites module location but is overwritten by declaration inside query."
        )
        private String staticBaseUri;

        @Option(
            names = "--xml-version",
            scope = ScopeType.INHERIT,
            paramLabel = "version",
            description = "Sets the XML version to use."
        )
        private String xmlVersion;

        @Option(
            names = "--dates-with-timezone",
            scope = ScopeType.INHERIT,
            negatable = true,
            description = "Activates timezone support for the type xs:date (deactivated by default)."
        )
        private boolean datesWithTimezone;

        @Option(
            names = { "--lax-json-null-validation", "--lax-json-null-valication" },
            scope = ScopeType.INHERIT,
            negatable = true,
            defaultValue = "true",
            fallbackValue = "true",
            description = "Allows conflating JSON nulls with absent values when validating nillable object fields for more flexibility (activated by default)."
        )
        private boolean laxJSONNullValidation;
    }

    public static final class Formatting {
        @Option(
            names = "--default-formatting-place",
            scope = ScopeType.INHERIT,
            paramLabel = "timezone",
            description = "Sets the default place used for formatting date and time values."
        )
        private ZoneId defaultFormattingPlace;

        @Option(
            names = "--default-formatting-calendar",
            scope = ScopeType.INHERIT,
            paramLabel = "calendar",
            description = "Sets the default calendar used for formatting date and time values."
        )
        private String defaultFormattingCalendar;

        @Option(
            names = "--default-formatting-language",
            scope = ScopeType.INHERIT,
            paramLabel = "language",
            description = "Sets the default language used for formatting date and time values."
        )
        private String defaultFormattingLanguage;
    }
}
