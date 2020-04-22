(:JIQS: ShouldRun; Output="({ "foo" : "bar" })" :)
descendant-pairs(parallelize(for $ i in 1 to 500 return {"foo": "bar"}))[250]