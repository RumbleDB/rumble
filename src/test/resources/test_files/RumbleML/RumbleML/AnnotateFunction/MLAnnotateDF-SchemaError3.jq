(:JIQS: ShouldCrash; ErrorCode="XPST0051"; :)
annotate(
    structured-json-lines("../../../../queries/rumbleML/sample-ml-data-age-weight.json"),
    {"id": "int", "age": "int", "weight": "dou"}
)

(: schema has unrecognized types :)
