package org.rumbledb.cli.arguments;

import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import org.rumbledb.config.model.InputConfig;

public final class InputArguments {
    @Option(
            names = {"-q", "--query"},
            paramLabel = "query",
            description = "A JSONiq query directly provided as a string.")
    private String query;

    @Option(
            names = "--query-path",
            paramLabel = "path",
            description = "A JSONiq query file to read from (from any file system, even the Web!).")
    private String queryPath;

    @Parameters(
            index = "0",
            arity = "0..1",
            paramLabel = "query-file",
            description = "A JSONiq query file to read from (from any file system, even the Web!).")
    private String positionalQueryPath;

    public InputConfig toConfig() {
        InputConfig.InputConfigBuilder builder = InputConfig.builder();
        OptionConversion.applyIfPresent(
                this.queryPath != null ? this.queryPath : this.positionalQueryPath, builder::queryPath);
        OptionConversion.applyIfPresent(this.query, builder::query);
        return builder.build();
    }
}
