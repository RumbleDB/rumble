(:JIQS: ShouldRun; Output="({ "label" : 0, "name" : "a", "age" : 20, "weight" : 50, "scaled" : [ 0.8, 0.6613756613756614 ] }, { "label" : 1, "name" : "b", "age" : 21, "weight" : 55.3, "scaled" : [ 0.84, 0.7314814814814815 ] }, { "label" : 2, "name" : "c", "age" : 22, "weight" : 60.6, "scaled" : [ 0.88, 0.8015873015873016 ] }, { "label" : 3, "name" : "d", "age" : 23, "weight" : 65.9, "scaled" : [ 0.92, 0.8716931216931219 ] }, { "label" : 4, "name" : "e", "age" : 24, "weight" : 70.3, "scaled" : [ 0.96, 0.9298941798941799 ] }, { "label" : 5, "name" : "f", "age" : 25, "weight" : 75.6, "scaled" : [ 1, 1 ] })" :)
let $data := annotate(
    json-lines("../../../../queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)
let $vector-assembler := get-transformer("VectorAssembler")
let $data := $vector-assembler($data, {"inputCols" : [ "age", "weight" ], "outputCol" : "features" })

let $est := get-estimator("MaxAbsScaler")
let $tra := $est(
    $data,
    { "inputCol": "features", "outputCol": "scaled" }
)
for $result in $tra(
    $data,
    { "inputCol": "features" }
)
return {
    "label": $result.label,
    "name": $result.name,
    "age": $result.age,
    "weight": $result.weight,
    "scaled": $result.scaled
}
