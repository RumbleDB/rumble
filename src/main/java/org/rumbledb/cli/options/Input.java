package org.rumbledb.cli.options;

import org.rumbledb.config.InputOptions;

import picocli.CommandLine.Option;

public final class Input {
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

    public InputOptions toInputOptions(String positionalQueryPath) {
        InputOptions.InputOptionsBuilder builder = InputOptions.builder();
        OptionConversion.applyIfPresent(
            this.queryPath != null ? this.queryPath : positionalQueryPath,
            builder::queryPath
        );
        OptionConversion.applyIfPresent(this.query, builder::query);
        return builder.build();
    }
}
