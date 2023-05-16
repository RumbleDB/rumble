(:JIQS: ShouldRun; Output="({ "label" : 0, "stringArrayCol" : [ "i", "am", "data", "entry", "1" ], "hashingtf" : { "2" : 1, "5" : 1, "11" : 1, "15" : 1, "16" : 1 } }, { "label" : 1, "stringArrayCol" : [ "i", "am", "data", "entry", "2" ], "hashingtf" : { "2" : 1, "5" : 1, "12" : 1, "15" : 1, "16" : 1 } }, { "label" : 2, "stringArrayCol" : [ "i", "am", "data", "entry", "3" ], "hashingtf" : { "2" : 1, "5" : 1, "6" : 1, "15" : 1, "16" : 1 } }, { "label" : 3, "stringArrayCol" : [ "i", "am", "data", "entry", "4" ], "hashingtf" : { "2" : 1, "5" : 1, "10" : 1, "15" : 1, "16" : 1 } }, { "label" : 4, "stringArrayCol" : [ "i", "am", "data", "entry", "5" ], "hashingtf" : { "1" : 1, "2" : 1, "5" : 1, "15" : 1, "16" : 1 } }, { "label" : 5, "stringArrayCol" : [ "i", "am", "data", "entry", "6" ], "hashingtf" : { "2" : 1, "5" : 1, "6" : 1, "15" : 1, "16" : 1 } })" :)
let $data := annotate(
    json-file("../../../../queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)

let $transformer := get-transformer("HashingTF")
for $result in $transformer(
    $data,
    {"inputCol": "stringArrayCol", "outputCol": "hashingtf", "numFeatures": 20}
)
return {
    "label": $result.label,
    "stringArrayCol": $result.stringArrayCol,
    "hashingtf": $result.hashingtf
}
