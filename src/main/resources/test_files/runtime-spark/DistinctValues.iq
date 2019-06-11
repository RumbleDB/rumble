(:JIQS: ShouldRun; Output="(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)" :)
for $result in distinct-values(for $i in parallelize(1 to 100000, 10) return (1 to 10))
order by $result
return $result
