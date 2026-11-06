(:JIQS: ShouldRun; Output="(2, 3, 5, 7, 11, 13, 17, 19)" :)
for $i in parallelize(2 to 20)
where every $j in 2 to $i - 1 satisfies $i mod $j gt 0
return $i
