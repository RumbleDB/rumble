(:JIQS: ShouldRun; Output="(0, true, true)" :)
let $sum := sum(parallelize((), 2) treat as xs:integer*) is statically xs:integer
let $min := min(parallelize((), 2) treat as xs:integer*) is statically xs:integer?
let $max := max(parallelize((), 2) treat as xs:integer*) is statically xs:integer?
return ($sum, empty($min), empty($max))
