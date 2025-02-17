(:JIQS: ShouldRun; Output="(4, 6)" :)
for $i in parallelize((1,2,3,4,5))
let $j := $i + 2
where $j mod 2 eq 0
return $j
