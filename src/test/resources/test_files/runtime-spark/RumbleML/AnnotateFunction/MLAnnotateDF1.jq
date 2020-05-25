(:JIQS: ShouldRun; Output="({ "age" : 20, "id" : 1, "weight" : 68.8 }, { "age" : 35, "id" : 2, "weight" : 72.4 }, { "age" : 50, "id" : 3, "weight" : 76.3 })" :)
annotate(
    structured-json-file("./src/test/resources/test_data/rumbleML/sample-ml-data-age-weight.json"),
    {"id": "decimal", "age": "decimal", "weight": "double"}
)
