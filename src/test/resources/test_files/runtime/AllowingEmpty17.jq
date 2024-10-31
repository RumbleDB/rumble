(:JIQS: ShouldRun; Output="({ "bar" : [ 1 ], "foo" : [ 3 ], "pos" : 1 }, { "bar" : [ 1 ], "foo" : [ 4 ], "pos" : 2 }, { "bar" : [ 2 ], "foo" : [ 3 ], "pos" : 1 }, { "bar" : [ 2 ], "foo" : [ 4 ], "pos" : 2 })" :)

for $bar in parallelize((1, 2))
for $foo allowing empty at $pos in parallelize((3, 4))
return { "bar" : [ $bar ], "foo" : [ $foo ], "pos" : $pos }
