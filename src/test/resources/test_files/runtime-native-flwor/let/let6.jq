(:JIQS: ShouldRun; Output="(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)" :)
for $i in annotate(
  for $j in 1 to 10 return {"foo":{"bar":$j}},
  {"foo":{"bar":"integer"}}
)
let $g := $i.foo.bar
return $g