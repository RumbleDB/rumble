(:JIQS: ShouldRun; Output="{ "foo" : 1, "bar" : null, "foobar" : [ "test1", "test2" ] }" :)
zero-or-one(json-lines("../../../../queries/emptyFile.txt")),
zero-or-one(json-lines("../../../../queries/singleLine.json"))

