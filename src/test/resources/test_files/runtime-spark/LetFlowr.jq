(:JIQS: ShouldRun; Output="(5, 7, 9, 11, 13)" :)
for $i in parallelize((1,2,3,4,5))
let $j := $i + 2 + 1
return $i + $j
