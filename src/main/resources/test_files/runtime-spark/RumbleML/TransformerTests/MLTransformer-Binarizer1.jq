(:JIQS: ShouldRun; Output="({ "binary_label" : null, "name" : "a", "age" : 20, "weight" : 50, "binarized_weight" : 0 }, { "binary_label" : null, "name" : "b", "age" : 21, "weight" : 55.3, "binarized_weight" : 0 }, { "binary_label" : null, "name" : "c", "age" : 22, "weight" : 60.6, "binarized_weight" : 0 }, { "binary_label" : null, "name" : "d", "age" : 23, "weight" : 65.9, "binarized_weight" : 0 }, { "binary_label" : null, "name" : "e", "age" : 24, "weight" : 70.3, "binarized_weight" : 1 }, { "binary_label" : null, "name" : "f", "age" : 25, "weight" : 75.6, "binarized_weight" : 1 })" :)
let $data := annotate(
    json-file("./src/main/resources/queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)

let $transformer := get-transformer("Binarizer")
for $result in $transformer(
    $data,
    {"inputCol": "weight", "outputCol": "binarized_weight", "threshold": 70.0}
)
return {
    "binary_label": $result.binary_label,
    "name": $result.name,
    "age": $result.age,
    "weight": $result.weight,
    "binarized_weight": $result.binarized_weight
}
