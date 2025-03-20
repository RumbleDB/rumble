(:JIQS: ShouldRun; Output="({ "id" : 1, "age" : 20, "weight" : 68.8 }, { "id" : 2, "age" : 35, "weight" : 72.4 }, { "id" : 3, "age" : 50, "weight" : 76.3 })" :)
annotate(
    structured-json-lines("../../../../queries/rumbleML/sample-ml-data-age-weight.json"),
    {"id": "integer", "age": "integer", "weight": "decimal"}
)

(: schema has incorrect types :)
