(:JIQS: ShouldCrash; ErrorCode="RBML0004"; :)
let $transformer := get-transformer("VectorAssembler")
let $rdd-data := json-file("./src/main/resources/queries/rumbleML/sample-ml-age-weight-data.json")
for $i in $transformer(
  $rdd-data,
  {"inputCols": ["age", "weight"], "outputCol": "features"}
)
return $i
