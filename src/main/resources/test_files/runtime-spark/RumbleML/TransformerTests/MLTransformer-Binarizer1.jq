(:JIQS: ShouldRun; Output="({ "age" : 20, "id" : 1, "weight" : 68.8, "binarized_weight" : 0 }, { "age" : 35, "id" : 2, "weight" : 72.4, "binarized_weight" : 1 }, { "age" : 50, "id" : 3, "weight" : 76.3, "binarized_weight" : 1 })" :)
let $transformer := get-transformer("Binarizer")
return $transformer(
    (structured-json-file("./src/main/resources/queries/rumbleML/sample-ml-data-age-weight.json")),
    {"inputCol": "weight", "outputCol": "binarized_weight", "threshold": 70.0}
)
