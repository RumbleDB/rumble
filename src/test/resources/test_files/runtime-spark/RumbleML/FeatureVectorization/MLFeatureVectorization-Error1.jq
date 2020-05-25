(:JIQS: ShouldCrash; ErrorCode="FORG0006"; :)
let $est := get-estimator("LogisticRegression")
let $tra := $est(
    libsvm-file("./src/test/resources/test_data/rumbleML/sample-libsvm-data-short.txt"),
    {"featuresCol": "label"}
)
let $res := $tra(
    libsvm-file("./src/test/resources/test_data/rumbleML/sample-libsvm-data-short.txt"),
    { }
)
return $res

(: estimator's featuresCol is not an array :)
