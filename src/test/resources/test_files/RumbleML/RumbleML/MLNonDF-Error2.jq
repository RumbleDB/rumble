(:JIQS: ShouldCrash; ErrorCode="RBML0004"; :)
let $transformer := get-transformer("VectorAssembler")
let $rdd-data := json-lines("../../../queries/rumbleML/sample-ml-data-age-weight.json")
for $i in $transformer(
  $rdd-data,
  {"inputCols": ["age", "weight"], "outputCol": "features"}
)
return $i

(: transformers expect a dataframe as the input dataset :)
