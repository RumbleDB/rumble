(:JIQS: ShouldRun; Output="({ "bar" : [ 1 ], "foo" : [ ] }, { "bar" : [ 2 ], "foo" : [ ] })" :)

for $bar in (1, 2)
for $foo allowing empty in parallelize(())
return { "bar" : [ $bar ], "foo" : [ $foo ] }
