(:JIQS: ShouldCrash; ErrorCode="RBML0005"; :)
annotate(
    structured-json-file("./src/main/resources/queries/rumbleML/sample-ml-data-age-weight.json"),
    {"id": "decimal", "age": "decimal"}
)

(: schema missing a field :)
