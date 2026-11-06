(:JIQS: ShouldRun; Output="" :)
for $i in parallelize((1,2,3)) where $i eq 5 return {"$i": $i}
