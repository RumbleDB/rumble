(:JIQS: ShouldCrash; ErrorCode="RBML0003"; :)
let $est := get-estimator("LogisticRegression")
let $tra := $est(
    libsvm-file("./src/main/resources/queries/rumbleML/sample-libsvm-data-short.txt"),
    {"featuresCol": [3] }
)
let $res := $tra(
    libsvm-file("./src/main/resources/queries/rumbleML/sample-libsvm-data-short.txt"),
    { }
)
return $res

(: estimator's featuresCol does not exist (but integer is correctly converted to string) :)
