(:JIQS: ShouldRun; Output="(true, false, false, false, false, true, false, true)" :)
deep-equal(parallelize((1)), parallelize((1))),
deep-equal(parallelize((1)), parallelize((1, 2))),
deep-equal(parallelize((1, 2)), parallelize((1))),
deep-equal(parallelize(()), parallelize((1, 2))),
deep-equal(parallelize((1, 2)), parallelize(())),
deep-equal(parallelize((1, 1, 1, 1, 1)), parallelize((1, 1, 1, 1, 1))),
deep-equal(parallelize((5, 4, 3, 2, 1)), parallelize((1, 2, 3, 4, 5))),
deep-equal(csv-file("../../../../queries/cities.csv"), csv-file("../../../../queries/cities.csv"))

(: general parallelized inputs :)
