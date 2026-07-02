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

package org.rumbledb.cli.commands;

import java.util.concurrent.Callable;

import org.rumbledb.cli.CLIInvocation;
import org.rumbledb.cli.arguments.AccessArguments;
import org.rumbledb.cli.arguments.AnalysisArguments;
import org.rumbledb.cli.arguments.BindingsArguments;
import org.rumbledb.cli.arguments.DebugArguments;
import org.rumbledb.cli.arguments.FormattingArguments;
import org.rumbledb.cli.arguments.OptimizationArguments;
import org.rumbledb.cli.arguments.RuntimeArguments;
import org.rumbledb.cli.arguments.SemanticsArguments;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.config.model.ExecutionMode;

import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;


@Command
public abstract class BaseCommand implements Callable<CLIInvocation> {
    @Mixin
    AccessArguments access;

    @Mixin
    RuntimeArguments runtime;

    @Mixin
    DebugArguments debug;

    @Mixin
    AnalysisArguments analysis;

    @Mixin
    OptimizationArguments optimization;

    @Mixin
    SemanticsArguments semantics;

    @Mixin
    FormattingArguments formatting;

    @Mixin
    BindingsArguments bindings;

    protected final RumbleConfiguration.RumbleConfigurationBuilder baseConfiguration(ExecutionMode mode) {
        return RumbleConfiguration.builder()
            .executionMode(mode)
            .access(this.access.toConfig())
            .runtime(this.runtime.toConfig())
            .debug(this.debug.toConfig())
            .analysis(this.analysis.toConfig())
            .optimization(this.optimization.toConfig())
            .semantics(this.semantics.toConfig())
            .formatting(this.formatting.toConfig());
    }

    protected final CLIInvocation invocation(RumbleConfiguration configuration) {
        return new CLIInvocation(configuration, this.bindings.toExternalBindings());
    }
}
