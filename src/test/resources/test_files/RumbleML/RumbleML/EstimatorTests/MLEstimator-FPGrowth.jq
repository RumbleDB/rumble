(:JIQS: ShouldRun; Output="({ "label" : 0, "name" : "a", "prediction" : [ "4", "2", "3", "6", "5" ] }, { "label" : 1, "name" : "b", "prediction" : [ "4", "3", "6", "5", "1" ] }, { "label" : 2, "name" : "c", "prediction" : [ "4", "2", "6", "5", "1" ] }, { "label" : 3, "name" : "d", "prediction" : [ "2", "3", "6", "5", "1" ] }, { "label" : 4, "name" : "e", "prediction" : [ "4", "2", "3", "6", "1" ] }, { "label" : 5, "name" : "f", "prediction" : [ "4", "2", "3", "5", "1" ] })" :)let $data := annotate(
    json-file("../../../../queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)

let $est := get-estimator("FPGrowth")
let $tra := $est(
    $data,
    { "itemsCol": "stringArrayCol", "minSupport": 0.1, "minConfidence": 0.1}
)
for $result in $tra(
    $data,
    { "itemsCol": "stringArrayCol" }
)
return {
    "label": $result.label,
    "name": $result.name,
    "prediction": $result.prediction
}
