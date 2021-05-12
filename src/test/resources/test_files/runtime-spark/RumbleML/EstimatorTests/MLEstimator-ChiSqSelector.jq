(:JIQS: ShouldRun; Output="({ "label" : 0, "name" : "a", "age" : 20, "weight" : 50, "selectedFeatures" : [ 20 ] }, { "label" : 1, "name" : "b", "age" : 21, "weight" : 55.3, "selectedFeatures" : [ 21 ] }, { "label" : 2, "name" : "c", "age" : 22, "weight" : 60.6, "selectedFeatures" : [ 22 ] }, { "label" : 3, "name" : "d", "age" : 23, "weight" : 65.9, "selectedFeatures" : [ 23 ] }, { "label" : 4, "name" : "e", "age" : 24, "weight" : 70.3, "selectedFeatures" : [ 24 ] }, { "label" : 5, "name" : "f", "age" : 25, "weight" : 75.6, "selectedFeatures" : [ 25 ] })" :)
let $data := annotate(
    json-file("../../../../queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)

let $est := get-estimator("ChiSqSelector")
let $tra := $est(
    $data,
    { "featuresCol": ["age", "weight"], "numTopFeatures": 1, "outputCol": "selectedFeatures"}
)
for $result in $tra(
    $data,
    { "featuresCol": ["age", "weight"] }
)
return {
    "label": $result.label,
    "name": $result.name,
    "age": $result.age,
    "weight": $result.weight,
    "selectedFeatures": $result.selectedFeatures
}
