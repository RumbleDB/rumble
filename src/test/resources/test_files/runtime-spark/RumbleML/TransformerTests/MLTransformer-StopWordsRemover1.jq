(:JIQS: ShouldRun; Output="({ "label" : 0, "stringArrayCol" : [ "i", "am", "data", "entry", "1" ], "output" : [ "data", "entry", "1" ] }, { "label" : 1, "stringArrayCol" : [ "i", "am", "data", "entry", "2" ], "output" : [ "data", "entry", "2" ] }, { "label" : 2, "stringArrayCol" : [ "i", "am", "data", "entry", "3" ], "output" : [ "data", "entry", "3" ] }, { "label" : 3, "stringArrayCol" : [ "i", "am", "data", "entry", "4" ], "output" : [ "data", "entry", "4" ] }, { "label" : 4, "stringArrayCol" : [ "i", "am", "data", "entry", "5" ], "output" : [ "data", "entry", "5" ] }, { "label" : 5, "stringArrayCol" : [ "i", "am", "data", "entry", "6" ], "output" : [ "data", "entry", "6" ] })" :)
let $data := annotate(
    json-file("./src/test/resources/test_data/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)

let $transformer := get-transformer("StopWordsRemover")
for $result in $transformer(
    $data,
    {"inputCol": "stringArrayCol", "outputCol": "output"}
)
return {
    "label": $result.label,
    "stringArrayCol": $result.stringArrayCol,
    "output": $result.output
}
