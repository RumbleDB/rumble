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
import lombok.Data;

/**
 * Temporary aggregate for the new typed configuration model.
 */
@Data
@Builder
public class RumbleConfiguration {
    /**
     * Application execution mode.
     */
    @Default
    private ExecutionMode mode = ExecutionMode.RUN;

    @Default
    private ServerOptions server = ServerOptions.builder().build();

    @Default
    private IOOptions io = IOOptions.builder().build();

    @Default
    private InputOptions input = InputOptions.builder().build();

    @Default
    private OutputOptions output = OutputOptions.builder().build();

    @Default
    private RuntimeLimits limits = RuntimeLimits.builder().build();

    @Default
    private DiagnosticsOptions diagnostics = DiagnosticsOptions.builder().build();

    @Default
    private AnalysisOptions analysis = AnalysisOptions.builder().build();

    @Default
    private ExecutionOptions execution = ExecutionOptions.builder().build();

    @Default
    private OptimizationOptions optimization = OptimizationOptions.builder().build();

    @Default
    private LanguageOptions language = LanguageOptions.builder().build();

    @Default
    private FormattingOptions formatting = FormattingOptions.builder().build();

    @Default
    private ExternalVariableBindings externalVariableBindings = ExternalVariableBindings.builder().build();
}
