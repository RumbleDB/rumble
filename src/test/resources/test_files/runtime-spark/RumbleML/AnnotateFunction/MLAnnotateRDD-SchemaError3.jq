(:JIQS: ShouldCrash; ErrorCode="XPST0051"; :)
annotate(
    json-file("../../../../queries/rumbleML/sample-ml-data-age-weight.json"),
    {"id": "int", "age": "str", "weight": "dec"}
)

(: schema has unrecognized types :)
