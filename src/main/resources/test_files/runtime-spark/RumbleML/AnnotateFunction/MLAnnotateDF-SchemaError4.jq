(:JIQS: ShouldCrash; ErrorCode="RBML0005"; :)
annotate(
    structured-json-file("./src/main/resources/queries/rumbleML/sample-ml-data-age-weight.json"),
    {"id": "integer", "age": "integer", "weight": "double"}
)

(: schema has incorrect types :)
