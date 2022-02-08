(:JIQS: ShouldRun; Output="({ "k" : 2, "c" : 3 }, { "k" : 1, "c" : 4 }, { "k" : 0, "c" : 3 })" :)
for $i in annotate(
  for $j in 1 to 10 return {"key" : $j mod 3, "foo":{"bar":[ $j - 2 to $j ]}},
  {"key" : "integer", "foo":{"bar":["integer"]}}
)
group by $k := ($i.key || "") cast as integer
order by $k descending
return { "k": $k, "c" : count($i) }
