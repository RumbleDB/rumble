(:JIQS: ShouldRun; Output="(1, 1000, bar, foo, good, good)" :)
min(parallelize(1 to 1000)),
max(parallelize(1 to 1000)),
min(parallelize(("foo", "bar"))),
max(parallelize(("foo", "bar"))),
try { min(parallelize((1, "foo"))) } catch XPTY0004 { "good" },
try { max(parallelize((1, "foo"))) } catch XPTY0004 { "good" }
