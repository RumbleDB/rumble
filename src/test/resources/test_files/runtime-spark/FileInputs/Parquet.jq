(:JIQS: ShouldRun; Output="{ "bool" : true, "bool_array" : [ true ], "float" : 4.2, "float_array" : [ 4.2 ], "int64" : 42, "int64_array" : [ 42 ], "object" : { "bool" : true, "float" : 4.2, "int64" : 42, "object" : { "bool" : true }, "string" : "hello" }, "object_array" : [ { "bool" : true, "float" : 4.2, "int64" : 42, "object" : { "bool" : true }, "string" : "hello" } ], "string" : "hello", "string_array" : [ "hello" ] }" :)
parquet-file("../../../queries/sample-json.snappy.parquet")

