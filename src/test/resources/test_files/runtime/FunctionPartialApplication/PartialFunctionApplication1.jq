(:JIQS: ShouldRun; Output="(8, 10)" :)
declare function fn ($i, $j) {$i + $j};
fn(3,5),
fn(3, ? ) (7)

(: Partial function application with inline function definition :)
