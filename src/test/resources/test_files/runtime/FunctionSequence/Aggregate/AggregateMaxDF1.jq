(:JIQS: ShouldRun; Output="(100)" :)
let $x := annotate(
  for $i in 1 to 100
  return {"foo":$i, "bar":($i+1)},
  {"foo":"integer", "bar":"integer"}
).foo
return max($x)
