(:JIQS: ShouldRun; Output="({ "binaryLabel" : 0, "name" : "a", "age" : 20, "weight" : 50, "dct_output" : [ 49.49747468305832, -21.213203435596427 ] }, { "binaryLabel" : 0, "name" : "b", "age" : 21, "weight" : 55.3, "dct_output" : [ 53.95224740453357, -24.25376259469858 ] }, { "binaryLabel" : 0, "name" : "c", "age" : 22, "weight" : 60.6, "dct_output" : [ 58.40702012600882, -27.294321753800737 ] }, { "binaryLabel" : 1, "name" : "d", "age" : 23, "weight" : 65.9, "dct_output" : [ 62.861792847484075, -30.334880912902896 ] }, { "binaryLabel" : 1, "name" : "e", "age" : 24, "weight" : 70.3, "dct_output" : [ 66.68016946589142, -32.73904396893715 ] }, { "binaryLabel" : 1, "name" : "f", "age" : 25, "weight" : 75.6, "dct_output" : [ 71.13494218736668, -35.779603128039305 ] })" :)
let $data := annotate(
    json-file("../../../../queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)
let $vector-assembler := get-transformer("VectorAssembler")
let $data := $vector-assembler($data, {"inputCols" : [ "age", "weight" ], "outputCol" : "features" })

let $transformer := get-transformer("DCT")
for $result in $transformer(
    $data,
    { "inputCol": "features" "outputCol": "dct_output", "inverse": false}
)

return {
    "binaryLabel": $result.binaryLabel,
    "name": $result.name,
    "age": $result.age,
    "weight": $result.weight,
    "dct_output": $result.dct_output
}
