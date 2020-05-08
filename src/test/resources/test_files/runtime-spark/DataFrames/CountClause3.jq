(:JIQS: ShouldRun; Output="(1, 2, 3)" :)
for $i in parallelize((10, 9, 8)) count $i return $i
