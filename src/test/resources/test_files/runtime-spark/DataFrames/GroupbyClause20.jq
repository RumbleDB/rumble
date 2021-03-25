(:JIQS: ShouldRun; Output="({ "j" : 0, "i" : [ 2, 4, 6, 8, 10 ] }, { "j" : 1, "i" : [ 1, 3, 5, 7, 9 ] })" :)
for $i in parallelize(1 to 10)
let $j := float($i mod 2)
group by $j
order by $j
return { "j" : $j, "i" : [ $i ] }
