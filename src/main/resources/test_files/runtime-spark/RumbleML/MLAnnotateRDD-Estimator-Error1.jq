(:JIQS: ShouldCrash; ErrorCode="RBML0004"; :)
let $estimator := get-estimator("KMeans")
let $rdd-data := json-file("./src/main/resources/queries/rumbleML/sample-ml-age-weight-data.json")
return ($estimator(
    $rdd-data,
    {
        "k": 2,
        "seed": 1
    }
))
