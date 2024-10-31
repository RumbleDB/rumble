(:JIQS: ShouldRun; Output="(8, 13, 18)" :)
declare function fn ($i, $j) {parallelize($i + $j)};
for $i in (5, 10, 15)
let $partial := fn ($i, ?)
return $partial(3)

(: Test dynamic context usage for DynamicFunctionCall :)
