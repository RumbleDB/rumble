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

import org.rumbledb.config.model.OutputConfig;
import org.rumbledb.serialization.SerializationParameterUtils;

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
                "Output file format (json, csv, parquet, avro, or another Spark format).",
                "Spark file formats, including json, require --output-path.",
                "Use serialize to write the whole sequence as one string using the serialization method.",
                "Use serialize-each-item to serialize items independently, separated by newlines.",
                "Default: serialize-each-item for JSONiq; serialize for XQuery.",
                "Other formats except json require a DataFrame-compatible sequence."
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
                    "Options to further specify the output format, for example a separator character for CSV or a compression format.")
    private Map<String, String> outputFormatOptions;

    public OutputConfig toConfig(String queryLanguage) {
        OutputConfig.OutputConfigBuilder builder = OutputConfig.builder();

        OptionConversion.applyBooleanIfPresent(this.overwrite, builder::allowOverwrite);
        OptionConversion.applyIfPresent(this.outputPath, builder::outputPath);
        OptionConversion.applyIfPresent(this.outputFormat, builder::outputFormat);
        OptionConversion.applyIfPresent(this.logPath, builder::logPath);
        OptionConversion.applyIntIfPresent(this.numberOfOutputPartitions, builder::numberOfOutputPartitions);
        OptionConversion.applyIfPresent(this.shellFilter, builder::shellFilter);
        OptionConversion.applyIfPresent(
                this.outputFormatOptions,
                options -> builder.serializationParameters(
                        SerializationParameterUtils.buildFromConfig(options, queryLanguage)));

        return builder.build();
    }
}
