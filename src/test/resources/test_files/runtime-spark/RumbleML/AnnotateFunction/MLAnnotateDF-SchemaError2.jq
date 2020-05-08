(:JIQS: ShouldCrash; ErrorCode="RBML0005"; :)
annotate(
    structured-json-file("./src/test/resources/queries/rumbleML/sample-ml-data-age-weight.json"),
    {"id": "decimal", "age": "decimal"}
)

(: schema missing a field :)
