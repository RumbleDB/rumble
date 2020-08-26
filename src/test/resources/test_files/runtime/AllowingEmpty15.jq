(:JIQS: ShouldRun; Output="({ "foo" : [ 1 ] }, { "foo" : [ 2 ] })" :)

for $foo allowing empty in parallelize((1, 2))
return { "foo" : [ $foo ] }
