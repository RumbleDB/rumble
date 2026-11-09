/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.cli.arguments;

import picocli.CommandLine.Option;

import org.rumbledb.config.model.OptimizationConfig;

public final class OptimizationArguments {
    @Option(
            names = "--function-inlining",
            negatable = true,
            description = "Activates function inlining for non-recursive functions (activated by default).")
    private Boolean functionInlining;

    @Option(names = "--tail-call-optimization", negatable = true, description = "Activates tail call optimization.")
    private Boolean tailCallOptimization;

    @Option(
            names = "--optimize-general-comparison-to-value-comparison",
            negatable = true,
            description =
                    "Activates automatic conversion of general comparisons to value comparisons when applicable (activated by default).")
    private Boolean optimizeGeneralComparisonToValueComparison;

    @Option(
            names = "--optimize-steps",
            negatable = true,
            description =
                    "Allows RumbleDB to optimize steps, might violate stability of document order (activated by default).")
    private Boolean optimizeSteps;

    @Option(
            names = "--optimize-steps-experimental",
            negatable = true,
            description =
                    "Experimentally optimizes steps more by skipping uniqueness and sorting in some cases. Correctness is not yet verified (disabled by default).")
    private Boolean optimizeStepsExperimental;

    @Option(
            names = "--optimize-parent-pointers",
            negatable = true,
            description =
                    "Allows RumbleDB to remove parent pointers from items if no steps requiring parent pointers are detected statically (activated by default).")
    private Boolean optimizeParentPointers;

    public OptimizationConfig toConfig() {
        OptimizationConfig.OptimizationConfigBuilder builder = OptimizationConfig.builder();

        OptionConversion.applyBooleanIfPresent(
                this.optimizeGeneralComparisonToValueComparison, builder::optimizeGeneralComparisonToValueComparison);
        OptionConversion.applyBooleanIfPresent(this.functionInlining, builder::useFunctionInlining);
        OptionConversion.applyBooleanIfPresent(this.tailCallOptimization, builder::useTailCallOptimization);
        OptionConversion.applyBooleanIfPresent(this.optimizeSteps, builder::optimizeSteps);
        OptionConversion.applyBooleanIfPresent(this.optimizeStepsExperimental, builder::optimizeStepsExperimental);
        OptionConversion.applyBooleanIfPresent(this.optimizeParentPointers, builder::optimizeParentPointers);

        return builder.build();
    }
}
