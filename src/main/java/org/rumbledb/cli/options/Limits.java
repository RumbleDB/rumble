package org.rumbledb.cli.options;

import org.rumbledb.config.RuntimeLimits;

import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

public final class Limits {
    @Option(
        names = "--result-size",
        scope = ScopeType.INHERIT,
        paramLabel = "count",
        description = "A cap on the maximum number of items to output on the screen or to a local list.",
        defaultValue = RuntimeLimits.DEFAULT_RESULTS_SIZE_CAP + ""
    )
    private Integer resultSize = RuntimeLimits.DEFAULT_RESULTS_SIZE_CAP;

    @Option(
        names = { "-c", "--materialization-cap" },
        scope = ScopeType.INHERIT,
        paramLabel = "count",
        description = "A cap on the maximum number of items to materialize during the query execution for large sequences within a query.",
        defaultValue = RuntimeLimits.DEFAULT_MATERIALIZATION_CAP + ""
    )
    private Integer materializationCap = RuntimeLimits.DEFAULT_MATERIALIZATION_CAP;

    public RuntimeLimits toRuntimeLimits() {
        return RuntimeLimits.builder()
            .resultsSizeCap(this.resultSize)
            .materializationCap(this.materializationCap)
            .build();
    }
}
