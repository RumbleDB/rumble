(:JIQS: ShouldRun; Output="(true, true, true, true, true, false, false, false, false, false)" :)
for $i in annotate(
  for $j in 1 to 10 return {"foo":{"bar":$j}},
  {"foo":{"bar":"integer"}}
)
let $g := $i.foo.bar le 5
return $g