(:JIQS: ShouldRun; Output="({ "binary_label" : null, "stringCol" : "i am data entry 1", "output" : [ "i", "am", "data", "entry", "1" ] }, { "binary_label" : null, "stringCol" : "i am data entry 2", "output" : [ "i", "am", "data", "entry", "2" ] }, { "binary_label" : null, "stringCol" : "i am data entry 3", "output" : [ "i", "am", "data", "entry", "3" ] }, { "binary_label" : null, "stringCol" : "i am data entry 4", "output" : [ "i", "am", "data", "entry", "4" ] }, { "binary_label" : null, "stringCol" : "i am data entry 5", "output" : [ "i", "am", "data", "entry", "5" ] }, { "binary_label" : null, "stringCol" : "i am data entry 6", "output" : [ "i", "am", "data", "entry", "6" ] })" :)
let $data := annotate(
    json-file("./src/main/resources/queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)

let $transformer := get-transformer("Tokenizer")
for $result in $transformer(
    $data,
    {"inputCol": "stringCol", "outputCol": "output"}
)
return {
    "binary_label": $result.binary_label,
    "stringCol": $result.stringCol,
    "output": $result.output
}

