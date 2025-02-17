(:JIQS: ShouldRun; Output="{ "foo" : [ ], "pos" : 0 }" :)

for $foo allowing empty at $pos in parallelize(())
return { "foo" : [ $foo ], "pos" : $pos }
