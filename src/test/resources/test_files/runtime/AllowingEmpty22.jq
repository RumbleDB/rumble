(:JIQS: ShouldRun; Output="({ "bar" : [ 1 ], "foo" : [ 3 ] }, { "bar" : [ 1 ], "foo" : [ 4 ] }, { "bar" : [ 2 ], "foo" : [ 3 ] }, { "bar" : [ 2 ], "foo" : [ 4 ] })" :)

for $bar in (1, 2)
for $foo allowing empty in parallelize((3, 4))
return { "bar" : [ $bar ], "foo" : [ $foo ] }
