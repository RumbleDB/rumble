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

    @Option(names = "--input-format", paramLabel = "format")
    private String inputFormat;

    public InputOptions toInputOptions() {
        return InputOptions.builder()
            .inputFormat(this.inputFormat)
            .queryPath(this.queryPath)
            .query(this.query)
            .build();
    }
}
