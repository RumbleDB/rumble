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

import org.rumbledb.cli.options.DynamicOptions;
import org.rumbledb.cli.options.Formatting;
import org.rumbledb.cli.options.Optimization;
import org.rumbledb.config.AnalysisOptions;
import org.rumbledb.config.ExecutionMode;
import org.rumbledb.config.ExecutionOptions;
import org.rumbledb.config.ExternalVariableBindings;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.config.ServerOptions;
import org.rumbledb.context.Name;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Parameters;

import java.util.HashMap;
import java.util.HashSet;
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
    public static RumbleConfiguration toRumbleConfiguration(String... args) {
        CommandLine commandLine = new CommandLine(new CliOptions());
        String[] normalizedArgs = DynamicOptions.normalizeLegacyDynamicOptions(args);

        return toRumbleConfiguration(commandLine.parseArgs(normalizedArgs));
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


        ServerOptions.ServerOptionsBuilder serverBuilder = ServerOptions.builder().mode(mode);
        applyIfPresent(server.host, serverBuilder::host);
        applyIfPresent(server.port, serverBuilder::port);


        return RumbleConfiguration.builder()
            .server(serverBuilder.build())
            .io(ioBuilder.build())
            .limits(limits.toRuntimeLimits())
            .diagnostics(diagnostics.toDiagnosticsOptions())
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
                optimization.toOptimizationOptions()
            )
            .language(language.toLanguageOptions())
            .formatting(formatting.toFormattingOptions())
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



}
