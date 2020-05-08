(:JIQS: ShouldRun; Output="({ "$i" : 1, "$j" : 2 }, { "$i" : 2, "$j" : 3 }, { "$i" : 3, "$j" : 4 })" :)
for $i in parallelize((1,2,3)) let $j := $i + 1 return {"$i": $i, "$j": $j}
