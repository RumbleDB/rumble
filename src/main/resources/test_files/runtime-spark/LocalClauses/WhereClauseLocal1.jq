(:JIQS: ShouldRun; Output="(5, 1, 2, 3, 4, 5)" :)
for $i in 1 to 5 where $i lt 0 return $i,
for $i in 1 to 5 where $i gt 4 return $i,
for $i in 1 to 5 where $i le 5 return $i
