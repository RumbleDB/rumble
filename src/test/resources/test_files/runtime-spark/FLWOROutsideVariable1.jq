(:JIQS: ShouldRun; Output="({ "k" : 1, "i" : 1 }, { "k" : 0, "i" : 1 }, { "k" : 1, "i" : 2 }, { "k" : 0, "i" : 2 })" :)

for $i in 1 to 2
return
  for $j in parallelize(1 to 4)
  let $k := $j mod 2
  group by $k
  return { "k" : $k, "i" : $i }