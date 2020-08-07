(:JIQS: ShouldRun; Output="(1, 1, 2, 1, 2, 3)" :)
for $i in 1 to 3
let $j :=
  for $a in 1 to $i
  order by $a
  return $a
return $j
