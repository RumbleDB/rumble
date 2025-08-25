(:JIQS: ShouldRun; Output="({ "label" : 0, "name" : "a", "age" : 20, "weight" : 50, "topicDistribution" : [ 0.99150074, 0.00849927 ] }, { "label" : 1, "name" : "b", "age" : 21, "weight" : 55.3, "topicDistribution" : [ 0.9922903, 0.007709656 ] }, { "label" : 2, "name" : "c", "age" : 22, "weight" : 60.6, "topicDistribution" : [ 0.99294144, 0.007058586 ] }, { "label" : 3, "name" : "d", "age" : 23, "weight" : 65.9, "topicDistribution" : [ 0.9934942, 0.0065057776 ] }, { "label" : 4, "name" : "e", "age" : 24, "weight" : 70.3, "topicDistribution" : [ 0.99388605, 0.0061139166 ] }, { "label" : 5, "name" : "f", "age" : 25, "weight" : 75.6, "topicDistribution" : [ 0.99430346, 0.0056965114 ] })" :)


let $data := annotate(
    json-lines("../../../../queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)
let $vector-assembler := get-transformer("VectorAssembler")
let $data := $vector-assembler($data, {"inputCols" : [ "age", "weight" ], "outputCol" : "features" })

let $est := get-estimator("LDA")
let $tra := $est(
    $data,
    { "k": 2, "maxIter": 10}
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
    "topicDistribution": [ for $v in $result.topicDistribution[] return float($v) ]
}
