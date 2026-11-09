(:JIQS: ShouldRun; Output="({ "label" : 0, "name" : "a", "age" : 20, "weight" : 50, "prediction" : 0.3713413 }, { "label" : 1, "name" : "b", "age" : 21, "weight" : 55.3, "prediction" : 1.2326249 }, { "label" : 2, "name" : "c", "age" : 22, "weight" : 60.6, "prediction" : 2.0939085 }, { "label" : 3, "name" : "d", "age" : 23, "weight" : 65.9, "prediction" : 2.955192 }, { "label" : 4, "name" : "e", "age" : 24, "weight" : 70.3, "prediction" : 3.7428248 }, { "label" : 5, "name" : "f", "age" : 25, "weight" : 75.6, "prediction" : 4.6041083 })" :)
declare function local:round($i as double) as float {
  float($i)
};
let $data := annotate(
    json-lines("../../../../queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)
let $vector-assembler := get-transformer("VectorAssembler")
let $data := $vector-assembler($data, {"inputCols" : [ "age", "weight" ], "outputCol" : "features" })

let $est := get-estimator("LinearRegression")
let $tra := $est(
    $data,
    { "maxIter": 10, "regParam": 0.3, "elasticNetParam": 0.8}
)
for $result in $tra(
    $data,
    { }
)
return {
    "label": $result.label,
    "name": $result.name,
    "age": $result.age,
    "weight": $result.weight,
    "prediction": local:round($result.prediction)
}
