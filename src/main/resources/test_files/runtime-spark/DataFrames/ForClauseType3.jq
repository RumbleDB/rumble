(:JIQS: ShouldRun; Output="(foo, bar)" :)
for $x as string? in parallelize(("foo", "bar"))
return $x
