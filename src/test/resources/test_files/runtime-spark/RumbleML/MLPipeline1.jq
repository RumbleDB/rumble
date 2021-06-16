(:JIQS: ShouldRun; Output="({ "label" : 0, "prediction" : 0 }, { "label" : 1, "prediction" : 1 }, { "label" : 1, "prediction" : 1 }, { "label" : 1, "prediction" : 1 }, { "label" : 1, "prediction" : 1 })" :)
let $vector-assembler := get-transformer("VectorAssembler")
let $training-data := (
    {"id": 0, "label": 1, "col1": 0.0, "col2": 1.1, "col3": 0.1},
    {"id": 1, "label": 0, "col1": 2.0, "col2": 1.0, "col3": -1.0},
    {"id": 2, "label": 0, "col1": 2.0, "col2": 1.3, "col3": 1.0},
    {"id": 3, "label": 1, "col1": 0.0, "col2": 1.2, "col3": -0.5}
)
let $test-data := (
    {"id": 0, "label": 1, "col1": -1.0, "col2": 1.5, "col3": 1.3},
    {"id": 1, "label": 0, "col1": 3.0, "col2": 2.0, "col3": -0.1},
    {"id": 2, "label": 1, "col1": 0.0, "col2": 2.2, "col3": -1.5}
)
let $my-training-data := annotate($training-data, {"id": "integer", "label": "integer", "col1": "decimal", "col2": "decimal", "col3": "decimal"})
let $my-test-data := annotate($test-data, {"id": "integer", "label": "integer", "col1": "decimal", "col2": "decimal", "col3": "decimal"})
let $est := get-estimator("PCA")
let $my-training-data := $vector-assembler($my-training-data, { "inputCols" : [ "col1", "col2", "col3" ], "outputCol" : "features" })
let $trained_est := $est($my-training-data,
                 {"inputCol": "features", "outputCol": ["ocol1", "ocol2", "ocol2"], "k":2})
let $my-new-training-data := $trained_est($my-training-data, {"inputCol": "features", "outputCol": "features2"})
let $est2 := get-estimator("LogisticRegression")
let $trained_est2 := $est2($my-new-training-data, {"featuresCol": "features2"})
let $my-test-data := $vector-assembler($my-test-data, { "inputCols" : [ "col1", "col2", "col3" ], "outputCol" : "features" })
let $my-new-test-data := $trained_est($my-test-data, {"inputCol": "features", "outputCol": "features2"})
for $i in $trained_est2(
    $my-new-test-data,
    {"featuresCol": "features2", "predictionCol": "ocol1"}
)
return $i