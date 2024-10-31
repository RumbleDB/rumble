(:JIQS: ShouldRun; Output="({ "foobar" : "foo" }, [ "foo", "bar", "foobar" ])" :)
remove-keys(parallelize(for $i in 1 to 5000 return {"foo" : "bar", "bar" : "foobar", "foobar" : "foo" }), ("foo", "bar"))[1000],
remove-keys(parallelize(for $i in 1 to 5000 return ["foo", "bar", "foobar"]), ("foo", "bar"))[1000]
