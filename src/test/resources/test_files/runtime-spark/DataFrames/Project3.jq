(:JIQS: ShouldRun; Output="(1, 2, 3, 4, 5)" :)
let $x := annotate(
  for $i in 1 to 5
  return {"foo":$i, "bar":($i+1)},
  {"foo":"integer", "bar":"integer"}
).foo
return
project($x, "foo")
