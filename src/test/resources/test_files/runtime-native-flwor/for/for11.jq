(:JIQS: ShouldRun; Output="(1, 1, 1, 1, 1, 1, 1, 1, 1, 1)" :)
for $i in annotate(
  for $j in 1 to 10 return {"foo":{"bar":[ if($j ge 5) then { "foobar" : $j } else () ]}},
  {"foo":{"bar":[{"foobar":"integer"}]}}
)
for $j at $k in $i.foo.bar
return $k
