(:JIQS: ShouldRun; Output="({ "id" : 1, "sentence" : "Hi I heard about Spark", "output" : [ "hi", "i", "heard", "about", "spark" ] }, { "id" : 2, "sentence" : "I wish Java could use case classes", "output" : [ "i", "wish", "java", "could", "use", "case", "classes" ] }, { "id" : 3, "sentence" : "Logistic regression models are neat", "output" : [ "logistic", "regression", "models", "are", "neat" ] })" :)
let $transformer := get-transformer("Tokenizer")
let $data := (structured-json-file("./src/main/resources/queries/rumbleML/sample-ml-data.json"))
return $transformer($data, {"inputCol": "sentence", "outputCol": "output"})
