# Type inference regressions

Run this focused annotation suite with `mvn -Dtest=iq.TypeInferenceTests test`.

The runner enables strict static typing for `strict/` and disables it for `runtime/`.
Both folders check materialized output. Query extensions select JSONiq or XQuery.
The runner allows up to 200 output items so grouped cases are not truncated.

Use `is statically` to check inferred item types and occurrence indicators, and
`fn:item-type` to check exact runtime atomic types. Several related operators can
share a positive test; failures have separate files because evaluation stops at
the first error. Cast failures use `try/catch` so they must compile successfully
and fail during evaluation.

Static typing exceptions use the existing `ShouldCrash` annotation convention.
The annotation harness currently categorizes these exceptions as Rumble errors,
rather than semantic compilation errors.

The suite covers union-aware comparisons, node comparison cardinality, ordinary
node casts, and RDD aggregate conversion and promotion. Scalar binary ordering
remains in `static-typing/comparison/comp11.jq` and `comp12.jq`; local aggregate
union inference remains in `UnionTypeInferenceTest`. Only the derived-string
regression checks both local and RDD execution because the shared runtime helper
must preserve that derived type in both modes. Exact internal cardinality algebra
remains in `SequenceCardinalityTest`.
Unnamed inferred unions cannot be written in `is statically`; their structure is
covered by `UnionTypeInferenceTest`, while these aggregate files check exact
runtime types and values. `EmptyRDD.xq` adds strict inference assertions for typed
empty RDDs to the existing runtime-only empty aggregate coverage.

Mixed atomic/node regressions cover function conversion, arithmetic, comparisons, string concatenation,
boolean conditions, and aggregate inference. Cast regressions also exercise nilled nodes and list-valued
typed values, because cardinality applies after atomization. Exact mixed-union members and singleton
FLWOR bindings are checked in `UnionTypeInferenceTest`; runtime operator behavior stays in annotation tests.
