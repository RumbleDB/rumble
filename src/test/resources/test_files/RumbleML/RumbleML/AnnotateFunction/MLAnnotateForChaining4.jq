(:JIQS: ShouldRun; Output="({ "id" : 1, "age" : 20, "weight" : 68.8, "binarized_weight" : 0, "features" : [ 0 ], "prediction" : 1 }, { "id" : 2, "age" : 35, "weight" : 72.4, "binarized_weight" : 1, "features" : [ 1 ], "prediction" : 0 }, { "id" : 3, "age" : 50, "weight" : 76.3, "binarized_weight" : 1, "features" : [ 1 ], "prediction" : 0 })" :)
let $data := structured-json-file("../../../../queries/rumbleML/sample-ml-data-age-weight.json")

let $binarizer := get-transformer("Binarizer")
let $intermediate-data := $binarizer($data, {"inputCol": "weight", "outputCol": "binarized_weight", "threshold": 70.0})

let $df-intermediate-data := annotate($intermediate-data, {"id": "integer", "age": "integer", "weight": "double", "binarized_weight": "double"})
let $vector-assembler := get-transformer("VectorAssembler")
let $df-intermediate-data := $vector-assembler($df-intermediate-data, {"inputCols" : [ "binarized_weight" ], "outputCol" : "features" })

let $est := get-estimator("KMeans")
let $tra := $est(
    $df-intermediate-data,
    { }
)

for $i in $tra(
    $df-intermediate-data,
    { }
)
return $i

(: process data by chaining a transformer followed by a full estimator execution :)
