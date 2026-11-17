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

import org.rumbledb.config.model.AnalysisConfig;

public final class AnalysisArguments {
    @Option(
            names = {"-t", "--static-typing"},
            negatable = true,
            description = {
                "Activates static type analysis, which annotates the expression tree with inferred types at compile time.",
                "Enables more optimizations (experimental). Deactivated by default."
            })
    private Boolean enableStaticTyping;

    @Option(names = "--print-inferred-types", negatable = true, description = "Prints inferred types.")
    private Boolean printInferredTypes;

    @Option(
            names = "--check-return-types-of-builtin-functions",
            negatable = true,
            description = "Checks return types of built-in functions.")
    private Boolean checkReturnTypesOfBuiltinFunctions;

    public AnalysisConfig toConfig() {
        AnalysisConfig.AnalysisConfigBuilder builder = AnalysisConfig.builder();

        OptionConversion.applyBooleanIfPresent(this.enableStaticTyping, builder::enableStaticTyping);
        OptionConversion.applyBooleanIfPresent(this.printInferredTypes, builder::printInferredTypes);
        OptionConversion.applyBooleanIfPresent(
                this.checkReturnTypesOfBuiltinFunctions, builder::checkReturnTypeOfBuiltinFunctions);

        return builder.build();
    }
}
