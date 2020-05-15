(:JIQS: ShouldRun; Output="({ "foo" : 1, "bar" : null, "foobar" : [ "test1", "test2" ] }, { "foo" : 1, "bar" : null, "foobar" : [ "test1", "test2" ] }, { "foo" : 2, "bar" : "test3", "foobar" : [ "test4", "test5" ] })" :)
one-or-more(json-file("./src/test/resources/test_data/singleLine.json")),
one-or-more(json-file("./src/test/resources/test_data/multiLine.json"))

