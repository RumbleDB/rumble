(:JIQS: ShouldRun; Output="({ "label" : 0, "name" : "a", "age" : 20, "weight" : 50, "hashes" : [ [ 26 ], [ 19 ], [ -26 ] ] }, { "label" : 1, "name" : "b", "age" : 21, "weight" : 55.3, "hashes" : [ [ 29 ], [ 21 ], [ -29 ] ] }, { "label" : 2, "name" : "c", "age" : 22, "weight" : 60.6, "hashes" : [ [ 31 ], [ 23 ], [ -31 ] ] }, { "label" : 3, "name" : "d", "age" : 23, "weight" : 65.9, "hashes" : [ [ 34 ], [ 26 ], [ -34 ] ] }, { "label" : 4, "name" : "e", "age" : 24, "weight" : 70.3, "hashes" : [ [ 36 ], [ 28 ], [ -36 ] ] }, { "label" : 5, "name" : "f", "age" : 25, "weight" : 75.6, "hashes" : [ [ 39 ], [ 30 ], [ -38 ] ] })" :)
let $data := annotate(
    json-lines("../../../../queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)
let $vector-assembler := get-transformer("VectorAssembler")
let $data := $vector-assembler($data, {"inputCols" : [ "age", "weight" ], "outputCol" : "features" })

let $est := get-estimator("BucketedRandomProjectionLSH")
let $tra := $est(
    $data,
    { "inputCol": "features", "bucketLength": 2.0, "numHashTables": 3, "outputCol": "hashes" }
)
for $result in $tra(
    $data,
    { "inputCol": "features"  }
)
return {
    "label": $result.label,
    "name": $result.name,
    "age": $result.age,
    "weight": $result.weight,
    "hashes": $result.hashes
}
