(:JIQS: ShouldRun; Output="({ "group" : 0, "items" : [ 2, 4, 6, 8, 10 ] }, { "group" : 1, "items" : [ 1, 3, 5, 7, 9 ] })" :)
for $i in 1 to 10
group by $j := $i mod 2
return { "group": $j, "items": $i }
