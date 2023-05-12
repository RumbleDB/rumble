(:JIQS: ShouldRun; Output="({ "label" : 0, "age" : 20, "weight" : 50, "output" : [ 0.2857142857142857, 0.7142857142857143 ] }, { "label" : 1, "age" : 21, "weight" : 55.3, "output" : [ 0.27522935779816515, 0.7247706422018348 ] }, { "label" : 2, "age" : 22, "weight" : 60.6, "output" : [ 0.26634382566585957, 0.7336561743341405 ] }, { "label" : 3, "age" : 23, "weight" : 65.9, "output" : [ 0.2587176602924634, 0.7412823397075365 ] }, { "label" : 4, "age" : 24, "weight" : 70.3, "output" : [ 0.25450689289501593, 0.7454931071049841 ] }, { "label" : 5, "age" : 25, "weight" : 75.6, "output" : [ 0.2485089463220676, 0.7514910536779323 ] })" :)
let $data := annotate(
    json-file("../../../../queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)
let $vector-assembler := get-transformer("VectorAssembler")
let $data := $vector-assembler($data, {"inputCols" : [ "age", "weight" ], "outputCol" : "features" })

let $transformer := get-transformer("Normalizer")
for $result in $transformer(
    $data,
    {"inputCol": "features", "outputCol": "output", "p": 1.0}
)
return {
    "label": $result.label,
    "age": $result.age,
    "weight": $result.weight,
    "output": $result.output
}
