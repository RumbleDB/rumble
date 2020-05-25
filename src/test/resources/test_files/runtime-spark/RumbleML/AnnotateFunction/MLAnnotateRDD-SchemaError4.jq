(:JIQS: ShouldCrash; ErrorCode="RBML0005"; :)
annotate(
    json-file("./src/test/resources/test_data/rumbleML/sample-ml-data-age-weight.json"),
    {"id": "integer", "age": "string", "weight": "decimal"}
)

(: schema has incorrect type :)
