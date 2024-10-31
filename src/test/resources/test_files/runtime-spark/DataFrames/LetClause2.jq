(:JIQS: ShouldRun; Output="({ "$i" : 1, "$j" : 2, "$k" : 4 }, { "$i" : 2, "$j" : 3, "$k" : 5 }, { "$i" : 3, "$j" : 4, "$k" : 6 })" :)
for $i in parallelize((1,2,3)) let $j := $i + 1 let $k := $j + 2 return {"$i": $i, "$j": $j, "$k": $k}
