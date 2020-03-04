(:JIQS: ShouldCrash; ErrorCode="RBML0005"; :)
annotate(
    structured-json-file("./src/main/resources/queries/rumbleML/sample-ml-data-age-weight.json"),
    {"id": "int", "age": "int", "weight": "dou"}
)

(: schema has unrecognized types :)
