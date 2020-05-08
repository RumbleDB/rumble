(:JIQS: ShouldRun; Output="({ "j" : 4, "i" : [ 2, 4 ], "mod" : 0 }, { "j" : 4, "i" : [ 1, 3 ], "mod" : 1 }, { "j" : 3, "i" : [ 2, 4 ], "mod" : 0 }, { "j" : 3, "i" : [ 1, 3 ], "mod" : 1 })" :)
for $i in 1 to 4, $j in 3 to 4
group by $j, $modd := $i mod 2
return {"j" : $j, "i" :$i, "mod": $modd}
