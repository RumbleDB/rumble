(:JIQS: ShouldRun; Output="({ "label" : 0, "age" : 20, "weight" : 50, "output" : [ 50 ] }, { "label" : 1, "age" : 21, "weight" : 55.3, "output" : [ 55.3 ] }, { "label" : 2, "age" : 22, "weight" : 60.6, "output" : [ 60.6 ] }, { "label" : 3, "age" : 23, "weight" : 65.9, "output" : [ 65.9 ] }, { "label" : 4, "age" : 24, "weight" : 70.3, "output" : [ 70.3 ] }, { "label" : 5, "age" : 25, "weight" : 75.6, "output" : [ 75.6 ] })" :)
let $data := annotate(
    json-file("../../../../queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)
let $vector-assembler := get-transformer("VectorAssembler")
let $data := $vector-assembler($data, {"inputCols" : [ "age", "weight" ], "outputCol" : "features" })

let $transformer := get-transformer("VectorSlicer")
for $result in $transformer(
    $data,
    {"inputCol": "features", "outputCol": "output", "indices": [ 1 ] }
)
return {
    "label": $result.label,
    "age": $result.age,
    "weight": $result.weight,
    "output": $result.output
}
