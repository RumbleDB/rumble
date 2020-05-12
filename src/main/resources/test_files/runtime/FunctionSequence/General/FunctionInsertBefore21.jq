(:JIQS: ShouldRun; Output="(a, b, c, y, z)" :)
insert-before(parallelize(("a", "b", "c"), 10), 432, ("y", "z"))

(: more partitions than elements :)
