(:JIQS: ShouldRun; Output="{ "foo" : 1, "bar" : null, "foobar" : [ "test1", "test2" ] }" :)
exactly-one(json-file("../../../../queries/singleLine.json"))

