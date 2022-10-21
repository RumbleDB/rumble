(:JIQS: ShouldRun; Output="({ "k" : 2, "c" : 3 }, { "k" : 1, "c" : 4 }, { "k" : 0, "c" : 3 })" :)
for $i in annotate(
  for $j in 1 to 3 return {"key" : $j },
  {"key" : "integer" }
)
let $k := ($i.key || "") cast as integer
order by $k descending
return { "k": $k }
