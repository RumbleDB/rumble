(:JIQS: ShouldRun; Output="({ "label" : 0, "prediction" : 0 }, { "label" : 1, "prediction" : 1 }, { "label" : 1, "prediction" : 1 }, { "label" : 1, "prediction" : 1 }, { "label" : 1, "prediction" : 1 })" :)
let $est := get-estimator("LogisticRegression")
let $tra := $est(
    libsvm-file("../../../../queries/rumbleML/sample-libsvm-data-short.txt"),
    { }
)
for $resultRow in $tra(
    libsvm-file("../../../../queries/rumbleML/sample-libsvm-data-short.txt"),
    { }
)
return {
    "label": $resultRow.label,
    "prediction": $resultRow.prediction
}


(: featureCol parameter defaults to 'features', the column with this default name is vectorized and used :)
