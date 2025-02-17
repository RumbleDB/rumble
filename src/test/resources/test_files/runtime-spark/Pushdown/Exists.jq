(:JIQS: ShouldRun; Output="(false, true, true)" :)
exists(parallelize(())),
exists(parallelize(1 to 100000)),
exists(parallelize(("a", 2, [3, 4])))
