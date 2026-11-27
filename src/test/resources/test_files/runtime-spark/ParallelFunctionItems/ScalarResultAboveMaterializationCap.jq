(:JIQS: ShouldRun; Output="100001" :)
let $f := function($rows as integer*) as integer { count($rows) }
return $f(parallelize(1 to 100001))
