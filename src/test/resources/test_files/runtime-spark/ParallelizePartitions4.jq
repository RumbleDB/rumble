(:JIQS: ShouldRun; Output="(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)" :)
parallelize(parallelize(1 to 12, 4), 3)
