(:JIQS: ShouldRun; Output="({ "label" : 0, "age" : 20, "weight" : 50, "output" : [ 20, 400, 8000, 50, 1000, 20000, 2500, 50000, 125000 ] }, { "label" : 1, "age" : 21, "weight" : 55.3, "output" : [ 21, 441, 9261, 55.3, 1161.3, 24387.3, 3058.0899999999997, 64219.88999999999, 169112.37699999998 ] }, { "label" : 2, "age" : 22, "weight" : 60.6, "output" : [ 22, 484, 10648, 60.6, 1333.2, 29330.4, 3672.36, 80791.92, 222545.016 ] }, { "label" : 3, "age" : 23, "weight" : 65.9, "output" : [ 23, 529, 12167, 65.9, 1515.7, 34861.1, 4342.81, 99884.63, 286191.17900000006 ] }, { "label" : 4, "age" : 24, "weight" : 70.3, "output" : [ 24, 576, 13824, 70.3, 1687.1999999999998, 40492.799999999996, 4942.089999999999, 118610.15999999997, 347428.9269999999 ] }, { "label" : 5, "age" : 25, "weight" : 75.6, "output" : [ 25, 625, 15625, 75.6, 1889.9999999999998, 47249.99999999999, 5715.359999999999, 142883.99999999997, 432081.2159999999 ] })" :)
let $data := annotate(
    json-file("../../../../queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "binaryLabel": "integer", "name": "string", "age": "double", "weight": "double", "booleanCol": "boolean", "nullCol": "null", "stringCol": "string", "stringArrayCol": ["string"], "intArrayCol": ["integer"],  "doubleArrayCol": ["double"],  "doubleArrayArrayCol": [["double"]] }
)
let $vector-assembler := get-transformer("VectorAssembler")
let $data := $vector-assembler($data, {"inputCols" : [ "age", "weight" ], "outputCol" : "features" })

let $transformer := get-transformer("PolynomialExpansion")
for $result in $transformer(
    $data,
    {"inputCol": "features", "outputCol": "output", "degree": 3}
)
return {
    "label": $result.label,
    "age": $result.age,
    "weight": $result.weight,
    "output": $result.output
}
