(:JIQS: ShouldCrash; ErrorCode="XQST0012"; :)
annotate(
    structured-json-lines("../../../../queries/rumbleML/sample-ml-data-age-weight.json"),
    {"id": "decimal", "age": "decimal", "weight": "double", "i-should-not-exist": null}
)

(: schema has extra fields :)
