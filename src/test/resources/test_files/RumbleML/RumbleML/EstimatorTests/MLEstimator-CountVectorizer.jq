(:JIQS: ShouldRun; Output="({ "label" : 0, "stringArrayCol" : [ "i", "am", "data", "entry", "1" ], "features" : { "0" : 1, "1" : 1, "2" : 1 } }, { "label" : 1, "stringArrayCol" : [ "i", "am", "data", "entry", "2" ], "features" : { "0" : 1, "1" : 1, "2" : 1 } }, { "label" : 2, "stringArrayCol" : [ "i", "am", "data", "entry", "3" ], "features" : { "0" : 1, "1" : 1, "2" : 1 } }, { "label" : 3, "stringArrayCol" : [ "i", "am", "data", "entry", "4" ], "features" : { "0" : 1, "1" : 1, "2" : 1 } }, { "label" : 4, "stringArrayCol" : [ "i", "am", "data", "entry", "5" ], "features" : { "0" : 1, "1" : 1, "2" : 1 } }, { "label" : 5, "stringArrayCol" : [ "i", "am", "data", "entry", "6" ], "features" : { "0" : 1, "1" : 1, "2" : 1 } })" :)
let $data := annotate(
    json-file("../../../../queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)

let $est := get-estimator("CountVectorizer")
let $tra := $est(
    $data,
    { "inputCol": "stringArrayCol", "outputCol": "features", "vocabSize": 3, "minDF": 2}
)
for $result in $tra(
    $data,
    {}
)
return {
    "label": $result.label,
    "stringArrayCol": $result.stringArrayCol,
    "features": $result.features
}
