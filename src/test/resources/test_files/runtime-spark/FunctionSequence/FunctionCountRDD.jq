(:JIQS: ShouldRun; Output="(500, 0, 1)" :)
count(structured-json-file("../../../queries/confusion_sample.json")),
count(structured-json-file("../../../queries/confusion_sample_empty.json")),
count(parquet-file("../../../queries/sample-json.snappy.parquet"))

