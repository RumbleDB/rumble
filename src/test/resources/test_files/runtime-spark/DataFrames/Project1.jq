(:JIQS: ShouldRun; Output="({ "foo" : 1 }, { "foo" : 2 }, { "foo" : 3 }, { "foo" : 4 }, { "foo" : 5 })" :)
let $x := annotate(
  for $i in 1 to 5
  return {"foo":$i, "bar":($i+1)},
  {"foo":"integer", "bar":"integer"}
)
return
project($x, "foo")
