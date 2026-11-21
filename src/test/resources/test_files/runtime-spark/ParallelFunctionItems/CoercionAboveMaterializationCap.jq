(:JIQS: ShouldRun; Output="true" :)
declare function local:apply($g as function(integer*) as double*) as double* {
    $g(parallelize(1 to 100001))
};
let $result := local:apply(function($rows as integer*) as integer* { $rows })
return count($result) eq 100001
    and empty(for $value in $result where not($value instance of double) return $value)
