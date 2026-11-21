(:JIQS: ShouldCrash; ErrorCode="RBST0002" :)
let $f := function($rows as integer*, $n as integer) as integer* { $rows }
return $f(parallelize(1 to 100001), ?)
