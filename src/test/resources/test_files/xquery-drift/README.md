# XQuery 3.1 drift tests

Small, self-contained queries that expose differences between RumbleDB and the
W3C XQuery 3.1 Recommendation, including its normative Functions and Operators
specification. These are project-authored regression cases, not W3C QT3 tests
or a complete conformance suite.

Run the dedicated category from the repository root:

```sh
mvn -Dtest=XQueryDriftTests test
```

The suite runs locally without initializing Spark. Each `.xq` file contains a
standard XQuery expression, a specification link, and a `JIQS` comment declaring
the expected result. Other XQuery processors can ignore these comments and
execute the query directly. Boolean checks return `true` for conforming behavior.

| Query | Expected | Observed on the initial master baseline |
| --- | --- | --- |
| `attribute-following-axis.xq` | `true` | `false` |
| `attribute-preceding-axis.xq` | `true` | `false` |
| `round-int-overflow.xq` | `2147483650` | `-2147483646` |
| `round-half-to-even-int-overflow.xq` | `2147483650` | `-2147483646` |
| `addition-signed-zero.xq` | `true` | `false` |
| `switch-atomization-before-cardinality.xq` | `true` | `XPTY0004` |

Initial baseline: master commit `5e8532499`, verified on 2026-11-18.
The tests assert specification-correct behavior and intentionally fail until the
corresponding implementation defects are fixed. Keep them as regression tests
after fixing the implementation. Add new standalone `.xq` files to this folder
to extend the category; the suite discovers them automatically.
