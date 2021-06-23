(:JIQS: ShouldRun; Output="({ "id" : 0, "label" : 1, "col1" : -1, "col2" : 1.5, "col3" : 1.3, "features" : [ -1, 1.5, 1.3 ], "features2" : [ 0.6811443247067492, -1.6574062675838126 ], "rawPrediction" : [ -37.01954042109449, 37.01954042109449 ], "probability" : [ 8.367926801087183E-17, 1 ], "ocol1" : 1 }, { "id" : 1, "label" : 0, "col1" : 3, "col2" : 2, "col3" : -0.1, "features" : [ 3, 2, -0.1 ], "features2" : [ -2.93898570550277, 0.48441694826197534 ], "rawPrediction" : [ 37.84452856407813, -37.84452856407813 ], "probability" : [ 1, 3.667161819961595E-17 ], "ocol1" : 0 }, { "id" : 2, "label" : 1, "col1" : 0, "col2" : 2.2, "col3" : -1.5, "features" : [ 0, 2.2, -1.5 ], "features2" : [ 0.28609394877170796, 1.1822492465709793 ], "rawPrediction" : [ -18.536609873254328, 18.536609873254328 ], "probability" : [ 8.905383262038758E-9, 0.9999999910946168 ], "ocol1" : 1 })" :)
declare type local:mytype as  {"id": "integer", "label": "integer", "col1": "decimal", "col2": "decimal", "col3": "decimal"};

let $vector-assembler := get-transformer("VectorAssembler", {"inputCols" : [ "col1", "col2", "col3" ], "outputCol" : "features"})
let $logisticregression := get-estimator("LogisticRegression", { "featuresCol" : "features"})

let $pipeline := get-estimator("Pipeline", { "stages" : [ $vector-assembler, $logisticregression ] })


let $training-data := $vector-assembler(validate type local:mytype* {
    {"id": 0, "label": 1, "col1": 0.0, "col2": 1.1, "col3": 0.1},
    {"id": 1, "label": 0, "col1": 2.0, "col2": 1.0, "col3": -1.0},
    {"id": 2, "label": 0, "col1": 2.0, "col2": 1.3, "col3": 1.0},
    {"id": 3, "label": 1, "col1": 0.0, "col2": 1.2, "col3": -0.5}
})
let $test-data := $vector-assembler(validate type local:mytype* {
    {"id": 0, "label": 1, "col1": -1.0, "col2": 1.5, "col3": 1.3},
    {"id": 1, "label": 0, "col1": 3.0, "col2": 2.0, "col3": -0.1},
    {"id": 2, "label": 1, "col1": 0.0, "col2": 2.2, "col3": -1.5}
})

let $pip := $pipeline($training-data, ?)
return $pip($test-data)
