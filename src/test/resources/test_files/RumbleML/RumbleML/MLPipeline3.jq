(:JIQS: ShouldRun; Output="({ "id" : 0, "label" : 1, "col1" : -1, "col2" : 1.5, "col3" : 1.3, "features" : [ -1, 1.5, 1.3 ], "features2" : [ 0.6811443247067493, -1.6574062675838126 ], "rawPrediction" : [ -37.806767087841095, 37.806767087841095 ], "probability" : [ 3.80828704311553E-17, 1 ], "ocol1" : 1 }, { "id" : 1, "label" : 0, "col1" : 3, "col2" : 2, "col3" : -0.1, "features" : [ 3, 2, -0.1 ], "features2" : [ -2.9389857055027706, 0.48441694826197546 ], "rawPrediction" : [ 37.56770709258579, -37.56770709258579 ], "probability" : [ 1, 0 ], "ocol1" : 0 }, { "id" : 2, "label" : 1, "col1" : 0, "col2" : 2.2, "col3" : -1.5, "features" : [ 0, 2.2, -1.5 ], "features2" : [ 0.28609394877170796, 1.1822492465709793 ], "rawPrediction" : [ -19.168353441006694, 19.168353441006694 ], "probability" : [ 4.734671708106025E-9, 0.9999999952653283 ], "ocol1" : 1 })" :)

declare type local:mytype as {
 "id": "integer", "label": "integer", "col1": "decimal", "col2": "decimal", "col3": "decimal"
};

let $vector-assembler := get-transformer("VectorAssembler")(?, { "inputCols" : [ "col1", "col2", "col3" ], "outputCol" : "features" })
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
let $my-training-data := validate type local:mytype* { $training-data }
let $my-training-data := $vector-assembler($my-training-data)
let $my-test-data := validate type local:mytype* {$test-data }
let $my-test-data := $vector-assembler($my-test-data)

let $est := get-estimator("PCA")
let $trained_est := $est($my-training-data,
                 {"inputCol": "features", "outputCol": ["ocol1", "ocol2", "ocol2"], "k":2})
let $my-new-training-data := $trained_est($my-training-data, {"inputCol": "features", "outputCol": "features2"})

let $est2 := get-estimator("LogisticRegression")
let $trained_est2 := $est2($my-new-training-data, {"featuresCol": "features2"})
let $my-new-test-data := $trained_est($my-test-data, {"inputCol": "features", "outputCol": "features2"})
for $i in $trained_est2(
    $my-new-test-data,
    {"featuresCol": "features2", "predictionCol": "ocol1"}
)
return $i