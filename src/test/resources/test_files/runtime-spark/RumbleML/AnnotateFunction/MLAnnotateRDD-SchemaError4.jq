(:JIQS: ShouldCrash; ErrorCode="RBST0004"; :)
annotate(
    json-file("../../../../queries/rumbleML/sample-ml-data-age-weight.json"),
    {"id": "integer", "age": "date", "weight": "decimal"}
)

(: schema has incorrect type :)
