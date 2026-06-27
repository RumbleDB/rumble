package org.rumbledb.cli.options;

import org.rumbledb.config.RuntimeLimits;

import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

public final class Limits {
    @Option(
        names = "--result-size",
        scope = ScopeType.INHERIT,
        paramLabel = "count",
        description = "A cap on the maximum number of items to output on the screen or to a local list."
    )
    private Integer resultSize = RuntimeLimits.DEFAULT_RESULTS_SIZE_CAP;

    @Option(
        names = { "-c", "--materialization-cap" },
        scope = ScopeType.INHERIT,
        paramLabel = "count",
        description = "A cap on the maximum number of items to materialize during the query execution for large sequences within a query."
    )
    private Integer materializationCap = RuntimeLimits.DEFAULT_MATERIALIZATION_CAP;

    public RuntimeLimits toRuntimeLimits() {
        RuntimeLimits.RuntimeLimitsBuilder builder = RuntimeLimits.builder();

        OptionConversion.applyIfPresent(this.resultSize, builder::resultsSizeCap);
        OptionConversion.applyIfPresent(this.materializationCap, builder::materializationCap);
        
        return builder.build();
    }
}
