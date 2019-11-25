(:JIQS: ShouldRun; Output="{ "foo" : 1, "bar" : null, "foobar" : [ "test1", "test2" ] }" :)
zero-or-one(json-file("./src/main/resources/queries/emptyFile.txt")),
zero-or-one(json-file("./src/main/resources/queries/singleLine.json"))

