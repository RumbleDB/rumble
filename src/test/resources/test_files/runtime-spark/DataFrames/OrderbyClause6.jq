(:JIQS: ShouldRun; Output="(1, 1, 2, 1, 2, 3, 1, 2, 3, 4)" :)
for $i in 1 to 4
return
for $k in parallelize(1 to $i)
order by $i
return $k

(: order by clause using variable defined in parent context :)
