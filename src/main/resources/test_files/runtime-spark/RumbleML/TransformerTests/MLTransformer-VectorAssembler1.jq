(:JIQS: ShouldRun; Output="({ "age" : 20, "id" : 1, "weight" : 68.8, "features" : [ 20, 68.8 ] }, { "age" : 35, "id" : 2, "weight" : 72.4, "features" : [ 35, 72.4 ] }, { "age" : 50, "id" : 3, "weight" : 76.3, "features" : [ 50, 76.3 ] })" :)
let $transformer := get-transformer("VectorAssembler")
for $i in $transformer(
  (structured-json-file("./src/main/resources/queries/rumbleML/sample-ml-data-age-weight.json")),
  {"inputCols": ["age", "weight"], "outputCol": "features"}
)
return $i
