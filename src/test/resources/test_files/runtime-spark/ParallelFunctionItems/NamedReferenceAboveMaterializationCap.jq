(:JIQS: ShouldRun; Output="100001" :)
declare function local:identity($rows as integer*) as integer* { $rows };
let $f := local:identity#1
return count($f(parallelize(1 to 100001)))
