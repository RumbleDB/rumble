(:JIQS: ShouldRun; Output="({ "j" : 3, "i" : [ 1, 2, 3, 4 ] }, { "j" : 4, "i" : [ 1, 2, 3, 4 ] })" :)
for $i in 1 to 4, $j in 3 to 4
group by $j
return {"j" : $j, "i" :$i}
