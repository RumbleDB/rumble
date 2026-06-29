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

import org.rumbledb.cli.options.Access;
import org.rumbledb.cli.options.Analysis;
import org.rumbledb.cli.options.Bindings;
import org.rumbledb.cli.options.Debug;
import org.rumbledb.cli.options.Formatting;
import org.rumbledb.cli.options.Optimization;
import org.rumbledb.cli.options.Runtime;
import org.rumbledb.cli.options.Semantics;
import org.rumbledb.config.ExecutionMode;
import org.rumbledb.config.RumbleConfiguration;

import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

import java.util.concurrent.Callable;

@Command
public abstract class BaseCommand implements Callable<RumbleConfiguration> {
    @Mixin
    Access access;

    @Mixin
    Runtime runtime;

    @Mixin
    Debug debug;

    @Mixin
    Analysis analysis;

    @Mixin
    Optimization optimization;

    @Mixin
    Semantics semantics;

    @Mixin
    Formatting formatting;

    @Mixin
    Bindings bindings;

    protected final RumbleConfiguration.RumbleConfigurationBuilder baseConfiguration(ExecutionMode mode) {
        return RumbleConfiguration.builder()
            .executionMode(mode)
            .access(this.access.toAccess())
            .runtime(this.runtime.toRuntime())
            .debug(this.debug.toDebugOptions())
            .analysis(this.analysis.toAnalysisOptions())
            .optimization(this.optimization.toOptimizationOptions())
            .semantics(this.semantics.toSemantics())
            .formatting(this.formatting.toFormattingOptions())
            .bindings(this.bindings.toBindings());
    }
}
