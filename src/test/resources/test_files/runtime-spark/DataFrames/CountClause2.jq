(:JIQS: ShouldRun; Output="({ "$i" : 10, "$j" : 1 }, { "$i" : 9, "$j" : 2 }, { "$i" : 8, "$j" : 3 })" :)
for $i in parallelize((10, 9, 8)) count $j return {"$i" : $i, "$j" : $j}
