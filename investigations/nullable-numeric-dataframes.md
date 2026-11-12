# Nullable numeric DataFrame schemas

The reported sequences failed because nullable numeric unions retained separate
numeric alternatives. Schema mapping selected a double column, while validation
could preserve an integer or decimal item; the writer then used a double getter.

DataFrame schema detection now joins the non-null numeric alternatives using
the existing common-supertype rules, then restores null as a separate member.
Thus integer + double + null becomes `numeric | null`, and integer + decimal +
null becomes `decimal | null`, regardless of input order or RDD partitioning.
Existing validation performs any conversion required by the physical column.
The numeric value getters in the row writer are unchanged.

The new DATAFRAME merge mode is used by runtime DataFrame schema detection and
ML DataFrame input inference. STRICT and LAX joins, including static inference,
are unchanged. Nested objects and arrays use the same schema merge rules.
Numeric unions and nullable numeric unions are recognized as DataFrame-compatible.

The six original queries are in
`src/test/resources/test_files/runtime-spark/NullableNumericDataFrames`.
`NullableNumericDataFrameTest` reads those files and verifies local conversion,
Parquet write/read, and RDD conversion. `DataFrameSchemaTypeTest` covers numeric
orderings and groupings, nested schemas, and unchanged static/LAX types.
