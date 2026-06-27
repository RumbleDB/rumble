package org.rumbledb.cli.options;

import org.rumbledb.config.DebugOptions;

import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

public final class Debug {
    @Option(
        names = "--print-iterator-tree",
        scope = ScopeType.INHERIT,
        negatable = true,
        description = "For debugging purposes, prints out the expression tree and runtime interator tree."
    )
    private boolean printIteratorTree;

    @Option(
        names = { "-v", "--show-error-info" },
        scope = ScopeType.INHERIT,
        negatable = true,
        description = {
            "For debugging purposes.",
            "If you want to report a bug, you can use this to get the full exception stack."
        }
    )
    private boolean showErrorInfo;

    @Option(
        names = "--debug",
        scope = ScopeType.INHERIT,
        negatable = true,
        description = "Enables debug output."
    )
    private boolean logging;

    public DebugOptions toDebugOptions() {
        return DebugOptions.builder()
            .printIteratorTree(this.printIteratorTree)
            .showErrorInfo(this.showErrorInfo)
            .logging(this.logging)
            .build();
    }
}
