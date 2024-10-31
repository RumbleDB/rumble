(:JIQS: ShouldCrash; ErrorCode="RBML0003"; :)
let $transformer := get-transformer("VectorAssembler")
for $i in $transformer(
  (structured-json-file("../../../queries/rumbleML/sample-ml-data-age-weight.json")),
  {"inputCols": ["age", 3], "outputCol": "features"}
)
return $i

(: column in array does not exist (but cast from int to string works) :)
