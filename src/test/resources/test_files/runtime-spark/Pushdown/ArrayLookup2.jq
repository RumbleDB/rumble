(:JIQS: ShouldRun; Output="(1, [ 5, 6 ], { "foo" : "bar" })" :)
 parallelize(([1, 2, 3, 4], { "foo" : "bar" }, true, "foo", 3, 4, [ [ 5, 6 ], 6, 7, 8], [], [{ "foo" : "bar" }, 10]))[[1]]
 
