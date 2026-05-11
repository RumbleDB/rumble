(:JIQS: ShouldRun; Output="({ "id" : 1, "age" : 20, "weight" : 68.8, "binarized_weight" : 0, "binarized_weight2" : 0 }, { "id" : 2, "age" : 35, "weight" : 72.4, "binarized_weight" : 1, "binarized_weight2" : 0 }, { "id" : 3, "age" : 50, "weight" : 76.3, "binarized_weight" : 1, "binarized_weight2" : 0 })" :)
let $data := structured-json-lines("../../../../queries/rumbleML/sample-ml-data-age-weight.json")

let $binarizer := get-transformer("Binarizer")
let $intermediate-data := $binarizer($data, {"inputCol": "weight", "outputCol": "binarized_weight", "threshold": 70.0})

let $df-intermediate-data := annotate($intermediate-data, {"id": "integer", "age": "integer", "weight": "double", "binarized_weight": "double"})

for $i in $binarizer(
    $df-intermediate-data,
    {"inputCol": "binarized_weight", "outputCol": "binarized_weight2", "threshold": 2.0}
)
return $i

(: Re-using the same transformer and modifying the data multiple times:)
