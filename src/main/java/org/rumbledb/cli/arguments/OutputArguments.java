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

import java.util.Map;

import picocli.CommandLine.Option;

import org.rumbledb.config.SerializationParameterBuilder;
import org.rumbledb.config.model.OutputConfig;
import org.rumbledb.serialization.SerializationParameters;

public final class OutputArguments {
    @Option(
            names = {"-o", "--output-path"},
            paramLabel = "path",
            description = {
                "Where to output to.",
                "If the output is large, it will create a sharded directory, otherwise it will create a file."
            })
    private String outputPath;

    @Option(
            names = {"-f", "--output-format"},
            paramLabel = "format",
            description = {
                "Use serialize (the default) for sequence serialization, or a Spark data-source format such as json, csv, or parquet.",
                "With serialize, select the serialization method using --output-format-option method=xml (or json, text, adaptive, etc.)."
            })
    private String outputFormat;

    @Option(names = "--log-path", paramLabel = "path", description = "Where to output log information.")
    private String logPath;

    @Option(
            names = {"-O", "--overwrite"},
            negatable = true,
            description = "Whether to overwrite to --output-path. No throws an error if the output file/folder exists.")
    private Boolean overwrite;

    @Option(
            names = {"-P", "--number-of-output-partitions"},
            paramLabel = "count",
            description =
                    "How many partitions to create in the output, i.e., the number of files that will be created in the output path directory.")
    private Integer numberOfOutputPartitions;

    @Option(
            names = "--shell-filter",
            paramLabel = "command",
            description = "Post-processes the output of JSONiq queries on the shell with the specified command.")
    private String shellFilter;

    @Option(
            names = "--output-format-option",
            paramLabel = "name=value",
            description =
                    "Serialization parameters for serialize (method=xml, indent=yes, item-separator=...), or Spark writer options for other formats.")
    private Map<String, String> outputFormatOptions;

    public OutputConfig toConfig(String queryLanguage) {
        OutputConfig.OutputConfigBuilder builder = OutputConfig.builder();

        OptionConversion.applyBooleanIfPresent(this.overwrite, builder::allowOverwrite);
        OptionConversion.applyIfPresent(this.outputPath, builder::outputPath);
        builder.outputFormat(this.outputFormat == null ? "serialize" : this.outputFormat);
        OptionConversion.applyIfPresent(this.logPath, builder::logPath);
        OptionConversion.applyIntIfPresent(this.numberOfOutputPartitions, builder::numberOfOutputPartitions);
        OptionConversion.applyIfPresent(this.shellFilter, builder::shellFilter);
        OptionConversion.applyIfPresent(this.outputFormatOptions, options -> {
            if (this.outputFormat == null || this.outputFormat.equals("serialize")) {
                builder.serializationParameters(SerializationParameterBuilder.build(options, queryLanguage));
            } else {
                SerializationParameters parameters = SerializationParameters.defaults(queryLanguage);
                parameters.getSparkOptions().putAll(options);
                builder.serializationParameters(parameters);
            }
        });

        return builder.build();
    }
}
