(:JIQS: ShouldRun; Output="({ "foo" : [ 3 ], "pos" : 1 }, { "foo" : [ 4 ], "pos" : 2 })" :)

for $foo allowing empty at $pos in parallelize((3, 4))
return { "foo" : [ $foo ], "pos" : $pos }
