(:JIQS: ShouldRun; Output="(1, 2, 3)" :)
for $f in parallelize((1,2,3), function($x) { $x + 1 } (1))
return $f
