(:JIQS: ShouldRun; Output="(8, 13, 18)" :)
declare function fn ($i, $j) {$i + $j};
for $i in parallelize((5, 10, 15))
let $partial := fn ($i, ?)
return $partial(3)

(: Test dynamic context usage for DynamicFunctionCall :)
