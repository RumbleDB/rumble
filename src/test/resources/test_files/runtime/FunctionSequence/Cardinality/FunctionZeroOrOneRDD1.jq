(:JIQS: ShouldRun; Output="{ "foo" : 1, "bar" : null, "foobar" : [ "test1", "test2" ] }" :)
zero-or-one(json-file("./src/test/resources/test_data/emptyFile.txt")),
zero-or-one(json-file("./src/test/resources/test_data/singleLine.json"))

