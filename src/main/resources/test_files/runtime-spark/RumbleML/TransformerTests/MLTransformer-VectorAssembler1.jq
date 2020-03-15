(:JIQS: ShouldRun; Output="({ "binary_label" : null, "name" : "a", "age" : 20, "weight" : 50, "features" : [ 20, 50 ] }, { "binary_label" : null, "name" : "b", "age" : 21, "weight" : 55.3, "features" : [ 21, 55.3 ] }, { "binary_label" : null, "name" : "c", "age" : 22, "weight" : 60.6, "features" : [ 22, 60.6 ] }, { "binary_label" : null, "name" : "d", "age" : 23, "weight" : 65.9, "features" : [ 23, 65.9 ] }, { "binary_label" : null, "name" : "e", "age" : 24, "weight" : 70.3, "features" : [ 24, 70.3 ] }, { "binary_label" : null, "name" : "f", "age" : 25, "weight" : 75.6, "features" : [ 25, 75.6 ] })" :)
let $data := annotate(
    json-file("./src/main/resources/queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)

let $transformer := get-transformer("VectorAssembler")
for $result in $transformer(
  $data,
  {"inputCols": ["age", "weight"], "outputCol": "features"}
)
return {
    "binary_label": $result.binary_label,
    "name": $result.name,
    "age": $result.age,
    "weight": $result.weight,
    "features": $result.features
}

