(:JIQS: ShouldRun; Output="(hello, 5, 4, 3, 2, 1)" :)
reverse(parallelize(())),
reverse(parallelize(("hello"))),
reverse(parallelize(1 to 5))

(: parallelized inputs :)
