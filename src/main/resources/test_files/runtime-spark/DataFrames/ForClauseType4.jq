(:JIQS: ShouldRun; Output="(foo, bar)" :)
for $x as item in parallelize(("foo", "bar"))
return $x
