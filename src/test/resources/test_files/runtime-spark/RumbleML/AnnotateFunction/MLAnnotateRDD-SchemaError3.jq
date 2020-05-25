(:JIQS: ShouldCrash; ErrorCode="RBML0005"; :)
annotate(
    json-file("./src/test/resources/test_data/rumbleML/sample-ml-data-age-weight.json"),
    {"id": "int", "age": "str", "weight": "dec"}
)

(: schema has unrecognized types :)
