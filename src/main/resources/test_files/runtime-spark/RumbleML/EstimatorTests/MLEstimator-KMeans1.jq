(:JIQS: ShouldRun; Output="({ "label" : 0, "name" : "a", "age" : 20, "weight" : 50, "sentence" : "i am data entry 1", "null" : null, "array" : [ 1, 2, 3 ], "prediction" : 1 }, { "label" : 1, "name" : "b", "age" : 21, "weight" : 55.3, "sentence" : "i am data entry 2", "null" : null, "array" : [ 4, 5, 6 ], "prediction" : 1 }, { "label" : 2, "name" : "c", "age" : 22, "weight" : 60.6, "sentence" : "i am data entry 3", "null" : null, "array" : [ 7, 8, 9 ], "prediction" : 1 }, { "label" : 3, "name" : "d", "age" : 23, "weight" : 65.9, "sentence" : "i am data entry 4", "null" : null, "array" : [ 1, 4, 7 ], "prediction" : 0 }, { "label" : 4, "name" : "e", "age" : 24, "weight" : 70.3, "sentence" : "i am data entry 5", "null" : null, "array" : [ 2, 5, 8 ], "prediction" : 0 }, { "label" : 5, "name" : "f", "age" : 25, "weight" : 75.6, "sentence" : "i am data entry 6", "null" : null, "array" : [ 3, 6, 9 ], "prediction" : 0 })" :)
let $data := annotate(
    json-file("./src/main/resources/queries/rumbleML/sample-ml-data-flat.json"),
    { "label": "integer", "name": "string", "age": "double", "weight": "double", "sentence": "string", "null": "null", "array": ["double"] }
)

let $est := get-estimator("KMeans")
let $tra := $est(
    $data,
    { "k": 2, "seed": 1, "featuresCol": ["age", "weight"] }
)
let $result := $tra(
    $data,
    { "featuresCol": ["age", "weight"] }
)
return $result
