(:JIQS: ShouldCrash; ErrorCode="XPDY0130"; :)
let $hashingTF := get-transformer("HashingTF")
return $hashingTF(
    (structured-json-file("./src/main/resources/queries/rumbleML/sample-ml-string-data.json")),
    {"inputCol": "sentence", "numFeatures": 2}
)
