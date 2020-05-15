(:JIQS: ShouldRun; Output="({ "label" : 0, "name" : "a", "age" : 20, "weight" : 50, "prediction" : 0.18303000252344503 }, { "label" : 1, "name" : "b", "age" : 21, "weight" : 55.3, "prediction" : 1.1206157752236088 }, { "label" : 2, "name" : "c", "age" : 22, "weight" : 60.6, "prediction" : 2.058201547923769 }, { "label" : 3, "name" : "d", "age" : 23, "weight" : 65.9, "prediction" : 2.995787320623929 }, { "label" : 4, "name" : "e", "age" : 24, "weight" : 70.3, "prediction" : 3.852389790502542 }, { "label" : 5, "name" : "f", "age" : 25, "weight" : 75.6, "prediction" : 4.789975563202702 })" :)
let $data := annotate(
    json-file("./src/test/resources/test_data/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)

let $est := get-estimator("GeneralizedLinearRegression")
let $tra := $est(
    $data,
    { "family": "gaussian", "link": "identity", "featuresCol": ["age", "weight"], "maxIter": 10, "regParam": 0.3 }
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
    "prediction": $result.prediction
}
