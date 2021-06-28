(:JIQS: ShouldCrash; ErrorCode="XQDY0027"; :)
annotate(
    structured-json-file("../../../../queries/rumbleML/sample-ml-data-age-weight.json"),
    {"id": "integer", "age": "integer", "weight": "decimal"}
)

(: schema has incorrect types :)
