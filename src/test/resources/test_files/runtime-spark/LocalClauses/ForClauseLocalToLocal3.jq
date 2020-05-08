(:JIQS: ShouldRun; Output="(4, 5, 6, 7, 8, 9, 10, 11, 12, 13)" :)
for $i in
for $j in
for $k in 1 to 10
return $k + 1
return $j + 1
return $i + 1

(: fully local for clauses :)
