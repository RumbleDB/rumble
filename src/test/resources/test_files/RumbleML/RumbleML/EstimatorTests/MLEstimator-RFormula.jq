(:JIQS: ShouldRun; Output="({ "label" : 0, "name" : "a", "age" : 20, "weight" : 50, "output_label" : null, "output_features" : [ 20, 50 ] }, { "label" : 1, "name" : "b", "age" : 21, "weight" : 55.3, "output_label" : null, "output_features" : [ 21, 55.3 ] }, { "label" : 2, "name" : "c", "age" : 22, "weight" : 60.6, "output_label" : null, "output_features" : [ 22, 60.6 ] }, { "label" : 3, "name" : "d", "age" : 23, "weight" : 65.9, "output_label" : null, "output_features" : [ 23, 65.9 ] }, { "label" : 4, "name" : "e", "age" : 24, "weight" : 70.3, "output_label" : null, "output_features" : [ 24, 70.3 ] }, { "label" : 5, "name" : "f", "age" : 25, "weight" : 75.6, "output_label" : null, "output_features" : [ 25, 75.6 ] })" :)
let $data := annotate(
    json-lines("../../../../queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)

let $est := get-estimator("RFormula")
let $tra := $est(
    $data,
    { "labelCol": "output_label", "featuresCol": "output_features", "formula": "y ~ age + weight"}
)
for $result in $tra(
    $data,
    { }
)
return {
    "label": $result.label,
    "name": $result.name,
    "age": $result.age,
    "weight": $result.weight,
    "output_label": $result.output_label,
    "output_features": $result."output_features"
}

(: RFormula writes its output to the columns specified by featuresCol and labelCol :)
