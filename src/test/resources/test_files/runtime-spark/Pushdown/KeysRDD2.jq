(:JIQS: ShouldRun; Output="" :)
keys(keys(parallelize(for $i in 1 to 10000 return { "foo" : "bar", "bar" : "foobar"})))
