(:JIQS: ShouldRun; Output="(1, 2, 2, 3)" :)
for $f in parallelize(
    for $j in 1 to 1000
    return function($x) { $x + $j }
)
for $i in 1 to 2
return $f($i)

