(:JIQS: ShouldRun; Output="({ "$i" : 2 }, { "$i" : 3 })" :)
for $i in parallelize((1,2,3)) where $i ge 2 return {"$i": $i}
