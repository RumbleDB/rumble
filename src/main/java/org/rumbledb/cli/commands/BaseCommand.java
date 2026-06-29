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

import org.rumbledb.cli.options.*;
import org.rumbledb.config.ExecutionMode;
import org.rumbledb.config.RumbleConfiguration;

import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

import java.util.concurrent.Callable;

@Command
public abstract class BaseCommand implements Callable<RumbleConfiguration> {
    @Mixin
    IO io;

    @Mixin
    Limits limits;

    @Mixin
    Debug debug;

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

    protected final RumbleConfiguration.RumbleConfigurationBuilder baseConfiguration(ExecutionMode mode) {
        return RumbleConfiguration.builder()
            .executionMode(mode)
            .io(this.io.toIOOptions())
            .runtimeLimits(this.limits.toRuntimeLimits())
            .debug(this.debug.toDebugOptions())
            .analysis(this.analysis.toAnalysisOptions())
            .execution(this.execution.toExecutionOptions())
            .optimization(this.optimization.toOptimizationOptions())
            .language(this.language.toLanguageOptions())
            .formatting(this.formatting.toFormattingOptions())
            .externalVariableBindings(this.variables.toExternalVariableBindings());
    }
}
