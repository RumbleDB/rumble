package org.rumbledb.cli.options;

import java.util.HashMap;
import java.util.Map;

import org.rumbledb.config.OutputOptions;

import picocli.CommandLine.Option;

public final class Output {
    @Option(
        names = { "-o", "--output-path" },
        paramLabel = "path",
        description = {
            "Where to output to.",
            "If the output is large, it will create a sharded directory, otherwise it will create a file."
        }
    )
    private String outputPath;

    @Option(
        names = { "-f", "--output-format" },
        paramLabel = "format",
        description = {
            "An output format to use for the output.",
            "Formats other than json can only be output if the query outputs a highly structured sequence of objects."
        }
    )
    private String outputFormat;

    @Option(names = "--log-path", paramLabel = "path", description = "Where to output log information.")
    private String logPath;

    @Option(
        names = { "-O", "--overwrite" },
        negatable = true,
        description = "Whether to overwrite to --output-path. No throws an error if the output file/folder exists."
    )
    private boolean overwrite;

    @Option(
        names = { "-P", "--number-of-output-partitions" },
        paramLabel = "count",
        description = "How many partitions to create in the output, i.e., the number of files that will be created in the output path directory."
    )
    private Integer numberOfOutputPartitions;

    @Option(
        names = "--shell-filter",
        paramLabel = "command",
        description = "Post-processes the output of JSONiq queries on the shell with the specified command."
    )
    private String shellFilter;

    @Option(names = "-o:", paramLabel = "name=value")
    private Map<String, String> shortSerializationParameters = new HashMap<>();

    @Option(
        names = "--output-format-option",
        paramLabel = "name=value",
        description = "Options to further specify the output format, for example a separator character for CSV or a compression format."
    )
    private Map<String, String> outputFormatOptions = new HashMap<>();

    public OutputOptions toOutputOptions() {
        OutputOptions.OutputOptionsBuilder builder = OutputOptions.builder().overwrite(this.overwrite);
        OptionConversion.applyIfPresent(this.outputPath, builder::outputPath);
        OptionConversion.applyIfPresent(this.outputFormat, builder::outputFormat);
        OptionConversion.applyIfPresent(this.logPath, builder::logPath);
        OptionConversion.applyIfPresent(this.numberOfOutputPartitions, builder::numberOfOutputPartitions);
        OptionConversion.applyIfPresent(this.shellFilter, builder::shell);

        Map<String, String> serializationParameters = new HashMap<>();
        serializationParameters.putAll(this.shortSerializationParameters);
        serializationParameters.putAll(this.outputFormatOptions);
        if (!serializationParameters.isEmpty()) {
            builder.outputFormatOptions(serializationParameters);
        }
        return builder.build();
    }
}
