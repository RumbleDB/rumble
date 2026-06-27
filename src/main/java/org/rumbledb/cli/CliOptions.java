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

import java.util.List;

import org.rumbledb.cli.commands.Repl;
import org.rumbledb.cli.commands.Run;
import org.rumbledb.cli.commands.Serve;
import org.rumbledb.cli.options.Analysis;
import org.rumbledb.cli.options.Diagnostics;
import org.rumbledb.cli.options.Execution;
import org.rumbledb.cli.options.Formatting;
import org.rumbledb.cli.options.IO;
import org.rumbledb.cli.options.Language;
import org.rumbledb.cli.options.LegacyCompatibility;
import org.rumbledb.cli.options.Limits;
import org.rumbledb.cli.options.Optimization;
import org.rumbledb.cli.options.Variables;
import org.rumbledb.config.ExecutionMode;
import org.rumbledb.config.RumbleConfiguration;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

@Command(
    name = "rumbledb",
    description = "RumbleDB command line interface.",
    mixinStandardHelpOptions = false,
    subcommands = {
        Run.class,
        Serve.class,
        Repl.class
    }
)
public final class CliOptions {
    @Mixin
    IO io;

    @Mixin
    Limits limits;

    @Mixin
    Diagnostics diagnostics;

    @Mixin
    Analysis analysis;

    @Mixin
    Execution execution;

    @Mixin
    Optimization optimization;

    @Mixin
    Language language;

    @Mixin
    Formatting formatting;

    @Mixin
    Variables variables;

    public static RumbleConfiguration parse(String... args) {
        CommandLine commandLine = new CommandLine(new CliOptions());
        String[] normalizedArgs =
            LegacyCompatibility.normalizeLegacyDynamicOptions(args);

        CommandLine.ParseResult parseResult =
            commandLine.parseArgs(normalizedArgs);

        commandLine.getExecutionStrategy().execute(parseResult);

        List<CommandLine> commands = parseResult.asCommandLineList();
        CommandLine activeCommand = commands.get(commands.size() - 1);

        return activeCommand.getExecutionResult();
    }

    public RumbleConfiguration.RumbleConfigurationBuilder baseConfiguration(
            ExecutionMode mode
    ) {
        return RumbleConfiguration.builder()
            .executionMode(mode)
            .ioOptions(this.io.toIOOptions())
            .runtimeLimits(this.limits.toRuntimeLimits())
            .diagnosticsOptions(this.diagnostics.toDiagnosticsOptions())
            .analysisOptions(this.analysis.toAnalysisOptions())
            .executionOptions(this.execution.toExecutionOptions())
            .optimizationOptions(this.optimization.toOptimizationOptions())
            .languageOptions(this.language.toLanguageOptions())
            .formattedOptions(this.formatting.toFormattingOptions())
            .externalVariableBindings(this.variables.toExternalVariableBindings());
    }
}
