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

    @Option(
        names = { "-I", "--context-item" },
        paramLabel = "value",
        description = {
            "Initializes the global context item $$ to the supplied value.",
            "The query must contain the corresponding global variable declaration, e.g. "
                + "\"declare context item external;\""
        }
    )
    private String contextItem;

    @Option(
        names = { "-i", "--context-item-input" },
        paramLabel = "path",
        description = "Reads the context item value from the standard input."
    )
    private String contextItemInput;

    @Option(
        names = "--context-item-input-format",
        paramLabel = "format",
        description = "Sets the input format to use for parsing the standard input (as text or as a serialized json value)."
    )
    private String contextItemInputFormat;

    public InputOptions toInputOptions() {
        return InputOptions.builder()
            .inputFormat(this.inputFormat)
            .queryPath(this.queryPath)
            .query(this.query)
            .build();
    }
}
