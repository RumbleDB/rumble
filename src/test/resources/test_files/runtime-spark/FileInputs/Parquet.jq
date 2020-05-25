(:JIQS: ShouldRun; Output="{ "bool" : true, "bool_array" : [ true ], "float" : 4.2, "float_array" : [ 4.2 ], "int64" : 42, "int64_array" : [ 42 ], "null" : null, "object" : { "bool" : true, "float" : 4.2, "int64" : 42, "null" : null, "object" : { "bool" : true, "null" : null }, "string" : "hello" }, "object_array" : [ { "bool" : true, "float" : 4.2, "int64" : 42, "null" : null, "object" : { "bool" : true, "null" : null }, "string" : "hello" } ], "string" : "hello", "string_array" : [ "hello" ] }" :)
parquet-file("./src/test/resources/test_data/sample-json.snappy.parquet")

