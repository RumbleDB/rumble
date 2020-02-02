(:JIQS: ShouldRun; Output="({ "label" : 0, "features" : [ 0, 0, 0 ], "prediction" : 0 }, { "label" : 1, "features" : [ 0.1, 0.1, 0.1 ], "prediction" : 0 }, { "label" : 2, "features" : [ 0.2, 0.2, 0.2 ], "prediction" : 0 }, { "label" : 3, "features" : [ 9, 9, 9 ], "prediction" : 1 }, { "label" : 4, "features" : [ 9.1, 9.1, 9.1 ], "prediction" : 1 }, { "label" : 5, "features" : [ 9.2, 9.2, 9.2 ], "prediction" : 1 })" :)
let $est := get-estimator("KMeans")
let $tra := $est(
    libsvm-file("./src/main/resources/queries/rumbleML/sample-libsvm-kmeans-data.txt"),
    {
        "k": 2,
        "seed": 1
    }
)
let $result := $tra(
    libsvm-file("./src/main/resources/queries/rumbleML/sample-libsvm-kmeans-data.txt"),
    { }
)
return $result
