(:JIQS: ShouldRun; Output="(1, 2, 2, 3, 3, 3, 4, 4, 4, 4)" :)
for $i in 1 to 4
return
for $k in parallelize(1 to $i)
order by $i
return $k

(: order by clause using variable defined in parent context :)
