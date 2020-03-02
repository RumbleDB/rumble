(:JIQS: ShouldCrash; ErrorCode="RBML0003"; :)
let $est := get-estimator("LogisticRegression")
let $tra := $est(
    libsvm-file("./src/main/resources/queries/rumbleML/sample-libsvm-data-short.txt"),
    {"featuresCol": "does not exist"}
)
let $res := $tra(
    libsvm-file("./src/main/resources/queries/rumbleML/sample-libsvm-data-short.txt"),
    { }
)
return $res

(: estimator's featuresCol does not exist :)
