(:JIQS: ShouldRun; Output="(false, false, true, true, false, true)" :)
deep-equal(csv-file("./src/test/resources/queries/cities.csv"), json-file("./src/main/resources/queries/conf-ex.json", 10)),
deep-equal(json-file("./src/test/resources/queries/conf-ex.json", 10), csv-file("./src/main/resources/queries/cities.csv")),
deep-equal(json-file("./src/test/resources/queries/conf-ex.json", 4), json-file("./src/main/resources/queries/conf-ex.json", 5)),
deep-equal(json-file("./src/test/resources/queries/conf-ex.json", 5), json-file("./src/main/resources/queries/conf-ex.json", 4)),
deep-equal(parallelize((1, 2, 3, 4, 5), 2), parallelize((1, 2, 3, 4, 5, 6), 2)),
deep-equal(parallelize(1 to 10000, 2), parallelize(1 to 10000, 20))

(: parallelized inputs with varying partition sizes :)
