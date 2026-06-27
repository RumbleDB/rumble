package org.rumbledb.cli.options;

import org.rumbledb.config.OptimizationOptions;

import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

public final class Optimization {
    @Option(
        names = "--optimize-general-comparison-to-value-comparison",
        scope = ScopeType.INHERIT,
        negatable = true,
        defaultValue = "true",
        fallbackValue = "true",
        description = "Activates automatic conversion of general comparisons to value comparisons when applicable (activated by default)."
    )
    private boolean optimizeGeneralComparisonToValueComparison;

    @Option(
        names = "--optimize-steps",
        scope = ScopeType.INHERIT,
        negatable = true,
        defaultValue = "true",
        fallbackValue = "true",
        description = "Allows RumbleDB to optimize steps, might violate stability of document order (activated by default)."
    )
    private boolean optimizeSteps;

    @Option(
        names = "--optimize-steps-experimental",
        scope = ScopeType.INHERIT,
        negatable = true,
        description = "Experimentally optimizes steps more by skipping uniqueness and sorting in some cases. Correctness is not yet verified (disabled by default)."
    )
    private boolean optimizeStepsExperimental;

    @Option(
        names = "--optimize-parent-pointers",
        scope = ScopeType.INHERIT,
        negatable = true,
        defaultValue = "true",
        fallbackValue = "true",
        description = "Allows RumbleDB to remove parent pointers from items if no steps requiring parent pointers are detected statically (activated by default)."
    )
    private boolean optimizeParentPointers;

    public OptimizationOptions toOptimizationOptions() {
        return OptimizationOptions.builder()
            .optimizeGeneralComparisonToValueComparison(this.optimizeGeneralComparisonToValueComparison)
            .optimizeSteps(this.optimizeSteps)
            .optimizeStepsExperimental(this.optimizeStepsExperimental)
            .optimizeParentPointers(this.optimizeParentPointers)
            .build();
    }
}
