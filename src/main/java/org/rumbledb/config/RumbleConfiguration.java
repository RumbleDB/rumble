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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;
import lombok.extern.jackson.Jacksonized;
import org.rumbledb.config.model.AccessConfig;
import org.rumbledb.config.model.AnalysisConfig;
import org.rumbledb.config.model.DebugConfig;
import org.rumbledb.config.model.ExecutionMode;
import org.rumbledb.config.model.FormattingConfig;
import org.rumbledb.config.model.InputConfig;
import org.rumbledb.config.model.OptimizationConfig;
import org.rumbledb.config.model.OutputConfig;
import org.rumbledb.config.model.RuntimeConfig;
import org.rumbledb.config.model.SemanticsConfig;
import org.rumbledb.config.model.ServerConfig;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Temporary aggregate for the new typed configuration model.
 */
@Value
@Jacksonized
@Accessors(fluent = true)
@JsonDeserialize(builder = RumbleConfiguration.RumbleConfigurationBuilder.class)
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
            FormattingConfig formatting
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
    }

    // Avoid Javadoc error because it cannot resolve the builder class used as return type for the baseConfiguration
    // method in cli.commands.AbstractCommand
    @JsonPOJOBuilder(withPrefix = "")
    public static class RumbleConfigurationBuilder {
        private final Map<String, Object> withEntries = new LinkedHashMap<>();

        public RumbleConfigurationBuilder with(String key, Object value) {
            this.withEntries.put(Objects.requireNonNull(key, "Configuration key cannot be null."), value);
            return this;
        }

        public RumbleConfiguration build() {
            RumbleConfiguration baseConfiguration = new RumbleConfiguration(
                    this.executionMode,
                    this.server,
                    this.access,
                    this.input,
                    this.output,
                    this.runtime,
                    this.debug,
                    this.analysis,
                    this.optimization,
                    this.semantics,
                    this.formatting
            );
            if (this.withEntries.isEmpty()) {
                return baseConfiguration;
            }
            return RumbleConfigurationResolver.apply(baseConfiguration, this.withEntries);
        }

        private <T, B> T configureSection(
                T currentValue,
                Supplier<B> emptyBuilder,
                Function<T, B> toBuilder,
                Consumer<B> customizer,
                Function<B, T> build
        ) {
            B builder = currentValue == null ? emptyBuilder.get() : toBuilder.apply(currentValue);
            customizer.accept(builder);
            return build.apply(builder);
        }

        @Tolerate // So Lombok still keep the original builder setter method
        public RumbleConfigurationBuilder server(Consumer<ServerConfig.ServerConfigBuilder> customizer) {
            return server(
                configureSection(
                    this.server,
                    ServerConfig::builder,
                    ServerConfig::toBuilder,
                    customizer,
                    ServerConfig.ServerConfigBuilder::build
                )
            );
        }

        @Tolerate
        public RumbleConfigurationBuilder access(Consumer<AccessConfig.AccessConfigBuilder> customizer) {
            return access(
                configureSection(
                    this.access,
                    AccessConfig::builder,
                    AccessConfig::toBuilder,
                    customizer,
                    AccessConfig.AccessConfigBuilder::build
                )
            );
        }

        @Tolerate
        public RumbleConfigurationBuilder input(Consumer<InputConfig.InputConfigBuilder> customizer) {
            return input(
                configureSection(
                    this.input,
                    InputConfig::builder,
                    InputConfig::toBuilder,
                    customizer,
                    InputConfig.InputConfigBuilder::build
                )
            );
        }

        @Tolerate
        public RumbleConfigurationBuilder output(Consumer<OutputConfig.OutputConfigBuilder> customizer) {
            return output(
                configureSection(
                    this.output,
                    OutputConfig::builder,
                    OutputConfig::toBuilder,
                    customizer,
                    OutputConfig.OutputConfigBuilder::build
                )
            );
        }

        @Tolerate
        public RumbleConfigurationBuilder runtime(Consumer<RuntimeConfig.RuntimeConfigBuilder> customizer) {
            return runtime(
                configureSection(
                    this.runtime,
                    RuntimeConfig::builder,
                    RuntimeConfig::toBuilder,
                    customizer,
                    RuntimeConfig.RuntimeConfigBuilder::build
                )
            );
        }

        @Tolerate
        public RumbleConfigurationBuilder debug(Consumer<DebugConfig.DebugConfigBuilder> customizer) {
            return debug(
                configureSection(
                    this.debug,
                    DebugConfig::builder,
                    DebugConfig::toBuilder,
                    customizer,
                    DebugConfig.DebugConfigBuilder::build
                )
            );
        }

        @Tolerate
        public RumbleConfigurationBuilder analysis(Consumer<AnalysisConfig.AnalysisConfigBuilder> customizer) {
            return analysis(
                configureSection(
                    this.analysis,
                    AnalysisConfig::builder,
                    AnalysisConfig::toBuilder,
                    customizer,
                    AnalysisConfig.AnalysisConfigBuilder::build
                )
            );
        }

        @Tolerate
        public RumbleConfigurationBuilder optimization(
                Consumer<OptimizationConfig.OptimizationConfigBuilder> customizer
        ) {
            return optimization(
                configureSection(
                    this.optimization,
                    OptimizationConfig::builder,
                    OptimizationConfig::toBuilder,
                    customizer,
                    OptimizationConfig.OptimizationConfigBuilder::build
                )
            );
        }

        @Tolerate
        public RumbleConfigurationBuilder semantics(Consumer<SemanticsConfig.SemanticsConfigBuilder> customizer) {
            return semantics(
                configureSection(
                    this.semantics,
                    SemanticsConfig::builder,
                    SemanticsConfig::toBuilder,
                    customizer,
                    SemanticsConfig.SemanticsConfigBuilder::build
                )
            );
        }

        @Tolerate
        public RumbleConfigurationBuilder formatting(Consumer<FormattingConfig.FormattingConfigBuilder> customizer) {
            return formatting(
                configureSection(
                    this.formatting,
                    FormattingConfig::builder,
                    FormattingConfig::toBuilder,
                    customizer,
                    FormattingConfig.FormattingConfigBuilder::build
                )
            );
        }
    }
}
