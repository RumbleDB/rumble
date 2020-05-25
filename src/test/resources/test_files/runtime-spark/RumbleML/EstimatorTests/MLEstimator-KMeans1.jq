(:JIQS: ShouldRun; Output="({ "binaryLabel" : 0, "name" : "a", "age" : 20, "weight" : 50, "prediction" : 1 }, { "binaryLabel" : 0, "name" : "b", "age" : 21, "weight" : 55.3, "prediction" : 1 }, { "binaryLabel" : 0, "name" : "c", "age" : 22, "weight" : 60.6, "prediction" : 1 }, { "binaryLabel" : 1, "name" : "d", "age" : 23, "weight" : 65.9, "prediction" : 0 }, { "binaryLabel" : 1, "name" : "e", "age" : 24, "weight" : 70.3, "prediction" : 0 }, { "binaryLabel" : 1, "name" : "f", "age" : 25, "weight" : 75.6, "prediction" : 0 })" :)
let $data := annotate(
    json-file("./src/test/resources/test_data/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)

let $est := get-estimator("KMeans")
let $tra := $est(
    $data,
    { "k": 2, "seed": 1, "featuresCol": ["age", "weight"] }
)
for $result in $tra(
    $data,
    { "featuresCol": ["age", "weight"] }
)
return {
    "binaryLabel": $result.binaryLabel,
    "name": $result.name,
    "age": $result.age,
    "weight": $result.weight,
    "prediction": $result.prediction
}
