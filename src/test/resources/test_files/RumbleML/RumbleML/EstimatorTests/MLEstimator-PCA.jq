(:JIQS: ShouldRun; Output="({ "label" : 0, "name" : "a", "age" : 20, "weight" : 50, "pca" : { } }, { "label" : 1, "name" : "b", "age" : 21, "weight" : 55.3, "pca" : [ 0.2, 0.20703124999999992 ] }, { "label" : 2, "name" : "c", "age" : 22, "weight" : 60.6, "pca" : [ 0.4, 0.4140625000000001 ] }, { "label" : 3, "name" : "d", "age" : 23, "weight" : 65.9, "pca" : [ 0.6000000000000001, 0.6210937500000003 ] }, { "label" : 4, "name" : "e", "age" : 24, "weight" : 70.3, "pca" : [ 0.8, 0.79296875 ] }, { "label" : 5, "name" : "f", "age" : 25, "weight" : 75.6, "pca" : [ 1, 1 ] })" :)
let $data := annotate(
    json-file("../../../../queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)
let $vector-assembler := get-transformer("VectorAssembler")
let $data := $vector-assembler($data, {"inputCols" : [ "age", "weight" ], "outputCol" : "features" })

let $est := get-estimator("MinMaxScaler")
let $tra := $est(
    $data,
    { "inputCol": "features", "outputCol": "pca" }
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
    "pca": $result.pca
}
