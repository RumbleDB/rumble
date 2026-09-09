# Compiler organization

`CompilerPipeline` is the compilation entry point. `frontend.ModuleSourceReader`
loads source through the configured resource resolver. `frontend.ModuleParser`
translates source into an AST; its main-module result records the selected language
so the shared pipeline can apply the appropriate conditional checks. `backend.RuntimePlanBuilder`
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

`ModuleSource` carries source text, source URI, and static base URI separately.
Diagnostics and extension-based language selection use the source URI. Relative
references use the static base URI, which may be overridden by configuration or a
base-URI declaration without changing the diagnostic source identity. In-memory
queries without an explicit source URI initially use the resolved base URI for both.

`ModuleImportLoader` loads and parses imported libraries, resolves their variable
dependencies, and validates their namespaces. Static contexts and types are processed
later through the importing main module. `compileLibraryModuleFromQuery`, used for
standalone library analysis, resolves dependencies, populates static contexts, and
infers types without running the main-module pipeline. `ModuleParser` itself does
not run dependency analysis.

`VariableDependenciesVisitor.visitProlog` checks and orders declarations in the
current prolog; it does not traverse imported modules. Each imported library therefore
needs its own dependency-analysis pass during loading. The main-module pass does not
replace these calls. Import dependency checks also run before namespace validation
and before duplicate-import pruning. Standalone compilation explicitly analyzes the
standalone library's declarations after its imports have been prepared.

Execution-mode inference retains the local-only path and the initial, intermediate,
and final passes for parallel execution, including the existing fallback for unresolved
function modes. AST execution-mode and storage-mode accessors return their stored
annotations, including `UNSET`. `ExecutionModeVisitor` applies the current pass's
`VisitorConfig` when reading those annotations; runtime-plan generation requires
resolved execution modes. Function-mode registration also belongs to the execution-mode
visitor. AST classes no longer depend on `VisitorConfig`.

Runtime-context construction still lives on expressions, statements, and clauses for
now. The backend passes an already validated execution mode to those methods.

The language-specific pass differences and repeated analyses are intentional in this
structural refactor; changing them requires a separate semantic change.
