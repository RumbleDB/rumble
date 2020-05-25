(:JIQS: ShouldRun; Output="({ "id" : 1, "age" : 20, "weight" : 68 }, { "id" : 2, "age" : 35, "weight" : 72 }, { "id" : 3, "age" : 50, "weight" : 76 })" :)
annotate(
    json-file("./src/test/resources/test_data/rumbleML/sample-ml-data-age-weight.json"),
    {"id": "integer", "age": "integer", "weight": "integer"}
)

(: decimal to integer cast on weight column :)
