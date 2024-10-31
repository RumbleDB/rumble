(:JIQS: ShouldRun; Output="({ "label" : 0, "name" : "a", "age" : 20, "weight" : 50, "scaled" : [ 10.690449676496975, 5.244335309537817 ] }, { "label" : 1, "name" : "b", "age" : 21, "weight" : 55.3, "scaled" : [ 11.224972160321824, 5.800234852348826 ] }, { "label" : 2, "name" : "c", "age" : 22, "weight" : 60.6, "scaled" : [ 11.759494644146674, 6.3561343951598355 ] }, { "label" : 3, "name" : "d", "age" : 23, "weight" : 65.9, "scaled" : [ 12.294017127971522, 6.912033937970844 ] }, { "label" : 4, "name" : "e", "age" : 24, "weight" : 70.3, "scaled" : [ 12.828539611796371, 7.373535445210171 ] }, { "label" : 5, "name" : "f", "age" : 25, "weight" : 75.6, "scaled" : [ 13.36306209562122, 7.929434988021179 ] })" :)
let $data := annotate(
    json-file("../../../../queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)
let $vector-assembler := get-transformer("VectorAssembler")
let $data := $vector-assembler($data, {"inputCols" : [ "age", "weight" ], "outputCol" : "features" })

let $est := get-estimator("StandardScaler")
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
