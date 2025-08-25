(:JIQS: ShouldRun; Output="({ "label" : 0, "name" : "a", "age" : 20, "weight" : 50, "features" : [ 20, 50 ] }, { "label" : 1, "name" : "b", "age" : 21, "weight" : 55.3, "features" : [ 21, 55.3 ] }, { "label" : 2, "name" : "c", "age" : 22, "weight" : 60.6, "features" : [ 22, 60.6 ] }, { "label" : 3, "name" : "d", "age" : 23, "weight" : 65.9, "features" : [ 23, 65.9 ] }, { "label" : 4, "name" : "e", "age" : 24, "weight" : 70.3, "features" : [ 24, 70.3 ] }, { "label" : 5, "name" : "f", "age" : 25, "weight" : 75.6, "features" : [ 25, 75.6 ] })" :)
let $data := annotate(
    json-lines("../../../../queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)

let $transformer := get-transformer("VectorAssembler")
for $result in $transformer(
  $data,
  {"inputCols": ["age", "weight"], "outputCol": "features"}
)
return {
    "label": $result.label,
    "name": $result.name,
    "age": $result.age,
    "weight": $result.weight,
    "features": $result.features
}
