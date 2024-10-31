(:JIQS: ShouldRun; Output="({ "label" : 0, "name" : "a", "age" : 20, "weight" : 50, "prediction" : 0 }, { "label" : 1, "name" : "b", "age" : 21, "weight" : 55.3, "prediction" : 0 }, { "label" : 2, "name" : "c", "age" : 22, "weight" : 60.6, "prediction" : 0 }, { "label" : 3, "name" : "d", "age" : 23, "weight" : 65.9, "prediction" : 0 }, { "label" : 4, "name" : "e", "age" : 24, "weight" : 70.3, "prediction" : 1 }, { "label" : 5, "name" : "f", "age" : 25, "weight" : 75.6, "prediction" : 1 })" :)
let $data := annotate(
    json-file("../../../../queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)
let $vector-assembler := get-transformer("VectorAssembler")
let $data := $vector-assembler($data, {"inputCols" : [ "age", "weight" ], "outputCol" : "features" })

let $est := get-estimator("GaussianMixture")
let $tra := $est(
    $data,
    { }
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
    "prediction": $result.prediction
}
