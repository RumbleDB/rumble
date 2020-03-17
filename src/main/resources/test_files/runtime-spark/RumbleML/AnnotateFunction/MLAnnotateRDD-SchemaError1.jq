(:JIQS: ShouldCrash; ErrorCode="RBML0005"; :)
annotate(
    json-file("./src/main/resources/queries/rumbleML/sample-ml-data-age-weight.json"),
    {"id": "integer", "age": "integer", "weight": "decimal", "name": "string"}
)

(: schema has extra fields :)
