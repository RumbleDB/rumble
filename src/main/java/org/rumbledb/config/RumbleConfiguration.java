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

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;

/**
 * Temporary aggregate for the new typed configuration model.
 */
@Value
@Builder(toBuilder = true)
public class RumbleConfiguration {
    /**
     * Application execution mode.
     */
    @Default
    private ExecutionMode executionMode = ExecutionMode.RUN;

    @Default
    private ServerOptions serverOptions = ServerOptions.builder().build();

    @Default
    private IOOptions ioOptions = IOOptions.builder().build();

    @Default
    private InputOptions inputOptions = InputOptions.builder().build();

    @Default
    private OutputOptions outputOptions = OutputOptions.builder().build();

    @Default
    private RuntimeLimits runtimeLimits = RuntimeLimits.builder().build();

    @Default
    private DiagnosticsOptions diagnosticsOptions = DiagnosticsOptions.builder().build();

    @Default
    private AnalysisOptions analysisOptions = AnalysisOptions.builder().build();

    @Default
    private ExecutionOptions executionOptions = ExecutionOptions.builder().build();

    @Default
    private OptimizationOptions optimizationOptions = OptimizationOptions.builder().build();

    @Default
    private LanguageOptions languageOptions = LanguageOptions.builder().build();

    @Default
    private FormattingOptions formattedOptions = FormattingOptions.builder().build();

    @Default
    private ExternalVariableBindings externalVariableBindings = ExternalVariableBindings.builder().build();

    /// Avoid Javadoc error because it cannot resolve the builder class
    /// Used as return type for the baseConfiguration method in cli.commands.AbstractCommand
    public static class RumbleConfigurationBuilder {
    }
}
