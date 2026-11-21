(:JIQS: ShouldCrash; ErrorCode="RBST0002" :)
let $f := function($x as integer*, $y as integer*) as integer { count($x) + count($y) }
return $f(parallelize(1 to 100001), parallelize(1 to 100001))
