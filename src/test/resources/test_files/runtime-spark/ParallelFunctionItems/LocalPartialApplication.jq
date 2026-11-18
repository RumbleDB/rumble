(:JIQS: ShouldRun; Output="100001" :)
let $f := function($min as integer, $rows as integer*) as integer* {
    for $x in $rows
    where $x gt $min
    return $x
}
let $g := $f(0, ?)
return count($g(parallelize(1 to 100001)))
