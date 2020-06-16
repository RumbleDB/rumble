(:JIQS: ShouldCrash; ErrorCode="RBML0005"; :)
annotate(
    structured-json-file("../../../../queries/rumbleML/sample-ml-data-age-weight.json"),
    {"id": "integer", "age": "integer", "weight": "double"}
)

(: schema has incorrect types :)
