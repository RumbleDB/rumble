(:JIQS: ShouldRun; Output="({ "label" : 0, "name" : "a", "age" : 20, "weight" : 50, "topicDistribution" : [ 0.9915007293228202, 0.0084992706771797 ] }, { "label" : 1, "name" : "b", "age" : 21, "weight" : 55.3, "topicDistribution" : [ 0.9922903440390959, 0.007709655960904113 ] }, { "label" : 2, "name" : "c", "age" : 22, "weight" : 60.6, "topicDistribution" : [ 0.9929414141611251, 0.007058585838874805 ] }, { "label" : 3, "name" : "d", "age" : 23, "weight" : 65.9, "topicDistribution" : [ 0.9934942226012154, 0.006505777398784658 ] }, { "label" : 4, "name" : "e", "age" : 24, "weight" : 70.3, "topicDistribution" : [ 0.9938860832566909, 0.006113916743309047 ] }, { "label" : 5, "name" : "f", "age" : 25, "weight" : 75.6, "topicDistribution" : [ 0.9943034888595204, 0.005696511140479687 ] })" :)
let $data := annotate(
    json-file("../../../../queries/rumbleML/sample-ml-data-flat.json"),
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
    "topicDistribution": $result.topicDistribution
}
