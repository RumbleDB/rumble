(:JIQS: ShouldRun; Output="(1, 2, 2, 3, 3, 3, 4, 4, 4, 4)" :)
for $i in 1 to 4
return
for $k in parallelize(1 to $i)
return $k

(: local to DF transition, for clause using variable defined in parent context :)
