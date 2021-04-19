(:JIQS: ShouldRun; Output="(0, 0, 0, 0, 1, 1, 1, 1, 1, 1)" :)
for $i in annotate(
  for $j in 1 to 10 return {"foo":{"bar":[ if($j ge 5) then { "foobar" : $j } else () ]}},
  {"foo":{"bar":[{"foobar":"integer"}]}}
)
for $j allowing empty at $k in $i.foo.bar[].foobar
return $k
