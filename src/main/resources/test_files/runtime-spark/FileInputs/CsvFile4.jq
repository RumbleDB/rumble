(:JIQS: ShouldRun; Output="true" :)
head(csv-file("./src/main/resources/queries/cities.csv", {header: true, "inferSchema": true}).LatD) instance of integer
