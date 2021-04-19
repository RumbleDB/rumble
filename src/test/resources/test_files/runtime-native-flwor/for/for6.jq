(:JIQS: ShouldRun; Output="(1, 1, 1, 1, 1, 1, 1, 1, 1, 1)" :)
for $i in annotate(
  for $j in 1 to 10 return {"foo":{"bar":[ 1 to $j ]}},
  {"foo":{"bar":["integer"]}}
)
for $j allowing empty in $i.foo.bar[[11]] 
return 1