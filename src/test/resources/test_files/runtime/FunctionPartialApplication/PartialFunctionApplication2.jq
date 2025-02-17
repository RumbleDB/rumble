(:JIQS: ShouldRun; Output="(16, 16)" :)
declare function fn ($i, $j, $k) {$i + $j + $k};
fn(3, 5, 8),
fn(3, ?, ?) (5, ?) (8)

(: Partial function application with inline function definition :)
