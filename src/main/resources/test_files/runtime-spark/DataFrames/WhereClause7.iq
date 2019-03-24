(:JIQS: ShouldRun; Output="({ "$i" : 2, "$j" : 4, "$k" : 7 }, { "$i" : 2, "$j" : 5, "$k" : 7 }, { "$i" : 2, "$j" : 5, "$k" : 8 }, { "$i" : 2, "$j" : 5, "$k" : 9 }, { "$i" : 2, "$j" : 6, "$k" : 7 })" :)
for $i in parallelize((1,2,3)) for $j in (4,5,6) for $k in (7,8,9) where $i eq 2 and ($j eq 5 or $k eq 7) return {"$i": $i, "$j": $j, "$k": $k}

