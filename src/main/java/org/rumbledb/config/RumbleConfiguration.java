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

package org.rumbledb.config;

import org.rumbledb.config.model.AccessConfig;
import org.rumbledb.config.model.AnalysisConfig;
import org.rumbledb.config.model.BindingsConfig;
import org.rumbledb.config.model.DebugConfig;
import org.rumbledb.config.model.ExecutionMode;
import org.rumbledb.config.model.FormattingConfig;
import org.rumbledb.config.model.InputConfig;
import org.rumbledb.config.model.OptimizationConfig;
import org.rumbledb.config.model.OutputConfig;
import org.rumbledb.config.model.RuntimeConfig;
import org.rumbledb.config.model.SemanticsConfig;
import org.rumbledb.config.model.ServerConfig;

import java.util.Objects;
import java.util.function.Consumer;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

/**
 * Temporary aggregate for the new typed configuration model.
 */
@Value
@Accessors(fluent = true)
public class RumbleConfiguration {
    /**
     * Application execution mode.
     */
    private ExecutionMode executionMode;

    private ServerConfig server;

    private AccessConfig access;

    private InputConfig input;

    private OutputConfig output;

    private RuntimeConfig runtime;

    private DebugConfig debug;

    private AnalysisConfig analysis;

    private OptimizationConfig optimization;

    private SemanticsConfig semantics;

    private FormattingConfig formatting;

    private BindingsConfig bindings;

    @Builder(toBuilder = true)
    private RumbleConfiguration(
            ExecutionMode executionMode,
            ServerConfig server,
            AccessConfig access,
            InputConfig input,
            OutputConfig output,
            RuntimeConfig runtime,
            DebugConfig debug,
            AnalysisConfig analysis,
            OptimizationConfig optimization,
            SemanticsConfig semantics,
            FormattingConfig formatting,
            BindingsConfig bindings
    ) {
        this.executionMode = Objects.requireNonNullElse(executionMode, ExecutionMode.RUN);
        this.server = Objects.requireNonNullElseGet(server, () -> ServerConfig.builder().build());
        this.access = Objects.requireNonNullElseGet(access, () -> AccessConfig.builder().build());
        this.input = Objects.requireNonNullElseGet(input, () -> InputConfig.builder().build());
        this.output = Objects.requireNonNullElseGet(output, () -> OutputConfig.builder().build());
        this.runtime = Objects.requireNonNullElseGet(runtime, () -> RuntimeConfig.builder().build());
        this.debug = Objects.requireNonNullElseGet(debug, () -> DebugConfig.builder().build());
        this.analysis = Objects.requireNonNullElseGet(analysis, () -> AnalysisConfig.builder().build());
        this.optimization = Objects.requireNonNullElseGet(optimization, () -> OptimizationConfig.builder().build());
        this.semantics = Objects.requireNonNullElseGet(semantics, () -> SemanticsConfig.builder().build());
        this.formatting = Objects.requireNonNullElseGet(formatting, () -> FormattingConfig.builder().build());
        this.bindings = Objects.requireNonNullElseGet(bindings, () -> BindingsConfig.builder().build());
    }

    // Avoid Javadoc error because it cannot resolve the builder class used as return type for the baseConfiguration
    // method in cli.commands.AbstractCommand
    public static class RumbleConfigurationBuilder {
        public RumbleConfigurationBuilder configureServer(Consumer<ServerConfig.ServerConfigBuilder> customizer) {
            ServerConfig.ServerConfigBuilder builder = this.server == null
                ? ServerConfig.builder()
                : this.server.toBuilder();
            customizer.accept(builder);
            return server(builder.build());
        }

        public RumbleConfigurationBuilder configureAccess(Consumer<AccessConfig.AccessConfigBuilder> customizer) {
            AccessConfig.AccessConfigBuilder builder = this.access == null
                ? AccessConfig.builder()
                : this.access.toBuilder();
            customizer.accept(builder);
            return access(builder.build());
        }

        public RumbleConfigurationBuilder configureInput(Consumer<InputConfig.InputConfigBuilder> customizer) {
            InputConfig.InputConfigBuilder builder = this.input == null
                ? InputConfig.builder()
                : this.input.toBuilder();
            customizer.accept(builder);
            return input(builder.build());
        }

        public RumbleConfigurationBuilder configureOutput(Consumer<OutputConfig.OutputConfigBuilder> customizer) {
            OutputConfig.OutputConfigBuilder builder = this.output == null
                ? OutputConfig.builder()
                : this.output.toBuilder();
            customizer.accept(builder);
            return output(builder.build());
        }

        public RumbleConfigurationBuilder configureRuntime(Consumer<RuntimeConfig.RuntimeConfigBuilder> customizer) {
            RuntimeConfig.RuntimeConfigBuilder builder = this.runtime == null
                ? RuntimeConfig.builder()
                : this.runtime.toBuilder();
            customizer.accept(builder);
            return runtime(builder.build());
        }

        public RumbleConfigurationBuilder configureDebug(Consumer<DebugConfig.DebugConfigBuilder> customizer) {
            DebugConfig.DebugConfigBuilder builder = this.debug == null
                ? DebugConfig.builder()
                : this.debug.toBuilder();
            customizer.accept(builder);
            return debug(builder.build());
        }

        public RumbleConfigurationBuilder configureAnalysis(Consumer<AnalysisConfig.AnalysisConfigBuilder> customizer) {
            AnalysisConfig.AnalysisConfigBuilder builder = this.analysis == null
                ? AnalysisConfig.builder()
                : this.analysis.toBuilder();
            customizer.accept(builder);
            return analysis(builder.build());
        }

        public RumbleConfigurationBuilder configureOptimization(
                Consumer<OptimizationConfig.OptimizationConfigBuilder> customizer
        ) {
            OptimizationConfig.OptimizationConfigBuilder builder = this.optimization == null
                ? OptimizationConfig.builder()
                : this.optimization.toBuilder();
            customizer.accept(builder);
            return optimization(builder.build());
        }

        public RumbleConfigurationBuilder configureSemantics(
                Consumer<SemanticsConfig.SemanticsConfigBuilder> customizer
        ) {
            SemanticsConfig.SemanticsConfigBuilder builder = this.semantics == null
                ? SemanticsConfig.builder()
                : this.semantics.toBuilder();
            customizer.accept(builder);
            return semantics(builder.build());
        }

        public RumbleConfigurationBuilder configureFormatting(
                Consumer<FormattingConfig.FormattingConfigBuilder> customizer
        ) {
            FormattingConfig.FormattingConfigBuilder builder = this.formatting == null
                ? FormattingConfig.builder()
                : this.formatting.toBuilder();
            customizer.accept(builder);
            return formatting(builder.build());
        }

        public RumbleConfigurationBuilder configureBindings(Consumer<BindingsConfig.BindingsConfigBuilder> customizer) {
            BindingsConfig.BindingsConfigBuilder builder = this.bindings == null
                ? BindingsConfig.builder()
                : this.bindings.toBuilder();
            customizer.accept(builder);
            return bindings(builder.build());
        }
    }
}
