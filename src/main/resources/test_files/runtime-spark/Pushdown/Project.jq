(:JIQS: ShouldRun; Output="{ "foo" : "bar", "bar" : "foobar" }" :)
project(parallelize(for $i in 1 to 5000 return {"foo" : "bar", "bar" : "foobar", "foobar" : "foo" }), ("foo", "bar"))[1000]
