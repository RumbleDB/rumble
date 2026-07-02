package org.rumbledb.cli.arguments;

import org.rumbledb.config.model.InputConfig;

import picocli.CommandLine.Option;

public final class InputArguments {
    @Option(
        names = { "-q", "--query" },
        paramLabel = "query",
        description = "A JSONiq query directly provided as a string."
    )
    private String query;

    @Option(
        names = "--query-path",
        paramLabel = "path",
        description = "A JSONiq query file to read from (from any file system, even the Web!)."
    )
    private String queryPath;

    public InputConfig toConfig(String positionalQueryPath) {
        InputConfig.InputConfigBuilder builder = InputConfig.builder();
        OptionConversion.applyIfPresent(
            this.queryPath != null ? this.queryPath : positionalQueryPath,
            builder::queryPath
        );
        OptionConversion.applyIfPresent(this.query, builder::query);
        return builder.build();
    }
}
