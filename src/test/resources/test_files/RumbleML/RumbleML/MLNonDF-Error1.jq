(:JIQS: ShouldCrash; ErrorCode="RBML0003"; :)
let $estimator := get-estimator("KMeans")
let $rdd-data := json-lines("../../../queries/rumbleML/sample-ml-data-age-weight.json")
return ($estimator(
    $rdd-data,
    {
        "k": 2,
        "seed": 1
    }
))

(: estimator input is now inferred into a dataframe, but KMeans still needs an explicit features column :)
