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

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;
import lombok.experimental.Accessors;

/**
 * Temporary aggregate for the new typed configuration model.
 */
@Value
@Builder(toBuilder = true)
@Accessors(fluent = true)
public class RumbleConfiguration {
    /**
     * Application execution mode.
     */
    @Default
    private ExecutionMode executionMode = ExecutionMode.RUN;

    @Default
    private ServerConfig server = ServerConfig.builder().build();

    @Default
    private AccessConfig access = AccessConfig.builder().build();

    @Default
    private InputConfig input = InputConfig.builder().build();

    @Default
    private OutputConfig output = OutputConfig.builder().build();

    @Default
    private RuntimeConfig runtime = RuntimeConfig.builder().build();

    @Default
    private DebugConfig debug = DebugConfig.builder().build();

    @Default
    private AnalysisConfig analysis = AnalysisConfig.builder().build();

    @Default
    private OptimizationConfig optimization = OptimizationConfig.builder().build();

    @Default
    private SemanticsConfig semantics = SemanticsConfig.builder().build();

    @Default
    private FormattingConfig formatting = FormattingConfig.builder().build();

    @Default
    private BindingsConfig bindings = BindingsConfig.builder().build();

    // Avoid Javadoc error because it cannot resolve the builder class used as return type for the baseConfiguration
    // method in cli.commands.AbstractCommand
    public static class RumbleConfigurationBuilder {
    }
}
