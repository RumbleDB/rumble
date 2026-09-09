# Compiler organization

`CompilerPipeline` is the compilation entry point. `frontend.ModuleParser` loads
and translates source into an AST; its main-module result records the selected
language so compilation uses the corresponding pass sequence. `backend.RuntimePlanBuilder`
creates executable plans and dynamic contexts. `VisitorHelpers` retains its public
signatures as a compatibility facade.

Packages group the implementation by responsibility:

- `frontend`: language translation, imports, and literal/constructor processing.
- `analysis`: static context, types, variable dependencies, classifications, and composability.
- `rewriting`: shared cloning and expression normalization.
- `optimization`: pruning, function optimizations, and projection pushdown with their supporting analyses.
- `backend`: execution-mode inference, runtime plans, dynamic contexts, and effective configuration.

## Preserved pass order

JSONiq main modules:

1. Prune modules.
2. Resolve variable dependencies.
3. Classify sequential expressions.
4. Rewrite builtin partial applications.
5. Identify recursive functions.
6. Inline functions (if enabled).
7. Optimize tail calls (if enabled).
8. Push down projections.
9. Populate static contexts.
10. Classify expressions.
11. Check composability.
12. Infer types.
13. Rewrite comparisons.
14. Infer execution modes.
15. Classify expressions again.

XQuery main modules:

1. Prune modules.
2. Resolve variable dependencies.
3. Rewrite builtin partial applications.
4. Identify recursive functions.
5. Inline functions (if enabled).
6. Optimize tail calls (if enabled).
7. Push down projections.
8. Populate static contexts.
9. Infer types.
10. Rewrite comparisons.
11. Infer execution modes.

Imported library modules receive variable dependency resolution during loading.
Static contexts and types are processed later through the importing main module.
`compileLibraryModuleFromQuery`, used for standalone library analysis, additionally
populates static contexts and infers types without running the main-module pipeline.

Execution-mode inference retains the local-only path and the initial, intermediate,
and final passes for parallel execution, including the existing fallback for unresolved
function modes. `VisitorConfig` remains at the root because AST classes also use it.

The language-specific pass differences and repeated analyses are intentional in this
structural refactor; changing them requires a separate semantic change.
