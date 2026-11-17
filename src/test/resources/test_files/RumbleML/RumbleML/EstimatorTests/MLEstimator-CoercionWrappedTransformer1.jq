(:JIQS: ShouldRun; Output="(6, 6)" :)

let $local-data := (
    {"id": 0, "col1": 0.0, "col2": 0.0, "col3": 0.0},
    {"id": 1, "col1": 0.1, "col2": 0.1, "col3": 0.1},
    {"id": 2, "col1": 0.2, "col2": 0.2, "col3": 0.2},
    {"id": 3, "col1": 9.0, "col2": 9.0, "col3": 9.0},
    {"id": 4, "col1": 9.1, "col2": 9.1, "col3": 9.1},
    {"id": 5, "col1": 9.2, "col2": 9.2, "col3": 9.2}
)
let $df-data := annotate($local-data, {"id": "integer", "col1": "decimal", "col2": "decimal", "col3": "decimal"})
let $vector-assembler := get-transformer("VectorAssembler")
let $df-data := $vector-assembler($df-data, {"inputCols" : [ "col1", "col2", "col3" ], "outputCol" : "features" })
let $estimator := get-estimator("KMeans")
let $trained-transformer := $estimator($df-data, {})
let $make-generic-transformer := function() as function(item*, item*) as item* {
  $trained-transformer
}
let $generic-transformer := $make-generic-transformer()
let $apply-default-params := $generic-transformer(?, {})
for $iteration in 1 to 2
return count($apply-default-params($df-data))
