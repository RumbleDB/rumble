(:JIQS: ShouldRun; Output="({ "foo" : 1, "bar" : 2 }, { "foo" : 2, "bar" : 3 }, { "foo" : 3, "bar" : 4 }, { "foo" : 4, "bar" : 5 }, { "foo" : 5, "bar" : 6 })" :)
let $x := annotate(
  for $i in 1 to 5
  return {"foo":$i, "bar":($i+1)},
  {"foo":"integer", "bar":"integer"}
)
return
project($x, ("foo", "foobar", "bar"))
