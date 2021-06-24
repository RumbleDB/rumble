(:JIQS: ShouldRun; Output="({ "id" : 0, "label" : 1, "col2" : 1.5, "col3" : 1.3, "col1" : -1, "features" : [ -1, 1.5, 1.3 ], "rawPrediction" : [ -43.28021983334889, 43.28021983334889 ], "probability" : [ 1.5982302663265492E-19, 1 ], "prediction" : 1 }, { "id" : 1, "label" : 0, "col2" : 2, "col3" : -0.1, "col1" : 3, "features" : [ 3, 2, -0.1 ], "rawPrediction" : [ 27.129563756689066, -27.129563756689066 ], "probability" : [ 0.9999999999983489, 1.6511258015816568E-12 ], "prediction" : 0 }, { "id" : 2, "label" : 1, "col2" : 2.2, "col3" : -1.5, "col1" : 0, "features" : [ 0, 2.2, -1.5 ], "rawPrediction" : [ -41.54445354973831, 41.54445354973831 ], "probability" : [ 9.067197376998538E-19, 1 ], "prediction" : 1 })" :)
declare type local:mytype as  {"id": "integer", "label": "integer", "col1": "decimal", "col2": "decimal", "col3": "decimal"};

let $pipeline := get-estimator("Pipeline", {
  "stages" : [
    get-transformer("VectorAssembler", {"inputCols" : [ "col1", "col2", "col3" ], "outputCol" : "features"}),
    get-estimator("LogisticRegression", { "featuresCol" : "features" })
  ]
})

let $training-data := validate type local:mytype* {
    {"id": 0, "label": 1, "col1": 0.0, "col2": 1.1, "col3": 0.1},
    {"id": 1, "label": 0, "col1": 2.0, "col2": 1.0, "col3": -1.0},
    {"id": 2, "label": 0, "col1": 2.0, "col2": 1.3, "col3": 1.0},
    {"id": 3, "label": 1, "col1": 0.0, "col2": 1.2, "col3": -0.5}
}
let $test-data := validate type local:mytype* {
    {"id": 0, "label": 1, "col1": -1.0, "col2": 1.5, "col3": 1.3},
    {"id": 1, "label": 0, "col1": 3.0, "col2": 2.0, "col3": -0.1},
    {"id": 2, "label": 1, "col1": 0.0, "col2": 2.2, "col3": -1.5}
}

let $pip := $pipeline($training-data, {})
return $pip($test-data, {})
