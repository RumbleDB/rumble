(:JIQS: ShouldRun; Output="({ "age" : 20, "id" : 1, "weight" : 68.8, "binarized_weight" : 0 }, { "age" : 35, "id" : 2, "weight" : 72.4, "binarized_weight" : 1 }, { "age" : 50, "id" : 3, "weight" : 76.3, "binarized_weight" : 1 })" :)
let $data := structured-json-file("../../../../queries/rumbleML/sample-ml-data-age-weight.json")

let $binarizer := get-transformer("Binarizer")
let $intermediate-data := $binarizer($data, {"inputCol": "weight", "outputCol": "binarized_weight", "threshold": 70.0})

let $df-intermediate-data := annotate($intermediate-data, {"id": "integer", "age": "integer", "weight": "double", "binarized_weight": "double"})
return $df-intermediate-data

(: calling annotate on the results of a transformer :)
