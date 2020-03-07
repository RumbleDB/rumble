(:JIQS: ShouldRun; Output="({ "id" : 0, "col1" : 6, "binarized_col1" : 1, "prediction" : 1 }, { "id" : 1, "col1" : 3, "binarized_col1" : 0, "prediction" : 0 }, { "id" : 2, "col1" : 4, "binarized_col1" : 0, "prediction" : 0 }, { "id" : 3, "col1" : 4.5, "binarized_col1" : 0, "prediction" : 0 }, { "id" : 4, "col1" : 5.3, "binarized_col1" : 1, "prediction" : 1 }, { "id" : 5, "col1" : 9.2, "binarized_col1" : 1, "prediction" : 1 })" :)
let $local-data := (
    {"id": 0, "col1": 6.0},
    {"id": 1, "col1": 3.0},
    {"id": 2, "col1": 4.0},
    {"id": 3, "col1": 4.5},
    {"id": 4, "col1": 5.3},
    {"id": 5, "col1": 9.2}
)
let $df-data := annotate($local-data, {"id": "integer", "col1": "decimal"})

let $tokenizer := get-transformer("Binarizer")
let $intermediate-data := $tokenizer($df-data, {"inputCol": "col1", "outputCol": "binarized_col1", "threshold": 5.0})
let $df-intermediate-data := annotate($intermediate-data, {"id": "integer", "col1": "double", "binarized_col1": "double"})

let $est := get-estimator("KMeans")
let $tra := $est(
    $df-intermediate-data,
    {"featuresCol": ["binarized_col1"]}
)

for $i in $tra(
    $df-intermediate-data,
    {"featuresCol": ["binarized_col1"]}
)
return $i

(: process data by chaining a transformer followed by a full estimator execution :)
