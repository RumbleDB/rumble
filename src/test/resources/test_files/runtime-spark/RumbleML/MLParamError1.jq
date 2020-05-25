(:JIQS: ShouldCrash; ErrorCode="RBML0003"; :)
let $hashingTF := get-transformer("HashingTF")
return $hashingTF(
    (structured-json-file("./src/test/resources/test_data/rumbleML/sample-ml-data-sentence.json")),
    {"inputCol": 123, "numFeatures": 2}
)

(: column does not exist :)
