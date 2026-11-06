(:JIQS: ShouldRun; Output="(1, 1, 2, 1, 2, 3, 1, 2, 3, 4, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)" :)
for $i in annotate(
  for $j in 1 to 10 return {"foo":{"bar":[ (1 to $j) ! { "foobar" : $$ } ]}},
  {"foo":{"bar":[{"foobar":"integer"}]}}
)
for $j in $i.foo.bar[].foobar
return $j