(:JIQS: ShouldRun; Output="({ "label" : 0, "prediction" : 0 }, { "label" : 1, "prediction" : 1 }, { "label" : 2, "prediction" : 2 }, { "label" : 3, "prediction" : 3 }, { "label" : 4, "prediction" : 4 }, { "label" : 5, "prediction" : 5 })" :)
let $data := annotate(
    json-file("./src/main/resources/queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "name": "string", "age": "double", "weight": "double", "sentence": "string", "null": "null", "array": ["double"] }
)

let $est := get-estimator("LogisticRegression")
let $tra := $est(
    $data,
    { "featuresCol": ["age", "weight"] }
)
for $result in $tra(
    $data,
    { "featuresCol": ["age", "weight"] }
)
return {
    "label": $result.label,
    "prediction": $result.prediction
}

