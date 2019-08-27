(:JIQS: ShouldRun; Output="{ "a" : [ "abc", 2 ], "b" : [ 2, "ab" ], "c" : [ [ 1, 2 ], "5e+1" ] }" :)
intersect(({"a" : "abc", "b" : 2, "c" : [1, 2], "d" : "this will be discarded"}, { "a" : 2, "b" : "ab", "c" : "5e+1" }))
