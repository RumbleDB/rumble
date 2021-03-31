(:JIQS: ShouldRun; Output="" :)
keys(
let $x := annotate(
  for $i in 1 to 5
  return {"foo":$i, "bar":($i+1)},
  {"foo":"integer", "bar":"integer"}
).foo
return
project($x, "foo")
)