(:JIQS: ShouldRun; Output="({ "label" : 0, "prediction" : 0 }, { "label" : 1, "prediction" : 1 }, { "label" : 1, "prediction" : 1 }, { "label" : 1, "prediction" : 1 }, { "label" : 1, "prediction" : 1 })" :)
let $est := get-estimator("LogisticRegression")
let $tra := $est(
    libsvm-file("./src/test/resources/test_data/rumbleML/sample-libsvm-data-short.txt"),
    {"featuresCol": ["features"]}
)
for $resultRow in $tra(
    libsvm-file("./src/test/resources/test_data/rumbleML/sample-libsvm-data-short.txt"),
    {"featuresCol": ["features"] }
)
return {
    "label": $resultRow.label,
    "prediction": $resultRow.prediction
}


(: libsvm data is read into a SparseVector form, applying vectorization to libSVM data should be idempotent :)
