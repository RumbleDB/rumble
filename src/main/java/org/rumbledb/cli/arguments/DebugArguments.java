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

import org.rumbledb.config.model.DebugConfig;

public final class DebugArguments {
    @Option(
            names = "--print-iterator-tree",
            negatable = true,
            description = "For debugging purposes, prints out the expression tree and runtime interator tree.")
    private Boolean printIteratorTree;

    @Option(
            names = {"-v", "--show-error-info"},
            negatable = true,
            description = {
                "For debugging purposes.",
                "If you want to report a bug, you can use this to get the full exception stack."
            })
    private Boolean showErrorInfo;

    @Option(names = "--debug", negatable = true, description = "Enables debug output.")
    private Boolean logging;

    @Option(
            names = "--log-level",
            paramLabel = "level",
            description =
                    "Sets the diagnostic logging level. Valid values: off, fatal, error, warn, info, debug, trace, all.")
    private String logLevel;

    @Option(
            names = "--spark-log-level",
            paramLabel = "level",
            description =
                    "Sets the Spark logging level. Valid values: off, fatal, error, warn, info, debug, trace, all.")
    private String sparkLogLevel;

    public DebugConfig toConfig() {
        DebugConfig.DebugConfigBuilder builder = DebugConfig.builder();

        OptionConversion.applyBooleanIfPresent(this.printIteratorTree, builder::printIteratorTree);
        OptionConversion.applyBooleanIfPresent(this.showErrorInfo, builder::showErrorInfo);
        OptionConversion.applyBooleanIfPresent(this.logging, builder::logging);
        OptionConversion.applyIfPresent(this.logLevel, builder::logLevel);
        OptionConversion.applyIfPresent(this.sparkLogLevel, builder::sparkLogLevel);

        return builder.build();
    }
}
