(:JIQS: ShouldCrash; ErrorCode="RBML0003"; :)
let $local-data := (
    {"id": 1, "age":  20, "weight": 68.8},
    {"id": 2, "age":  35, "weight": 72.4},
    {"id": 3, "age":  50, "weight": 76.3}
)
let $df-data := annotate($local-data, {"id": "integer", "age": "integer", "weight": "decimal"})

let $binarizer := get-transformer("Binarizer")
return $binarizer($df-data, {"inputCol": "weight", "outputCol": "binarized_weight", "threshold": 70.0})

(: decimal types cannot be used in SparkML algorithms :)
