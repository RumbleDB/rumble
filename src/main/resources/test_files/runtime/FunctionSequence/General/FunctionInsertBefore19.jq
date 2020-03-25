(:JIQS: ShouldRun; Output="(a, b, c, y, z)" :)
insert-before(("a", "b", "c"), 4, parallelize(("y", "z")))

(: both inputs rdds :)
