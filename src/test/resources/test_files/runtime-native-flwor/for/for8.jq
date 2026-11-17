(:JIQS: ShouldRun; Output="({ "j" : 1, "c" : 10 }, { "j" : 2, "c" : 9 }, { "j" : 3, "c" : 8 }, { "j" : 4, "c" : 7 }, { "j" : 5, "c" : 6 }, { "j" : 6, "c" : 5 }, { "j" : 7, "c" : 4 }, { "j" : 8, "c" : 3 }, { "j" : 9, "c" : 2 }, { "j" : 10, "c" : 1 })" :)
for $i in annotate(
  for $j in 1 to 10 return {"foo":{"bar":[ 1 to $j ]}},
  {"foo":{"bar":["integer"]}}
)
for $j in $i.foo.bar[]
group by $j
order by $j
return { "j" : $j, "c" : count($i) }