(:JIQS: ShouldRun; Output="({ "bar" : 2, "foo" : [ "test1", "test2" ], "foobar" : { "foo" : 1 } }, { "bar" : 2, "foo" : [ "test4", "test5" ], "foobar" : { "foo" : 2 } })" :)
structured-json-file("../../../queries/nested.json").foobar

