(:JIQS: ShouldRun; Output="1" :)
for $i in parallelize(1) for $l in (for $k in parallelize(1) return 1)
return $i

