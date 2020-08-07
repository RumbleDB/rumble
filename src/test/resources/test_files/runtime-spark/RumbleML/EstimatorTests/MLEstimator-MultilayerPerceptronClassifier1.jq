(:JIQS: ShouldRun; Output="({ "label" : 1, "features" : { "0" : -0.222222, "1" : 0.5, "2" : -0.762712, "3" : -0.833333 }, "prediction" : 1 }, { "label" : 1, "features" : { "0" : -0.555556, "1" : 0.25, "2" : -0.864407, "3" : -0.916667 }, "prediction" : 1 }, { "label" : 1, "features" : { "0" : -0.722222, "1" : -0.166667, "2" : -0.864407, "3" : -0.833333 }, "prediction" : 1 }, { "label" : 1, "features" : { "0" : -0.722222, "1" : 0.166667, "2" : -0.694915, "3" : -0.916667 }, "prediction" : 1 })" :)
let $data := libsvm-file("../../../../queries/rumbleML/sample-libsvm-data-multiclass-classification.txt")

let $est := get-estimator("MultilayerPerceptronClassifier")
let $tra := $est(
    $data,
    { "layers": [4, 5, 4, 3], "blockSize": 128, "seed": 1234, "maxIter": 3}
)
for $result in $tra(
    $data,
    { }
)
count $c
where $c lt 5
return {
    "label": $result.label,
    "features": $result.features,
    "prediction": $result.prediction
}

(:
specify layers for the neural network:
input layer of size 4 (features), two intermediate of size 5 and 4
and output of size 3 (classes)
:)
