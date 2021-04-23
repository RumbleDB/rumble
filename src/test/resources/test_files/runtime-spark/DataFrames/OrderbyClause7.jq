(:JIQS: ShouldRun; Output="(1, 2, 3, 1, 2, 3, 1, 2, 3)" :)
for $i in parallelize(1 to 3)
order by () ascending
return $i,
for $i in parallelize(1 to 3)
order by (), 1 ascending
return $i,
for $i in parallelize(1 to 3)
order by 1, () ascending
return $i