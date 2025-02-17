(:JIQS: ShouldRun; Output="(2, 2, 3, 2, 3, 4)" :)
for $i in 1 to 3
let $j := for $k in 1 to $i
          return $k + 1
return $j
