(:JIQS: ShouldRun; Output="(1, single, 1, a)" :)
head(parallelize(())),
head(parallelize(1 to 1000000)),
head(parallelize(("single"))),
head(parallelize(1 to 5)),
head(parallelize(("a", "b", "c")))
