(:JIQS: ShouldRun; Output="(-1, 0, 1, 2, 3, 4, 5, 6, 7, 8)" :)
for $i in annotate(
  for $j in 1 to 10 return {"foo":{"bar":[ $j - 2 to $j ]}},
  {"foo":{"bar":["integer"]}}
)
order by $i.foo.bar[[2]]
return $i.foo.bar[[1]]