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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

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
                B builder,
                Consumer<B> customizer,
                Function<B, T> build
        ) {
            customizer.accept(builder);
            return build.apply(builder);
        }

        public RumbleConfigurationBuilder accessWith(Consumer<AccessConfig.AccessConfigBuilder> customizer) {
            return access(
                configureSection(
                    this.access == null ? AccessConfig.builder() : this.access.toBuilder(),
                    customizer,
                    AccessConfig.AccessConfigBuilder::build
                )
            );
        }

        public RumbleConfigurationBuilder inputWith(Consumer<InputConfig.InputConfigBuilder> customizer) {
            return input(
                configureSection(
                    this.input == null ? InputConfig.builder() : this.input.toBuilder(),
                    customizer,
                    InputConfig.InputConfigBuilder::build
                )
            );
        }

        public RumbleConfigurationBuilder outputWith(Consumer<OutputConfig.OutputConfigBuilder> customizer) {
            return output(
                configureSection(
                    this.output == null ? OutputConfig.builder() : this.output.toBuilder(),
                    customizer,
                    OutputConfig.OutputConfigBuilder::build
                )
            );
        }

        public RumbleConfigurationBuilder runtimeWith(Consumer<RuntimeConfig.RuntimeConfigBuilder> customizer) {
            return runtime(
                configureSection(
                    this.runtime == null ? RuntimeConfig.builder() : this.runtime.toBuilder(),
                    customizer,
                    RuntimeConfig.RuntimeConfigBuilder::build
                )
            );
        }

        public RumbleConfigurationBuilder debugWith(Consumer<DebugConfig.DebugConfigBuilder> customizer) {
            return debug(
                configureSection(
                    this.debug == null ? DebugConfig.builder() : this.debug.toBuilder(),
                    customizer,
                    DebugConfig.DebugConfigBuilder::build
                )
            );
        }

        public RumbleConfigurationBuilder analysisWith(Consumer<AnalysisConfig.AnalysisConfigBuilder> customizer) {
            return analysis(
                configureSection(
                    this.analysis == null ? AnalysisConfig.builder() : this.analysis.toBuilder(),
                    customizer,
                    AnalysisConfig.AnalysisConfigBuilder::build
                )
            );
        }

        public RumbleConfigurationBuilder optimizationWith(
                Consumer<OptimizationConfig.OptimizationConfigBuilder> customizer
        ) {
            return optimization(
                configureSection(
                    this.optimization == null ? OptimizationConfig.builder() : this.optimization.toBuilder(),
                    customizer,
                    OptimizationConfig.OptimizationConfigBuilder::build
                )
            );
        }

        public RumbleConfigurationBuilder semanticsWith(Consumer<SemanticsConfig.SemanticsConfigBuilder> customizer) {
            return semantics(
                configureSection(
                    this.semantics == null ? SemanticsConfig.builder() : this.semantics.toBuilder(),
                    customizer,
                    SemanticsConfig.SemanticsConfigBuilder::build
                )
            );
        }

        public RumbleConfigurationBuilder formattingWith(
                Consumer<FormattingConfig.FormattingConfigBuilder> customizer
        ) {
            return formatting(
                configureSection(
                    this.formatting == null ? FormattingConfig.builder() : this.formatting.toBuilder(),
                    customizer,
                    FormattingConfig.FormattingConfigBuilder::build
                )
            );
        }
    }
}
