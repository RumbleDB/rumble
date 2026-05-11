(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, true, true, true, true)" :)
for $i in annotate(
  for $j in 1 to 10 return {"foo":{"bar":[ 1 to $j ]}},
  {"foo":{"bar":["integer"]}}
)
for $j in $i.foo.bar[[1]] eq 1 
return $j