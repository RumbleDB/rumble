(:JIQS: ShouldRun; Output="(false, false, true, true)" :)
deep-equal(csv-file("./src/main/resources/queries/cities.csv"), json-file("./src/main/resources/queries/conf-ex.json", 10)),
deep-equal(json-file("./src/main/resources/queries/conf-ex.json", 10), csv-file("./src/main/resources/queries/cities.csv")),
deep-equal(json-file("./src/main/resources/queries/conf-ex.json", 4), json-file("./src/main/resources/queries/conf-ex.json", 5)),
deep-equal(json-file("./src/main/resources/queries/conf-ex.json", 5), json-file("./src/main/resources/queries/conf-ex.json", 4))

(: parallelized inputs with varying partition sizes :)
