(:JIQS: ShouldRun; Output="{ "a" : [ "abc", 2, [ 1, 2 ] ], "b" : [ 2, "ab", 30 ] }" :)
intersect(({"a" : "abc", "b" : 2, "c" : "discard", "d" : "discard"}, { "a" : 2, "b" : "ab", "c" : "5e+1" }, {"a" : [1, 2], "b" : 3e+1, "d" : 3} ))
