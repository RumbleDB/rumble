(:JIQS: ShouldRun; Output="(1, 2, 3, 4)" :)
for $i in parallelize((1, 2, 3, 4)) where (1, 2) return $i
