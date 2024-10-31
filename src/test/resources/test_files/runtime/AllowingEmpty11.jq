(:JIQS: ShouldRun; Output="{ "foo" : [ ] }" :)

for $foo allowing empty in ()
return { "foo" : [ $foo ] }
