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

import org.rumbledb.cli.commands.AbstractCommand;
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

    public static RumbleConfiguration toRumbleConfiguration(String... args) {
        CommandLine commandLine = new CommandLine(new CliOptions());
        String[] normalizedArgs = LegacyCompatibility.normalizeLegacyDynamicOptions(args);

        return toRumbleConfiguration(commandLine.parseArgs(normalizedArgs));
    }

    public static RumbleConfiguration toRumbleConfiguration(CommandLine.ParseResult parseResult) {
        CommandLine.ParseResult current = parseResult;
        while (current.subcommand() != null) {
            current = current.subcommand();
        }
        Object activeCommand = current.commandSpec().userObject();
        if (activeCommand instanceof AbstractCommand command) {
            return command.toRumbleConfiguration();
        }

        throw new IllegalStateException("No active command found in the parse result.");
    }

    public RumbleConfiguration.RumbleConfigurationBuilder baseConfiguration(
            ExecutionMode mode
    ) {
        return RumbleConfiguration.builder()
            .mode(mode)
            .io(this.io.toIOOptions())
            .limits(this.limits.toRuntimeLimits())
            .diagnostics(this.diagnostics.toDiagnosticsOptions())
            .analysis(this.analysis.toAnalysisOptions())
            .execution(this.execution.toExecutionOptions())
            .optimization(this.optimization.toOptimizationOptions())
            .language(this.language.toLanguageOptions())
            .formatting(this.formatting.toFormattingOptions())
            .externalVariableBindings(this.variables.toExternalVariableBindings());
    }
}
