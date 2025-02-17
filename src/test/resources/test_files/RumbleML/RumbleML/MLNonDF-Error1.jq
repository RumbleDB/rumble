(:JIQS: ShouldCrash; ErrorCode="RBML0004"; :)
let $estimator := get-estimator("KMeans")
let $rdd-data := json-lines("../../../queries/rumbleML/sample-ml-data-age-weight.json")
return ($estimator(
    $rdd-data,
    {
        "k": 2,
        "seed": 1
    }
))

(: estimators expect a dataframe as the input dataset :)
