(:JIQS: ShouldRun; Output="({ "label" : 1.218, "censor" : 1, "feature1" : 1.56, "feature2" : -0.605, "prediction" : 5 }, { "label" : 2.949, "censor" : 0, "feature1" : 0.346, "feature2" : 2.158, "prediction" : 18 }, { "label" : 3.627, "censor" : 0, "feature1" : 1.38, "feature2" : 0.231, "prediction" : 7 }, { "label" : 0.273, "censor" : 1, "feature1" : 0.52, "feature2" : 1.151, "prediction" : 13 }, { "label" : 4.199, "censor" : 0, "feature1" : 0.795, "feature2" : -0.226, "prediction" : 9 })" :)
let $raw-data := parallelize((
    {"label": 1.218, "censor": 1.0, "feature1": 1.560, "feature2": -0.605},
    {"label": 2.949, "censor": 0.0, "feature1": 0.346, "feature2": 2.158},
    {"label": 3.627, "censor": 0.0, "feature1": 1.380, "feature2": 0.231},
    {"label": 0.273, "censor": 1.0, "feature1": 0.520, "feature2": 1.151},
    {"label": 4.199, "censor": 0.0, "feature1": 0.795, "feature2": -0.226}
))

let $data := annotate(
    $raw-data,
    { "label": "double", "censor": "double", "feature1": "double", "feature2": "double"}
)
let $vector-assembler := get-transformer("VectorAssembler")
let $data := $vector-assembler($data, {"inputCols" : [ "feature1", "feature2" ], "outputCol" : "features" })

let $est := get-estimator("AFTSurvivalRegression")
let $tra := $est(
    $data,
    { "quantileProbabilities": [0.3, 0.6], "quantilesCol": "quantiles"}
)
for $result in $tra(
    $data,
    { }
)
return {
    "label": $result.label,
    "censor": $result.censor,
    "feature1": $result.feature1,
    "feature2": $result.feature2,
    "prediction": $result.prediction cast as integer
}

(: Result cast as integer intentionally to lose precision so that test results can match :)

(: https://spark.apache.org/docs/2.4.5/ml-classification-regression.html#survival-regression :)
