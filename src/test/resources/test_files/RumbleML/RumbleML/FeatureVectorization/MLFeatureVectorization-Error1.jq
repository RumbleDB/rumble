(:JIQS: ShouldCrash; ErrorCode="RBML0003"; :)
let $est := get-estimator("LogisticRegression")
let $tra := $est(
    libsvm-file("../../../../queries/rumbleML/sample-libsvm-data-short.txt"),
    {"featuresCol": [ "label" ]}
)
let $res := $tra(
    libsvm-file("../../../../queries/rumbleML/sample-libsvm-data-short.txt"),
    { }
)
return $res

(: estimator's featuresCol is not a string :)
