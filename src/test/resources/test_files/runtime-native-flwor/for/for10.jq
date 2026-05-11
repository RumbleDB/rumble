(:JIQS: ShouldRun; Output="([ ], [ ], [ ], [ ], [ 5 ], [ 6 ], [ 7 ], [ 8 ], [ 9 ], [ 10 ])" :)
for $i in annotate(
  for $j in 1 to 10 return {"foo":{"bar":[ if($j ge 5) then { "foobar" : $j } else () ]}},
  {"foo":{"bar":[{"foobar":"integer"}]}}
)
for $j allowing empty in $i.foo.bar[].foobar
return [ $j ]
