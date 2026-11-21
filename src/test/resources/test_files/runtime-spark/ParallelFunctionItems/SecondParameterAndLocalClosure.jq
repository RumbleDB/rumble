(:JIQS: ShouldRun; Output="true" :)
let $offset := 3
let $f := function($min as integer, $rows as integer*) as integer* {
    for $x in $rows
    where $x gt $min
    return $x + $offset
}
let $result := $f(50000, parallelize(1 to 100001))
return count($result) eq 50001 and min($result) eq 50004 and max($result) eq 100004
