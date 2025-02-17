(:JIQS: ShouldRun; Output="({ "bar" : [ 1 ], "foo" : [ ], "pos" : 0 }, { "bar" : [ 2 ], "foo" : [ ], "pos" : 0 })" :)

for $bar in parallelize((1, 2))
for $foo allowing empty at $pos in parallelize(())
return { "bar" : [ $bar ], "foo" : [ $foo ], "pos" : $pos }
