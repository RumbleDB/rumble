(:JIQS: ShouldRun; Output="{ "foo" : [ ] }" :)

for $foo allowing empty in parallelize(())
return { "foo" : [ $foo ] }
