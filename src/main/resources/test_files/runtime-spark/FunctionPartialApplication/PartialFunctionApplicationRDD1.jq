(:JIQS: ShouldRun; Output="(8, 13, 18)" :)
declare function fn ($i, $j) {$i + $j};
for $i in parallelize((5, 10, 15))
return fn(3, ?) ($i)
