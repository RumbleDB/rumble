(:JIQS: ShouldRun; Output="(2, 2, 2)" :)
let $f := function($rows as integer*) as integer { count($rows) }
let $n := $f(parallelize(1 to 100001))
return for $i in parallelize(1 to 3) return $f(($i, $n))
