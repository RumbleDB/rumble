(:JIQS: ShouldRun; Output="({ "label" : 0, "stringCol" : "i am data entry 1", "output" : [ "i", "am", "data", "entry", "1" ] }, { "label" : 1, "stringCol" : "i am data entry 2", "output" : [ "i", "am", "data", "entry", "2" ] }, { "label" : 2, "stringCol" : "i am data entry 3", "output" : [ "i", "am", "data", "entry", "3" ] }, { "label" : 3, "stringCol" : "i am data entry 4", "output" : [ "i", "am", "data", "entry", "4" ] }, { "label" : 4, "stringCol" : "i am data entry 5", "output" : [ "i", "am", "data", "entry", "5" ] }, { "label" : 5, "stringCol" : "i am data entry 6", "output" : [ "i", "am", "data", "entry", "6" ] })" :)
let $data := annotate(
    json-file("../../../../queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)

let $transformer := get-transformer("RegexTokenizer")
for $result in $transformer(
    $data,
    {"inputCol": "stringCol", "outputCol": "output", "pattern": "\\W"}
)
return {
    "label": $result.label,
    "stringCol": $result.stringCol,
    "output": $result.output
}
