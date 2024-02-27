(:JIQS: ShouldRun; Output="({ "id" : 0, "label" : 1, "col1" : -1, "col2" : 1.5, "col3" : 1.3, "features" : [ -1, 1.5, 1.3 ], "prediction" : 1, "features2" : [ ], "rawPrediction" : [ -38.071373, 38.071373 ], "probability" : [ 2.922893E-17, 1 ] }, { "id" : 1, "label" : 0, "col1" : 3, "col2" : 2, "col3" : -0.1, "features" : [ 3, 2, -0.1 ], "prediction" : 0, "features2" : [ ], "rawPrediction" : [ 36.295826, -36.295826 ], "probability" : [ 1, 2.220446E-16 ] }, { "id" : 2, "label" : 1, "col1" : 0, "col2" : 2.2, "col3" : -1.5, "features" : [ 0, 2.2, -1.5 ], "prediction" : 1, "features2" : [ ], "rawPrediction" : [ -21.188417, 21.188417 ], "probability" : [ 6.2804023E-10, 1 ] })" :)
declare type local:mytype as  {"id": "integer", "label": "integer", "col1": "decimal", "col2": "decimal", "col3": "decimal"};

declare function local:round($i as object) as object {
  {|
    remove-keys($i, ("features2", "rawPrediction", "probability")),
    {
      "features2" : [ for $v in $i.features2[] return float($v) ],
      "rawPrediction" : [ for $v in $i.rawPrediction[] return float($v) ],
      "probability" : [ for $v in $i.probability[] return float($v) ]
    }
  |}
};

let $vector-assembler := get-transformer("VectorAssembler", {"inputCols" : [ "col1", "col2", "col3" ], "outputCol" : "features"})
let $logisticregression := get-estimator("LogisticRegression", { "featuresCol" : "features" })

let $pipeline := get-estimator("Pipeline", { "stages" : [ $vector-assembler, $logisticregression ] })


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
for $i in $pip($test-data, {})
return local:round($i)

