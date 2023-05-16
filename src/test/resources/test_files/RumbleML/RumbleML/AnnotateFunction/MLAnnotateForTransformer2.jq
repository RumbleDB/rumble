(:JIQS: ShouldRun; Output="({ "id" : 1, "sentence" : "Hi I heard about Spark", "output" : [ "hi", "i", "heard", "about", "spark" ] }, { "id" : 2, "sentence" : "I wish Java could use case classes", "output" : [ "i", "wish", "java", "could", "use", "case", "classes" ] }, { "id" : 3, "sentence" : "Logistic regression models are neat", "output" : [ "logistic", "regression", "models", "are", "neat" ] })" :)
let $rdd-data := json-file("../../../../queries/rumbleML/sample-ml-data-sentence.json")
let $df-data := annotate($rdd-data, {"id": "integer", "sentence": "string"})

let $transformer := get-transformer("Tokenizer")
for $i in $transformer(
    $df-data,
    {"inputCol": "sentence", "outputCol": "output"}
)
return $i
