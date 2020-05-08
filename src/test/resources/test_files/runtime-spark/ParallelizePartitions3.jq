(:JIQS: ShouldRun; Output="(1, 2, 1, 2)" :)
for $f in parallelize((function($a) { $a }, function($b) { $b }), function($x) { $x + 1 } (1))
for $i in 1 to 2
return $f($i)

(: Partitions specified by an inline function :)
