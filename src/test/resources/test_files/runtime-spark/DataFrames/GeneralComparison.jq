(:JIQS: ShouldRun; Output="(true, false, true, false, true, false, false, false, true, false, true, false)" :)
parallelize(1 to 5) = parallelize(5 to 10),
parallelize(1 to 5) = parallelize(6 to 10),
parallelize(1 to 5) != parallelize(5 to 10),
parallelize((1, 1, 1, 1)) != parallelize((1, 1, 1, 1)),
parallelize(1 to 5) < parallelize(5 to 10),
parallelize(6 to 10) < parallelize(1 to 5),
parallelize(1 to 5) > parallelize(5 to 10),
parallelize(6 to 10) < parallelize(1 to 5),
parallelize(1 to 5) <= parallelize(5 to 10),
parallelize(6 to 10) <= parallelize(1 to 5),
parallelize(1 to 5) >= parallelize(5 to 10),
parallelize(6 to 10) <= parallelize(1 to 5)

