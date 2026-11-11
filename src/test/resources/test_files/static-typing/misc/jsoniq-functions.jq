(:JIQS: ShouldRun :)
(: members returns the members of arrays, and project and remove-keys return one object per object. :)
members([1, 2]) is statically integer*,
jn:members([1]) is statically integer*,
project({"a": 1, "b": 2}, "a") is statically object,
remove-keys(({"a": 1}, {"a": 2}), "a") is statically object+,
(: The rules apply whether a function is called in the default function namespace or in the jn namespace. :)
size([1, 2]) is statically integer,
jn:size([1, 2]) is statically integer,
parquet-file("../../../queries/sample-json.snappy.parquet").int64 is statically integer*,
jn:parquet-file("../../../queries/sample-json.snappy.parquet").int64 is statically integer*
