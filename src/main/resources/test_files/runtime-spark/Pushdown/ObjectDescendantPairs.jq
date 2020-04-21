(:JIQS: ShouldRun; Output="({ "foo" : "bar" })" :)
descendant-pairs(parallelize(for $ i in 1 to 1000 return {"foo": "bar"}))[500]