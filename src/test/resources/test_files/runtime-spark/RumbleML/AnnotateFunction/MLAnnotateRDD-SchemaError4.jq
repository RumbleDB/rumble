(:JIQS: ShouldCrash; ErrorCode="XQDY0027"; :)
annotate(
    json-file("../../../../queries/rumbleML/sample-ml-data-age-weight.json"),
    {"id": "integer", "age": "date", "weight": "decimal"}
)

(: schema has incorrect type :)
