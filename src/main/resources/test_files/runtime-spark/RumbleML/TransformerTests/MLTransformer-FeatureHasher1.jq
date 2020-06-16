(:JIQS: ShouldRun; Output="({ "name" : "a", "age" : 20, "weight" : 50, "booleanCol" : false, "hashed" : { "34087" : 1, "68404" : 50, "121715" : 1, "147703" : 20 } }, { "name" : "b", "age" : 21, "weight" : 55.3, "booleanCol" : false, "hashed" : { "34087" : 1, "68404" : 55.3, "118564" : 1, "147703" : 21 } }, { "name" : "c", "age" : 22, "weight" : 60.6, "booleanCol" : false, "hashed" : { "34087" : 1, "61149" : 1, "68404" : 60.6, "147703" : 22 } }, { "name" : "d", "age" : 23, "weight" : 65.9, "booleanCol" : true, "hashed" : { "48961" : 1, "68404" : 65.9, "147703" : 23, "227025" : 1 } }, { "name" : "e", "age" : 24, "weight" : 70.3, "booleanCol" : true, "hashed" : { "68404" : 70.3, "147703" : 24, "171510" : 1, "227025" : 1 } }, { "name" : "f", "age" : 25, "weight" : 75.6, "booleanCol" : true, "hashed" : { "61092" : 1, "68404" : 75.6, "147703" : 25, "227025" : 1 } })" :)
let $data := annotate(
    json-file("../../../../queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)

let $transformer := get-transformer("FeatureHasher")
for $result in $transformer(
    $data,
    {"inputCols": ["name", "age", "weight", "booleanCol"], outputCol: "hashed"}
)
return {
    "name": $result.name,
    "age": $result.age,
    "weight": $result.weight,
    "booleanCol": $result.booleanCol,
    "hashed": $result.hashed
}
