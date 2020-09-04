(:JIQS: ShouldRun; Output="(13, 18, 23)" :)
declare function fn ($i, $j, $k) {$i + $j + $k};
for $i in parallelize((5, 10, 15))
return fn(3, ?, ?) (5, ?) ($i)
