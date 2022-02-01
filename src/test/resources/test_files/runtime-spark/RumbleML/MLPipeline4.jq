(:JIQS: ShouldRun; Output="({ "id" : 0, "label" : 1, "col1" : -1, "col2" : 1.5, "col3" : 1.3, "features" : [ -1, 1.5, 1.3 ], "rawPrediction" : [ -38.07137267677716, 38.07137267677716 ], "probability" : [ 2.922893072747922E-17, 1 ], "prediction" : 1 }, { "id" : 1, "label" : 0, "col1" : 3, "col2" : 2, "col3" : -0.1, "features" : [ 3, 2, -0.1 ], "rawPrediction" : [ 36.29582598188576, -36.29582598188576 ], "probability" : [ 0.9999999999999998, 2.220446049250313E-16 ], "prediction" : 0 }, { "id" : 2, "label" : 1, "col1" : 0, "col2" : 2.2, "col3" : -1.5, "features" : [ 0, 2.2, -1.5 ], "rawPrediction" : [ -21.188416917059495, 21.188416917059495 ], "probability" : [ 6.280402132407761E-10, 0.9999999993719598 ], "prediction" : 1 })" :)

declare type local:mytype as  {"id": "integer", "label": "integer", "col1": "decimal", "col2": "decimal", "col3": "decimal"};


declare function local:pipeline($trainingset as object*, $testset as object*) as object*
{
  let $logisticregression := get-estimator("LogisticRegression")
  let $model := $logisticregression($trainingset, {"featuresCol": "features"})
  return $model($testset, {"featuresCol": "features"})
};

let $vector-assembler := get-transformer("VectorAssembler")(?, {"inputCols" : [ "col1", "col2", "col3" ], "outputCol" : "features"})


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

let $pip := local:pipeline($training-data, ?)
return $pip($test-data)