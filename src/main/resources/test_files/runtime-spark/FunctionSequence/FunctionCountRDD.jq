(:JIQS: ShouldRun; Output="(500, 0, 1)" :)
count(structured-json-file("./src/main/resources/queries/confusion_sample.json")),
count(structured-json-file("./src/main/resources/queries/confusion_sample_empty.json")),
count(parquet-file("./src/main/resources/queries/sample-json.snappy.parquet"))

