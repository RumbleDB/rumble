(:JIQS: ShouldRun; Output="(20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, { "foo" : 2 })" :)
parallelize(20 to 30), parallelize(1 to 10), descendant-objects(parallelize(for $ i in 1 to 500 return {"foo": {"bar": 1, "foobar": {"foo": 2}}}))[300]
