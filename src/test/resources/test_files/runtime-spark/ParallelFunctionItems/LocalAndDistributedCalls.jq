(:JIQS: ShouldRun; Output="(2, 100001, 3)" :)
let $f := function($rows as integer*) as integer { count($rows) }
return ($f((1, 2)), $f(parallelize(1 to 100001)), $f((1, 2, 3)))
