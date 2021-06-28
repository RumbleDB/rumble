(:JIQS: ShouldRun; Output="({ "label" : 0, "features" : { } }, { "label" : 1, "features" : { "0" : 0.1, "1" : 0.1, "2" : 0.1 } }, { "label" : 2, "features" : { "0" : 0.2, "1" : 0.2, "2" : 0.2 } }, { "label" : 3, "features" : { "0" : 9, "1" : 9, "2" : 9 } }, { "label" : 4, "features" : { "0" : 9.1, "1" : 9.1, "2" : 9.1 } }, { "label" : 5, "features" : { "0" : 9.2, "1" : 9.2, "2" : 9.2 } })" :)
annotate(
    libsvm-file("../../../../queries/rumbleML/sample-libsvm-data-kmeans.txt"),
    {"label": "double", "features": "array"}
)

(: annotate with vector type representation in the 'features' :)
