package org.rumbledb.cli.arguments;

import picocli.CommandLine.Option;

public final class DebugArguments {
    @Option(
        names = "--print-iterator-tree",
        negatable = true,
        description = "For debugging purposes, prints out the expression tree and runtime interator tree."
    )
    private Boolean printIteratorTree;

    @Option(
        names = { "-v", "--show-error-info" },
        negatable = true,
        description = {
            "For debugging purposes.",
            "If you want to report a bug, you can use this to get the full exception stack."
        }
    )
    private Boolean showErrorInfo;

    @Option(
        names = "--debug",
        negatable = true,
        description = "Enables debug output."
    )
    private Boolean logging;
}
