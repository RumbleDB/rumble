(:JIQS: ShouldRun; Output="({ "label" : 0, "name" : "a", "prediction" : [ "2", "3", "4", "5", "6" ] }, { "label" : 1, "name" : "b", "prediction" : [ "1", "3", "4", "5", "6" ] }, { "label" : 2, "name" : "c", "prediction" : [ "1", "2", "4", "5", "6" ] }, { "label" : 3, "name" : "d", "prediction" : [ "1", "2", "3", "5", "6" ] }, { "label" : 4, "name" : "e", "prediction" : [ "1", "2", "3", "4", "6" ] }, { "label" : 5, "name" : "f", "prediction" : [ "1", "2", "3", "4", "5" ] })" :)
let $data := annotate(
    json-lines("../../../../queries/rumbleML/sample-ml-data-flat.json"),
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
    "prediction": [ for $item in $result.prediction[] order by $item return $item ]
}
