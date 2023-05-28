(:JIQS: ShouldRun; Output="{ "bool_array" : [ true ], "float" : 4.2, "float_array" : [ 4.2 ], "int64" : 42, "int64_array" : [ 42 ], "object" : { "bool" : true, "float" : 4.2, "int64" : 42, "object" : { "bool" : true }, "string" : "hello" }, "object_array" : [ { "bool" : true, "float" : 4.2, "int64" : 42, "object" : { "bool" : true }, "string" : "hello" } ], "string" : "hello", "string_array" : [ "hello" ], "bool" : true }" :)
copy json $je := delta-file("../../../queries/sample_json_delta")
modify replace json value of $je.bool with true
return $je