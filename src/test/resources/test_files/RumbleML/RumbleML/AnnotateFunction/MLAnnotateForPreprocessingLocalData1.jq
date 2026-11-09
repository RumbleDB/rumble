(:JIQS: ShouldRun; Output="({ "label" : 0, "name" : "a", "age" : 20, "weight" : 50, "sentence" : "i am data entry 1", "null" : null, "array" : [ 1, 2, 3 ], "binarized_age" : 0 }, { "label" : 1, "name" : "b", "age" : 21, "weight" : 55.3, "sentence" : "i am data entry 2", "null" : null, "array" : [ 4, 5, 6 ], "binarized_age" : 0 }, { "label" : 2, "name" : "c", "age" : 22, "weight" : 60.6, "sentence" : "i am data entry 3", "null" : null, "array" : [ 7, 8, 9 ], "binarized_age" : 0 }, { "label" : 3, "name" : "d", "age" : 23, "weight" : 65.9, "sentence" : "i am data entry 4", "null" : null, "array" : [ 1, 4, 7 ], "binarized_age" : 1 }, { "label" : 4, "name" : "e", "age" : 24, "weight" : 70.3, "sentence" : "i am data entry 5", "null" : null, "array" : [ 2, 5, 8 ], "binarized_age" : 1 }, { "label" : 5, "name" : "f", "age" : 25, "weight" : 75.6, "sentence" : "i am data entry 6", "null" : null, "array" : [ 3, 6, 9 ], "binarized_age" : 1 })" :)
let $data := (
    let $local := (
        {"label": 0, "data": {"name": "a", "age":  20, "weight": 50.0, "sentence": "i am data entry 1", "null": null, "array": [1,2,3]}},
        {"label": 1, "data": {"name": "b", "age":  21, "weight": 55.3, "sentence": "i am data entry 2", "null": null, "array": [4,5,6]}},
        {"label": 2, "data": {"name": "c", "age":  22, "weight": 60.6, "sentence": "i am data entry 3", "null": null, "array": [7,8,9]}},
        {"label": 3, "data": {"name": "d", "age":  23, "weight": 65.9, "sentence": "i am data entry 4", "null": null, "array": [1,4,7]}},
        {"label": 4, "data": {"name": "e", "age":  24, "weight": 70.3, "sentence": "i am data entry 5", "null": null, "array": [2,5,8]}},
        {"label": 5, "data": {"name": "f", "age":  25, "weight": 75.6, "sentence": "i am data entry 6", "null": null, "array": [3,6,9]}}
    )
    for $entry in $local
    let $edited := {
        "label": $entry.label,
        "name": $entry.data.name,
        "age": $entry.data.age,
        "weight": $entry.data.weight,
        "sentence": $entry.data.sentence,
        "null": $entry.data."null",
        "array": $entry.data."array"
    }
    return $edited
)

let $df := annotate(
    $data,
    {
        "label": "integer",
        "name": "string",
        "age": "double",
        "weight": "double",
        "sentence": "string",
        "null": "null",
        "array": ["double"]
    }
)

let $transformer := get-transformer("Binarizer")
return $transformer(
    $df,
    {"inputCol": "age", "outputCol": "binarized_age", "threshold": 22.0}
)
