(:JIQS: ShouldRun; Output="(99, 2, 3, 4, 5, 1, 1)" :)
count(tail(parallelize(1 to 100))),
tail(parallelize(1 to 1)),
tail(parallelize((1, 2, 3, 4, 5))),
tail(parallelize((1, 1, 1))),
tail(parallelize(()))
