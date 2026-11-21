(:JIQS: ShouldRun; Output="(2, 100001)" :)
declare function local:identity($rows as integer*) as integer* { $rows };
let $f := function($rows as integer*) as integer* { local:identity($rows) }
return (count(local:identity((1, 2))), count($f(parallelize(1 to 100001))))
