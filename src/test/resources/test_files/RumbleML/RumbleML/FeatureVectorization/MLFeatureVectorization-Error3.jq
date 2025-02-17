(:JIQS: ShouldCrash; ErrorCode="RBML0003"; :)
let $est := get-estimator("LogisticRegression")
let $tra := $est(
    libsvm-file("../../../../queries/rumbleML/sample-libsvm-data-short.txt"),
    {"featuresCol": [3] }
)
let $res := $tra(
    libsvm-file("../../../../queries/rumbleML/sample-libsvm-data-short.txt"),
    { }
)
return $res

(: column given in featuresCol does not exist (but integer is correctly converted to string) :)
