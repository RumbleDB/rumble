(:JIQS: ShouldRun; Output="(1, 2, 3, 2, 3, [ 4, 5 ])" :)
parallelize(([1, 2, 3], 1, 3, [ 2, 3, [ 4, 5 ] ], { "foo" : "bar" }))[]
