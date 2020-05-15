(:JIQS: ShouldRun; Output="(500, 0, 1)" :)
count(structured-json-file("./src/test/resources/test_data/confusion_sample.json")),
count(structured-json-file("./src/test/resources/test_data/confusion_sample_empty.json")),
count(parquet-file("./src/test/resources/test_data/sample-json.snappy.parquet"))

