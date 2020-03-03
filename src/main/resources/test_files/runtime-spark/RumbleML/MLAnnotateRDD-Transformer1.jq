(:JIQS: ShouldRun; Output="({ "id" : 1, "age" : 20, "weight" : 68.8, "features" : [ 20, 68.8 ] }, { "id" : 2, "age" : 35, "weight" : 72.4, "features" : [ 35, 72.4 ] }, { "id" : 3, "age" : 50, "weight" : 76.3, "features" : [ 50, 76.3 ] })" :)
let $transformer := get-transformer("VectorAssembler")
let $rdd-data := json-file("./src/main/resources/queries/rumbleML/sample-ml-age-weight-data.json")
let $df-data := annotate($rdd-data, {"id": "integer", "age": "integer", "weight": "double"})
for $i in $transformer(
  $df-data,
  {"inputCols": ["age", "weight"], "outputCol": "features"}
)
return $i
