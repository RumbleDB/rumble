(:JIQS: ShouldRun; Output="(1, 1, 2, 1, 2, 3, 1, 2, 3, 4)" :)
for $i in 1 to 4
return
for $k in parallelize(1 to $i)
return $k

(: local to DF transition, for clause using variable defined in parent context :)
