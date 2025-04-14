(:JIQS: ShouldCrash; ErrorCode="XQDY0027"; :)
annotate(
    structured-json-lines("../../../../queries/rumbleML/sample-ml-data-age-weight.json"),
    {"id": "decimal", "age": "decimal"}
)

(: schema missing a field :)
