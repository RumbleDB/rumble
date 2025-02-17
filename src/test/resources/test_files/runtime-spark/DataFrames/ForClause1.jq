(:JIQS: ShouldRun; Output="({ "$i" : 1 }, { "$i" : 2 }, { "$i" : 3 })" :)
for $i in parallelize((1,2,3)) return {"$i": $i}
