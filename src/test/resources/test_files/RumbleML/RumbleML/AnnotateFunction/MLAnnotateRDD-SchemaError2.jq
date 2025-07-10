(:JIQS: ShouldCrash; ErrorCode="XQDY0027"; :)
annotate(
    json-lines("../../../../queries/rumbleML/sample-ml-data-age-weight.json"),
    {"id": "integer", "age": "integer" }
)

(: schema missing a field :)
