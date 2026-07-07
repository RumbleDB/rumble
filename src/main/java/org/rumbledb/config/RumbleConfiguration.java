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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.experimental.NonFinal;
import lombok.extern.jackson.Jacksonized;
import org.rumbledb.config.model.AccessConfig;
import org.rumbledb.config.model.AnalysisConfig;
import org.rumbledb.config.model.DebugConfig;
import org.rumbledb.config.model.RumbleMode;
import org.rumbledb.config.model.FormattingConfig;
import org.rumbledb.config.model.InputConfig;
import org.rumbledb.config.model.OptimizationConfig;
import org.rumbledb.config.model.OutputConfig;
import org.rumbledb.config.model.RuntimeConfig;
import org.rumbledb.config.model.SemanticsConfig;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Temporary aggregate for the new typed configuration model.
 */
@Value
@Jacksonized
@Accessors(fluent = true)
@JsonDeserialize(builder = RumbleConfiguration.RumbleConfigurationBuilder.class)
public class RumbleConfiguration implements Serializable, KryoSerializable {
    private static final long serialVersionUID = 1L;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Application execution mode.
     */
    @NonFinal
    private RumbleMode mode;

    @NonFinal
    private AccessConfig access;

    @NonFinal
    private InputConfig input;

    @NonFinal
    private OutputConfig output;

    @NonFinal
    private RuntimeConfig runtime;

    @NonFinal
    private DebugConfig debug;

    @NonFinal
    private AnalysisConfig analysis;

    @NonFinal
    private OptimizationConfig optimization;

    @NonFinal
    private SemanticsConfig semantics;

    @NonFinal
    private FormattingConfig formatting;

    public static RumbleConfiguration defaultConfiguration() {
        return RumbleConfiguration.builder().build();
    }

    @Builder(toBuilder = true)
    private RumbleConfiguration(
            RumbleMode mode,
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
        this.mode = Objects.requireNonNullElse(mode, RumbleMode.RUN);
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

    @Override
    public void write(Kryo kryo, Output output) {
        try {
            output.writeString(MAPPER.writeValueAsString(this));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not serialize RumbleConfiguration.", e);
        }
    }

    @Override
    public void read(Kryo kryo, Input input) {
        try {
            RumbleConfiguration deserialized = MAPPER.readValue(input.readString(), RumbleConfiguration.class);
            this.mode = deserialized.mode;
            this.access = deserialized.access;
            this.input = deserialized.input;
            this.output = deserialized.output;
            this.runtime = deserialized.runtime;
            this.debug = deserialized.debug;
            this.analysis = deserialized.analysis;
            this.optimization = deserialized.optimization;
            this.semantics = deserialized.semantics;
            this.formatting = deserialized.formatting;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not deserialize RumbleConfiguration.", e);
        }
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
                    this.mode,
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
    }
}
