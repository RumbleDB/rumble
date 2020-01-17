(:JIQS: ShouldCrash; ErrorCode="XPDY0130"; :)
let $hashingTF := get-transformer("HashingTF")
let $data := (structured-json-file("./src/main/resources/queries/rumbleML/sample-ml-string-data.json"))
return $hashingTF($data, {"inputCol": "sentence", "numFeatures": 2})
