(:JIQS: ShouldRun; Output="{ "b" : [ 2, [ 2, "xyz" ] ], "c" : [ 3, { "d" : 200 } ] }" :)
accumulate(parallelize(({ "b" : 2 }, { "c" : 3 }, { "b" : [2,"xyz"] }, {"c" : {"d" : 2e+2}})))

(: general functionality :)

