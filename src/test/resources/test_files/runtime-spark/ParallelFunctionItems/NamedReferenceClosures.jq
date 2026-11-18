(:JIQS: ShouldRun; Output="(2, 100001, 3)" :)
declare function local:identity($rows as integer*) as integer* { $rows };
let $f := local:identity#1
let $g := local:identity#1
return (count($f((1, 2))), count($g(parallelize(1 to 100001))), count($f((1, 2, 3))))
