(:JIQS: ShouldRun; Output="(1, 2, 3, 4)" :)
subsequence(insert-before(parallelize(1 to 10000, 10), 200, parallelize(1 to 5000)), 200, 4)

(: large parallelized inputs :)
