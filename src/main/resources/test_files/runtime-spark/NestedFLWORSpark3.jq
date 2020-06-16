(:JIQS: ShouldRun; Output="1" :)
for $i in parallelize(1) for $l in (for $i in parallelize(1) return 1)
return $i

(: cartesian product with variable hiding :)
