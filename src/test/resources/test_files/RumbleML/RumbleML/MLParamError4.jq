(:JIQS: ShouldCrash; ErrorCode="RBML0003"; :)
let $local-data := (
    {"id": 0, "col1": 6.0},
    {"id": 1, "col1": 3.0},
    {"id": 2, "col1": 4.0},
    {"id": 3, "col1": 4.5},
    {"id": 4, "col1": 5.3},
    {"id": 5, "col1": 9.2}
)
let $df-data := annotate($local-data, {"id": "integer", "col1": "decimal"})

let $est := get-estimator("KMeans")
let $tra := $est(
    $df-data,
    {"featuresCol": ["col1"]}
)

for $i in $tra(
    $df-data,
    { }
)
return $i

(: estimator and the transformer generated from it(the fitted model) have individual parameters :)
(: the transformer defaults to 'features' value for 'featuresCol' param, which is not found in the input data :)
